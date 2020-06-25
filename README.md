# vertx-future-utils
Convenient Utilities for Vert.x [`Future`](https://vertx.io/docs/apidocs/io/vertx/core/Future.html).

## Usage Example

 - [Futurization](#futurization)
 - [Default Value on Empty](#default-value-on-empty)
 - [Fallback Values on Failure/Empty](#fallback-values-on-failureempty)
 - [Wrapping Evaluation Result](#wrapping-evaluation-result)
 - [Access Original `CompositeFuture` on Failure](#access-original-compositefuture-on-failure)
 - [Mapping `CompositeFuture` on Failure](#mapping-compositefuture-on-failure)
 
### Futurization
Convert a callback style Vert.x call to `Future` result style:

``` java
Future<Integer> lengthFuture = FutureUtils.<HttpResponse<Buffer>>futurize(handler ->
        WebClient.create(Vertx.vertx()).get(443, "hltj.me", "/").ssl(true).send(handler)
).map(response -> response.bodyAsString().length());
```

Vert.x will provide `Future` result style APIs since 4.0, while `futurize()` can also be used for third party APIs.

### Default Value on Empty
If a `Future` succeed with `null`, map it with a default value:

``` java
// Succeed with 1
Future<Integer> plusOneFuture = defaultWith(Future.succeededFuture(), 0).map(i -> i + 1);
```

the lazy version:

``` java
Future<Double> doubleFuture = FutureUtils.<Double>defaultWith(Future.succeededFuture(), () -> {
    double rand = Math.random();
    System.out.println("default with random value: " + rand);
    return rand;
}).map(d -> d + 1);
```

### Fallback Values on Failure/Empty
If a `Future` failed or succeed with `null`, replace it with a `Future` that succeed with a default value:

``` java
Future<Integer> plusOneFuture = fallbackWith(intFuture, 0).map(i -> i + 1);
```

the lazy version:

``` java
Future<Double> plusOneFuture = FutureUtils.<Double>fallbackWith(doubleFuture, errorOpt -> {
    if (errorOpt.isPresent()) {
        log.warn("fallback error with -0.5, the error is: ", errorOpt.get());
        return -0.5;
    } else {
        log.warn("fallback empty with 0.5");
        return 0.5;
    }
}).map(d -> d + 1);
```

or with separated lambdas for failure & empty:

``` java
Future<Double> plusOneFuture = fallbackWith(doubleFuture, error -> {
    System.out.println("fallback error with -0.5, the error is " + error);
    return -0.5;
}, () -> {
    System.out.println("fallback empty with 0.5");
    return 0.5;
}).map(d -> d + 1);
```

### Wrapping Evaluation Result
Wraps an evaluation result within `Future`:

``` java
Future<Integer> futureA = wrap(() -> Integer.parseInt("1")); // Succeed with 1
Future<Integer> futureB = wrap(() -> Integer.parseInt("@")); // Failed with a NumberFormatException
```

Wraps a function application result within `Future`:

``` java
Future<Integer> futureA = wrap("1", Integer::parseInt); // Succeed with 1
Future<Integer> futureB = wrap("@", Integer::parseInt); // Failed with a NumberFormatException
```

If the evaluation result itself is a `Future`, use `joinWrap()`(or it's alias `flatWrap()`) to flatten the nested result `Future`:

``` java
Future<Integer> future0 = wrap("0", Integer::parseInt);
Future<Integer> future1 = wrap("1", Integer::parseInt);

Future<Integer> futureA = joinWrap(() -> future0.map(i -> 2 / i)); // Failed with a ArithmeticException
Future<Integer> futureB = joinWrap(() -> future1.map(i -> 2 / i)); // Succeed with 2

Function<String, Future<Integer>> stringToIntFuture = s -> FutureUtils.wrap(s, Integer::parseInt);

Future<Integer> futureC = joinWrap("1", stringToIntFuture); // Succeed with 1
Future<Integer> futureD = joinWrap("@", stringToIntFuture); // Failed with a NumberFormatException
```

### Access Original `CompositeFuture` on Failure
When a [`CompositeFuture`](https://vertx.io/docs/apidocs/io/vertx/core/CompositeFuture.html) failed, we cannot access the original `CompositeFuture` directly inside the lambda argument of [`onComplete()`](https://vertx.io/docs/apidocs/io/vertx/core/CompositeFuture.html#onComplete-io.vertx.core.Handler-) or [`onFailure`](https://vertx.io/docs/apidocs/io/vertx/core/CompositeFuture.html#onFailure-io.vertx.core.Handler-).
A work round is introducing a local variable, e.g:

``` java
CompositeFuture composite = CompositeFuture.join(
        Future.succeededFuture(1), Future.<Double>failedFuture("fail"), Future.succeededFuture("hello")
);
composite.onFailure(t -> {
    System.out.println("The CompositeFuture failed, where these base Futures succeed:");
    for (int i = 0; i < composite.size(); i++) {
        if (composite.succeeded(i)) {
            System.out.println(("Future-" + i));
        }
    }
});
```

But this is not fluent and cause an extra variable introduced, especially we repeat to do this again and again.
In this case, we can use `CompositeFutureWrapper#use()` instead:

``` java
CompositeFutureWrapper.of(CompositeFuture.join(
        Future.succeededFuture(1), Future.<Double>failedFuture("fail"), Future.succeededFuture("hello")
)).use(composite -> composite.onFailure(t -> {
    System.out.println("The CompositeFuture failed, where these base Futures succeed:");
    for (int i = 0; i < composite.size(); i++) {
        if (composite.succeeded(i)) {
            System.out.println(("Future-" + i));
        }
    }
}));
```

While it's not recommended to use `CompositeFutureWrapper` directly, please use more powerful `CompositeFutureTuple[2-9]` instead.

### Mapping `CompositeFuture` on Failure
When a `CompositeFuture` failed, the `map()`/`flatMap()` method won't be invoked.
If we still want to map the partial succeed results, we can use `CompositeFutureWrapper#through()` or it's alias `mapAnyway`:

``` java
Future<Double> sumFuture = CompositeFutureWrapper.of(
        CompositeFuture.join(Future.succeededFuture(1.0), Future.<Integer>failedFuture("error"))
).through(composite -> (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
        (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
);
```

If the mapper itself returns a `Future`, we can use `CompositeFutureWrapper#joinThrough()` or it's alias `flatMapAnyway` to flatten the nested result `Future`:

``` java
Future<Double> sumFuture = CompositeFutureWrapper.of(
        CompositeFuture.join(Future.succeededFuture(1.0), Future.<Integer>failedFuture("error"))
).joinThrough(composite -> wrap(() -> composite.<Double>resultAt(0) + composite.<Integer>resultAt(1)));
```

While it's not recommended to use `CompositeFutureWrapper` directly, please use more powerful `CompositeFutureTuple[2-9]` instead.
