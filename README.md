# vertx-future-utils
Convenient Utilities for Vert.x [`Future`](https://vertx.io/docs/apidocs/io/vertx/core/Future.html).

[![Build Status](https://img.shields.io/travis/hltj/vertx-future-utils)](https://travis-ci.org/hltj/vertx-future-utils)
[![Maven Central](https://img.shields.io/maven-central/v/me.hltj/vertx-future-utils)](https://search.maven.org/search?q=g:me.hltj%20AND%20a:vertx-future-utils)
[![javadoc](https://javadoc.io/badge2/me.hltj/vertx-future-utils/javadoc.svg)](https://javadoc.io/doc/me.hltj/vertx-future-utils)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/hltj/vertx-future-utils.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/hltj/vertx-future-utils/alerts/)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/hltj/vertx-future-utils.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/hltj/vertx-future-utils/context:java)
[![Codecov](https://img.shields.io/codecov/c/github/hltj/vertx-future-utils)](https://codecov.io/gh/hltj/vertx-future-utils)
[![License: LGPL v3](https://img.shields.io/badge/license-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![HitCount](http://hits.dwyl.io/hltj/vertx-future-utils.svg)](https://github.com/hltj/vertx-future-utils)

- [Compatibility](#compatibility)
- [Build Status](#build-status)
- [Install](#install)
  - [Maven](#maven)
  - [Gradle Kotlin DSL](#gradle-kotlin-dsl)
  - [Gradle Groovy DSL](#gradle-groovy-dsl)
  - [With `vertx-core` Excluded](#with-vertx-core-excluded)
- [Usage Example](#usage-example)
  - [Futurization](#futurization)
  - [Default Value on Empty](#default-value-on-empty)
  - [Fallback Values on Failure/Empty](#fallback-values-on-failureempty)
  - [Wrapping Evaluation Result](#wrapping-evaluation-result)
  - [Access `CompositeFuture` Itself on Failure](#access-compositefuture-itself-on-failure)
  - [Mapping `CompositeFuture` on Failure](#mapping-compositefuture-on-failure)
  - [Keep Generic Type of the Original `Future`s of `CompositeFuture`](#keep-generic-type-of-the-original-futures-of-compositefuture)
  - [Mapping the Original `Future`s of a `CompositeFuture` on Failure](#mapping-the-original-futures-of-a-compositefuture-on-failure)
  - [Access `CompositeFuture` and the Original `Future`s on Failure](#access-compositefuture-and-the-original-futures-on-failure)
  - [Setting Default/Fallback Values before Composition](#setting-defaultfallback-values-before-composition)

## Compatibility

### Java
- [x] Java 8
- [x] Java 11
- [x] Java 14
- [x] Java 15

### Vert.x
- [x] 4.0.0
- [x] 3.9.4 ([with `vertx-core` excluded](#with-vertx-core-excluded))
- [x] 3.9.3 ([with `vertx-core` excluded](#with-vertx-core-excluded))
- [x] 3.9.2 ([with `vertx-core` excluded](#with-vertx-core-excluded))
- [x] 3.9.1 ([with `vertx-core` excluded](#with-vertx-core-excluded))
- [x] 3.9.0 ([with `vertx-core` excluded](#with-vertx-core-excluded))
- [x] 3.8.5 ([with `vertx-core` excluded](#with-vertx-core-excluded))

## Build Status

|  | Java 14 | Java 11 | Java 8|
|--|---------|---------|-------|
| [![Ubuntu 18.04](https://img.shields.io/badge/Ubuntu_18-black?logo=ubuntu&labelColor=black)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/1)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/2)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/3)](https://travis-ci.org/hltj/vertx-future-utils) |
| [![macOS](https://img.shields.io/badge/macOS-black?logo=apple&labelColor=black)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/4)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/5)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/6)](https://travis-ci.org/hltj/vertx-future-utils) |
| [![Windows](https://img.shields.io/badge/Windows-black?logo=windows&labelColor=black)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/7)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/8)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/9)](https://travis-ci.org/hltj/vertx-future-utils) |

## Install
### Maven

``` xml
<dependency>
  <groupId>me.hltj</groupId>
  <artifactId>vertx-future-utils</artifactId>
  <version>1.1.0</version>
</dependency>
```

### Gradle Kotlin DSL

``` kotlin
implementation(group = "me.hltj", name = "vertx-future-utils", version = "1.1.0")
```

### Gradle Groovy DSL

``` groovy
implementation 'me.hltj:vertx-future-utils:1.1.0'
```

### With `vertx-core` Excluded

The default dependent version of `io.vertx:vertx-core` is `4.0.0`, if you want to use `vertx-future-utils`
with `vertx-core` `3.8.5` or `3.9.0` to `3.9.4`, please exclude the default one.

<details>

#### for Maven

``` xml
<dependency>
  <groupId>me.hltj</groupId>
  <artifactId>vertx-future-utils</artifactId>
  <version>1.1.0</version>
  <exclusions>
    <exclusion>
      <groupId>io.vertx</groupId>
      <artifactId>vertx-core</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```

#### for Gradle Kotlin DSL

``` kotlin
implementation(group = "me.hltj", name = "vertx-future-utils", version = "1.1.0") {
    exclude(group = "io.vertx", module = "vertx-core")
}
```

#### for Gradle Groovy DSL

``` groovy
implementation 'me.hltj:vertx-future-utils:1.1.0', {
    exclude group: "io.vertx", module: "vertx-core"
}
```
</details>

## Usage Example 
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

If the evaluation result itself is a `Future`, use `joinWrap()`(or its alias `flatWrap()`)
to flatten the nested result `Future`s:

``` java
Future<Integer> future0 = wrap("0", Integer::parseInt);
Future<Integer> future1 = wrap("1", Integer::parseInt);

Future<Integer> futureA = joinWrap(() -> future0.map(i -> 2 / i)); // Failed with a ArithmeticException
Future<Integer> futureB = joinWrap(() -> future1.map(i -> 2 / i)); // Succeed with 2

Function<String, Future<Integer>> stringToIntFuture = s -> FutureUtils.wrap(s, Integer::parseInt);

Future<Integer> futureC = joinWrap("1", stringToIntFuture); // Succeed with 1
Future<Integer> futureD = joinWrap("@", stringToIntFuture); // Failed with a NumberFormatException
```

### Access `CompositeFuture` Itself on Failure
When a [`CompositeFuture`](https://vertx.io/docs/apidocs/io/vertx/core/CompositeFuture.html) failed,
we cannot access the `CompositeFuture` itself directly inside the lambda argument of
[`onComplete()`](https://vertx.io/docs/apidocs/io/vertx/core/CompositeFuture.html#onComplete-io.vertx.core.Handler-)
or [`onFailure`](https://vertx.io/docs/apidocs/io/vertx/core/CompositeFuture.html#onFailure-io.vertx.core.Handler-).
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

While it's not recommended to use `CompositeFutureWrapper` directly, please use more powerful subclasses
`CompositeFutureTuple[2-9]` instead.

### Mapping `CompositeFuture` on Failure
When a `CompositeFuture` failed, its `map()`/`flatMap()` method won't be invoked.
If we still want to map the partial succeed results, we can use `CompositeFutureWrapper#through()` (or its alias
`mapAnyway()`):

``` java
Future<Double> sumFuture = CompositeFutureWrapper.of(
        CompositeFuture.join(Future.succeededFuture(1.0), Future.<Integer>failedFuture("error"))
).through(composite -> (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
        (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
);
```

If the mapper itself returns a `Future`, we can use `CompositeFutureWrapper#joinThrough()` (or its alias
`flatMapAnyway()`) to flatten the nested result `Future`s:

``` java
Future<Double> sumFuture = CompositeFutureWrapper.of(
        CompositeFuture.join(Future.succeededFuture(1.0), Future.<Integer>failedFuture("error"))
).joinThrough(composite -> wrap(() -> composite.<Double>resultAt(0) + composite.<Integer>resultAt(1)));
```

While it's not recommended to use `CompositeFutureWrapper` directly, please use more powerful subclasses
`CompositeFutureTuple[2-9]` instead.

### Keep Generic Type of the Original `Future`s of `CompositeFuture`
In a `CompositeFuture`, all the original `Future`s are type erased.
We have to specify type parameters for the results frequently. e.g.:

``` java
Future<Integer> future0 = Future.succeededFuture(2);
Future<Double> future1 = Future.succeededFuture(3.5);
Future<Double> productFuture = CompositeFuture.all(future0, future1).map(
        composite -> composite.<Double>resultAt(0) * composite.<Integer>resultAt(1)
);
```

The result `productFuture` is 'succeed with 7.0'? Unfortunately, NO. It is 'failed with a ClassCastException',
because the type parameters are misspecified. They are `(Integer, Double`), not `(Double, Integer)`!
We can use `CompositeFutureTuple2#applift()` (or its alias `mapTyped()`) to avoid this error-prone case:

``` java
Future<Integer> future0 = Future.succeededFuture(2);
Future<Double> future1 = Future.succeededFuture(3.5);
Future<Double> productFuture = FutureUtils.all(future0, future1).applift((i, d) -> i * d);
```

We needn't specify the type parameters manually inside the lambda argument of `applift()` anymore,
because the `CompositeFutureTuple2` has already kept them.
Moreover, the code is significantly simplified with the boilerplate code reduced.

If the lambda result itself is a `Future`, we can use `CompositeFutureTuple2#joinApplift()` (or its alias
`flatMapTyped()`) to flatten the nested result `Future`s, e.g:

``` java
Future<Integer> future0 = Future.succeededFuture(2);
Future<Double> future1 = Future.failedFuture("error");
Future<Double> productFuture = FutureUtils.all(future0, future1).joinApplift((i, d) -> wrap(() -> i * d));
```

There are also `any()` and `join()` factory methods, and `CompositeFutureTuple3` to `CompositeFutureTuple9`
for 3-9 arities.

### Mapping the Original `Future`s of a `CompositeFuture` on Failure
In `CompositeFutureTuple[2-9]`, there are additional overload `through()` & `joinThrough()` (or their alias
`mapAnyway` & `flatMapAnyway`) methods, they provide the original `Future`s as parameters to invoke the lambda argument,
e.g. :

``` java
Future<Double> sumFuture = FutureUtils.join(
        Future.succeededFuture(1.0), Future.<Integer>failedFuture("error")
).through((fut0, fut1) -> fallbackWith(fut0, 0.0).result() + fallbackWith(fut1, 0).result());
``` 

### Access `CompositeFuture` and the Original `Future`s on Failure
In `CompositeFutureTuple[2-9]`, there is an additional overload `use()` method, it provides the `CompositeFuture` itself
as well as the original `Future`s as parameters to invoke the lambda argument, e.g.:

``` java
Future<Double> future0 = Future.succeededFuture(1.0);
Future<Integer> future1 = Future.failedFuture("error");
FutureUtils.join(future0, future1).use((composite, fut0, fut1) -> composite.onComplete(_ar -> {
        String status = composite.succeeded() ? "succeeded" : "failed";
        System.out.println(String.format("composite future %s, original futures: (%s, %s)", status, fut0, fut1))
}));
```

Moreover, there is a new method `with()` that likes `use()` but return a value, e.g.:

``` java
Future<Double> future0 = Future.succeededFuture(1.0);
Future<Integer> future1 = Future.failedFuture("error");
Future<String> stringFuture = join(future0, future1).with((composite, fut0, fut1) -> composite.compose(
        _x -> Future.succeededFuture(String.format("succeeded: (%s, %s)", fut0, fut1)),
        _t -> Future.succeededFuture(String.format("failed: (%s, %s)", fut0, fut1))
));
```

### Setting Default/Fallback Values before Composition
We can set default values for each original `Future`s before composition to avoid `null` check, e.g.:

``` java
Future<Integer> future0 = defaultWith(futureA, 1);
Future<Integer> future1 = defaultWith(futureB, 1);
Future<Double> future2 = defaultWith(futureC, 1.0);
Future<Double> productFuture = FutureUtils.all(future0, future1, future2)
        .applift((i1, i2, d) -> i1 * i2 * d);
```

In fact, it's unnecessary to introduce so many temporary variables, we can use `FutureTuple3#defaults()` to simplify it:

``` java
Future<Double> productFuture = tuple(futureA, futureB, futureC)
        .defaults(1, 1, 1.0)
        .join()
        .applift((i1, i2, d) -> i1 * i2 * d);
```

the factory method `tuple()` creates a `FutureTuple3` object, and then invoke its `defaults()`
method to set default values, then invoke its `join()` method to get a `CompositeFutureTuple3` object.

Another useful method of `FutureTuple[2-9]` is `fallback`, just likes `defaults()`, we can use it to set the fallback
values at once:

``` java
Future<Double> productFuture = tuple(futureA, futureB, futureC)
        .fallback(1, 1, 1.0)
        .all()
        .applift((i1, i2, d) -> i1 * i2 * d);
```

There are other similar methods in `FutureTuple[2-9]`: `mapEmpty()`, `otherwise()`, `otherwiseEmpty()`
and overload methods for `otherwise`, `defaults()`, `fallback()` with effect,
see the [Java doc](https://javadoc.io/doc/me.hltj/vertx-future-utils/latest/me/hltj/vertx/future/FutureTuple2.html#method.summary).
e.g.:

``` java
Future<String> productFutureA = tuple(futureA, futureB, futureC)
        .otherwiseEmpty()
        .any()
        .applift((i1, i2, d) -> String.format("results: (%d, %d, %f)", i1, i2, d));

Future<Double> productFutureB = tuple(futureA, futureB, futureC)
        .fallback(
                t -> log.error("fallback on failure", t),
                () -> log.warn("fallback on empty"),
                1, 1, 1.0
        )
        .all()
        .applift((i1, i2, d) -> i1 * i2 * d);
```
