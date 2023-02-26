# vertx-future-utils

易用的 Vert.x [`Future`](https://vertx.io/docs/apidocs/io/vertx/core/Future.html) 实用工具。

[![构建状态](https://img.shields.io/github/actions/workflow/status/hltj/vertx-future-utils/build.yml?logo=github&label=构建)](https://github.com/hltj/vertx-future-utils/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/me.hltj/vertx-future-utils)](https://search.maven.org/artifact/me.hltj/vertx-future-utils)
[![javadoc](https://javadoc.io/badge2/me.hltj/vertx-future-utils/javadoc.svg)](https://javadoc.io/doc/me.hltj/vertx-future-utils)
[![测试覆盖率](https://img.shields.io/codecov/c/github/hltj/vertx-future-utils?label=测试覆盖)](https://codecov.io/gh/hltj/vertx-future-utils)
[![授权许可：LGPL v3](https://img.shields.io/badge/授权许可-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![访客数](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fhltj%2Fvertx-future-utils&count_bg=%2379C83D&title_bg=%23555555&icon=github.svg&icon_color=%23E7E7E7&title=%E8%AE%BF%E5%AE%A2&edge_flat=false)](https://github.com/hltj/vertx-future-utils)

[🇺🇸 English](README.md) | 🇨🇳 简体中文

- [兼容性](#兼容性)
<!-- - [构建状态](#构建状态) -->
- [安装](#安装)
  - [Maven](#maven)
  - [Gradle Kotlin DSL](#gradle-kotlin-dsl)
  - [Gradle Groovy DSL](#gradle-groovy-dsl)
  - [排除 `vertx-core` 方式](#排除-vertx-core-方式)
- [用法示例](#用法示例)
  - [`Future` 化](#future-化)
  - [包装求值结果](#包装求值结果)
  - [为空时取默认值](#为空时取默认值)
  - [空转为失败](#空转为失败)
  - [失败或为空时取备用值](#失败或为空时取备用值)
  - [只映射非空值](#只映射非空值)
  - [失败时访问 `CompositeFuture` 自身](#失败时访问-compositefuture-自身)
  - [失败时映射 `CompositeFuture`](#失败时映射-compositefuture)
  - [保留 `CompositeFuture` 对应的各原始 `Future` 的泛型类型](#保留-compositefuture-对应的各原始-future-的泛型类型)
  - [失败时映射 `CompositeFuture` 对应的各原始 `Future`](#失败时映射-compositefuture-对应的各原始-future)
  - [失败时访问 `CompositeFuture` 以及各原始 `Future`](#失败时访问-compositefuture-以及各原始-future)
  - [组合前批量设置默认/备用值](#组合前批量设置默认备用值)

## 兼容性

### Java

- [x] Java 17
- [x] Java 11
- [x] Java 8

### Vert.x

- [x] 4.2.0 - 4.2.1（[排除 `vertx-core` 方式](#排除-vertx-core-方式)）
- [x] 4.1.0 - 4.1.2（[排除 `vertx-core` 方式](#排除-vertx-core-方式)）
- [x] 4.0.3
- [x] 4.0.0 - 4.0.2（[排除 `vertx-core` 方式](#排除-vertx-core-方式)）
- [x] 3.9.0 - 3.9.8（[排除 `vertx-core` 方式](#排除-vertx-core-方式)）
- [x] 3.8.5（[排除 `vertx-core` 方式](#排除-vertx-core-方式)）

<!--
## 构建状态

|  | Java 17 | Java 11 | Java 8|
|--|---------|---------|-------|
| [![Ubuntu](https://img.shields.io/badge/Ubuntu-black?logo=ubuntu&labelColor=black)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/1)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/2)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/3)](https://travis-ci.org/hltj/vertx-future-utils) |
| [![macOS](https://img.shields.io/badge/macOS-black?logo=apple&labelColor=black)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/4)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/5)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/6)](https://travis-ci.org/hltj/vertx-future-utils) |
| [![Windows](https://img.shields.io/badge/Windows-black?logo=windows&labelColor=black)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/7)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/8)](https://travis-ci.org/hltj/vertx-future-utils) | [![](https://travis-matrix-badges.herokuapp.com/repos/hltj/vertx-future-utils/branches/master/9)](https://travis-ci.org/hltj/vertx-future-utils) |
-->

## 安装

### Maven

``` xml
<dependency>
  <groupId>me.hltj</groupId>
  <artifactId>vertx-future-utils</artifactId>
  <version>1.1.2</version>
</dependency>
```

### Gradle Kotlin DSL

``` kotlin
implementation(group = "me.hltj", name = "vertx-future-utils", version = "1.1.2")
```

### Gradle Groovy DSL

``` groovy
implementation group: 'me.hltj', name: 'vertx-future-utils', version: '1.1.2'
```

### 排除 `vertx-core` 方式

默认依赖的 `io.vertx:vertx-core` 版本是 `4.0.3`，如果想将 `vertx-future-utils`
用于 `vertx-core` 为 `3.8.5` 到 `3.9.8`、 `4.0.0` 到 `4.0.2` 或者 `4.1.0` 到 `4.2.1` 的场景，那么需要排除默认依赖。

<details>

#### 对于 Maven

``` xml
<dependency>
  <groupId>me.hltj</groupId>
  <artifactId>vertx-future-utils</artifactId>
  <version>1.1.2</version>
  <exclusions>
    <exclusion>
      <groupId>io.vertx</groupId>
      <artifactId>vertx-core</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```

#### 对于 Gradle Kotlin DSL

``` kotlin
implementation(group = "me.hltj", name = "vertx-future-utils", version = "1.1.2") {
    exclude(group = "io.vertx", module = "vertx-core")
}
```

#### 对于 Gradle Groovy DSL

``` groovy
implementation group: 'me.hltj', name: 'vertx-future-utils', version: '1.1.2', {
    exclude group: "io.vertx", module: "vertx-core"
}
```

</details>

## 用法示例

### `Future` 化

将回调风格的 Vert.x 调用转换为 `Future` 返回风格。例如：

``` java
Future<Integer> lengthFuture = FutureUtils.<HttpResponse<Buffer>>futurize(handler ->
        WebClient.create(Vertx.vertx()).get(443, "hltj.me", "/").ssl(true).send(handler)
).map(response -> response.bodyAsString().length());
```

Vert.x 自 4.0.0 起已经提供了 `Future` 返回风格的 API，当然 `futurize()` 还可以用于第三方 API。

### 包装求值结果

将求值结果包装到 `Future` 中。例如：

``` java
Future<Integer> futureA = wrap(() -> Integer.parseInt("1")); // 成功值为 1
Future<Integer> futureB = wrap(() -> Integer.parseInt("@")); // 失败异常为 NumberFormatException
```

将函数应用的结果包装到 `Future` 中。例如：

``` java
Future<Integer> futureA = wrap("1", Integer::parseInt); // 成功值为 1
Future<Integer> futureB = wrap("@", Integer::parseInt); // 失败异常为 NumberFormatException
```

如果求值结果自身就是 `Future`，那么可以使用 `joinWrap()`（或其别名 `flatWrap()`）
将嵌套的两层 `Future` 展平。例如：

``` java
Future<Integer> future0 = wrap("0", Integer::parseInt);
Future<Integer> future1 = wrap("1", Integer::parseInt);

Future<Integer> futureA = joinWrap(() -> future0.map(i -> 2 / i)); // 失败异常为 ArithmeticException
Future<Integer> futureB = joinWrap(() -> future1.map(i -> 2 / i)); // 成功值为 2

Function<String, Future<Integer>> stringToIntFuture = s -> FutureUtils.wrap(s, Integer::parseInt);

Future<Integer> futureC = joinWrap("1", stringToIntFuture); // 成功值为 1
Future<Integer> futureD = joinWrap("@", stringToIntFuture); // 失败异常为 NumberFormatException
```

### 为空时取默认值

如果一个 `Future` 成功值为 `null`，那么将其值以默认值取代。例如：

``` java
// 成功值为 1
Future<Integer> plusOneFuture = defaultWith(Future.succeededFuture(), 0).map(i -> i + 1);
```

惰性求值版：

``` java
Future<Double> doubleFuture = FutureUtils.<Double>defaultWith(Future.succeededFuture(), () -> {
    double rand = Math.random();
    System.out.println("default with random value: " + rand);
    return rand;
}).map(d -> d + 1);
```

如果想将值为 `null` 的 `Future` 替换为另一个 `Future`
（异步且/或可能失败），那么可以使用 `flatDefaultWith()`。例如：

``` java
Future<Integer> cachedCountFuture = getCountFutureFromCache().otherwiseEmpty();
Future<Integer> countFuture = flatDefaultWith(countFuture, () -> getCountFutureViaHttp());
```

### 空转为失败

如果 `Future` 失败或者成功值非空，那么返回该 `Future` 自身。
否则（即成功值为 `null`） 返回失败异常为 `NullPointerException` 的 `Future`。例如：

``` java
nonEmpty(future).onFailure(t -> log.error("either failed or empty, ", t);
```

### 失败或为空时取备用值

如果 `Future` 失败或成功值为 `null`，那么返回成功值为备用值的 `Future`。
否则（即成功值非空） 返回该 `Future` 自身，例如：

``` java
Future<Integer> plusOneFuture = fallbackWith(intFuture, 0).map(i -> i + 1);
```

惰性求值版：

``` java
Future<Double> plusOneFuture = FutureUtils.<Double>fallbackWith(doubleFuture, throwableOpt ->
        throwableOpt.map(t -> {
            log.warn("fallback error with -0.5, the error is: ", t);
            return -0.5;
        }).orElseGet(() -> {
            log.warn("fallback empty with 0.5");
            return 0.5;
        })
).map(d -> d + 1);
```

还可以对失败与为空分别用不同的 lambda 表达式：

``` java
Future<Double> plusOneFuture = fallbackWith(doubleFuture, error -> {
    System.out.println("fallback error with -0.5, the error is " + error);
    return -0.5;
}, () -> {
    System.out.println("fallback empty with 0.5");
    return 0.5;
}).map(d -> d + 1);
```

如果想将失败或成功值为空的 `Future` 替换为另一个 `Future`
（异步且/或可能失败），那么可以使用 `flatFallbackWith()`。例如：

``` java
Future<Integer> cachedCountFuture = getCountFutureFromCache();
Future<Integer> countFuture1 = flatFallbackWith(countFuture, _throwableOpt -> getCountFutureViaHttp());
Future<Integer> countFuture2 = flatFallbackWith(
        countFuture, throwable -> getCountFutureViaHttpOnFailure(throwable), () -> getCountFutureViaHttpOnEmpty()
);
```

### 只映射非空值

仅在 `Future` 成功值非空时对其值进行映射。如果该参数 `Future` 成功值为 `null`，
那么 `mapSome()` 也返回一个成功值为 `null` 的 `Future`。例如：

``` java
Future<List<Integer>> intsFuture = getIntegers();
Future<List<String>> hexStringsFuture = mapSome(intsFuture, ints ->
        ints.stream().map(i -> Integer.toString(i, 16)).collect(Collectors.toUnmodifiableList())
);
```

如果映射函数自身就返回 `Future`，那么可以使用 `flatMapSome()` 来展平嵌套的 `Future`。例如：

``` java
Future<String> userIdFuture = getUserIdFuture();
Future<User> userFuture = flatMapSome(userIdFuture, id -> getUserFuture(id));
```

### 失败时访问 `CompositeFuture` 自身

当 [`CompositeFuture`](https://vertx.io/docs/apidocs/io/vertx/core/CompositeFuture.html) 失败时， 无法直接在
[`onComplete()`](https://vertx.io/docs/apidocs/io/vertx/core/CompositeFuture.html#onComplete-io.vertx.core.Handler-)
或 [`onFailure`](https://vertx.io/docs/apidocs/io/vertx/core/CompositeFuture.html#onFailure-io.vertx.core.Handler-)
的参数 lambda 表达式内部访问该 `CompositeFuture` 自身。
一个变通方式是引入局部变量。例如：

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

但是这并不流畅，而且还会引入额外的变量，当一次次这样重复时尤为明显。
对于这种情况， 可以改用 `CompositeFutureWrapper#use()`。例如：

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

当然，并不建议直接使用 `CompositeFutureWrapper`，请改用更为强大的子类
`CompositeFutureTuple[2-9]`。

### 失败时映射 `CompositeFuture`

当 `CompositeFuture` 失败时，不会调用其 `map()`/`flatMap()` 方法传入的 lambda 表达式。
如果还是想映射其中部分成功的结果，那么可以使用 `CompositeFutureWrapper#through()`（或其别名
`mapAnyway()`）。例如：

``` java
Future<Double> sumFuture = CompositeFutureWrapper.of(
        CompositeFuture.join(Future.succeededFuture(1.0), Future.<Integer>failedFuture("error"))
).through(composite -> (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
        (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
);
```

如果映射函数自身就返回 `Future`，那么可以使用 `CompositeFutureWrapper#joinThrough()`（或其别名
`flatMapAnyway()`）将嵌套的结果 `Future` 展平。例如：

``` java
Future<Double> sumFuture = CompositeFutureWrapper.of(
        CompositeFuture.join(Future.succeededFuture(1.0), Future.<Integer>failedFuture("error"))
).joinThrough(composite -> wrap(() -> composite.<Double>resultAt(0) + composite.<Integer>resultAt(1)));
```

当然，并不建议直接使用 `CompositeFutureWrapper`，请改用更为强大的子类
`CompositeFutureTuple[2-9]`。

### 保留 `CompositeFuture` 对应的各原始 `Future` 的泛型类型

在 `CompositeFuture` 中，所有原始 `Future` 的类型都被擦除了。
于是必须频繁指定结果的类型参数。 例如：

``` java
Future<Integer> future0 = Future.succeededFuture(2);
Future<Double> future1 = Future.succeededFuture(3.5);
Future<Double> productFuture = CompositeFuture.all(future0, future1).map(
        composite -> composite.<Double>resultAt(0) * composite.<Integer>resultAt(1)
);
```

其结果 `productFuture` 是“成功，值为 7.0”，没错吧？很不幸，错了。而是“失败，错误为 ClassCastException”，
因为类型参数指定错了。应该是 `(Integer, Double)`，而不是 `(Double, Integer)`！
可以用 `CompositeFutureTuple2#mapTyped()`（或其别名 `applift()`）来避免这种易错情形。例如：

``` java
Future<Integer> future0 = Future.succeededFuture(2);
Future<Double> future1 = Future.succeededFuture(3.5);
Future<Double> productFuture = FutureUtils.all(future0, future1).mapTyped((i, d) -> i * d);
```

在 `mapTyped()` 的参数 lambda 表达式内部不需要再手动指定类型参数了，
因为 `CompositeFutureTuple2` 已经保留了这些类型。
此外，由于样板代码的减少，上述代码显著简化了。

如果该 lambda 表达式的结果自身就是 `Future`，那么可以使用 `CompositeFutureTuple2#flatMapTyped()`（或其别名
`joinApplift()`）来展平嵌套的结果 `Future`。例如：

``` java
Future<Integer> future0 = Future.succeededFuture(2);
Future<Double> future1 = Future.failedFuture("error");
Future<Double> productFuture = FutureUtils.all(future0, future1).flatMapTyped((i, d) -> wrap(() -> i * d));
```

还有 `any()` 与 `join()` 工厂方法，以及用于 3-9 元的
`CompositeFutureTuple3` 到 `CompositeFutureTuple9`。

### 失败时映射 `CompositeFuture` 对应的各原始 `Future`

在 `CompositeFutureTuple[2-9]` 中，还重载了 `through()` 与 `joinThrough()`（及其别名
`mapAnyway` 与 `flatMapAnyway`）方法，它们以各原始 `Future` 作为参数来调用其 lambda 表达式参数。
例如：

``` java
Future<Double> sumFuture = FutureUtils.join(
        Future.succeededFuture(1.0), Future.<Integer>failedFuture("error")
).through((fut0, fut1) -> fallbackWith(fut0, 0.0).result() + fallbackWith(fut1, 0).result());
``` 

### 失败时访问 `CompositeFuture` 以及各原始 `Future`

在 `CompositeFutureTuple[2-9]` 中，还重载了 `use()` 方法，它以该 `CompositeFuture`
自身以及各原始 `Future` 作为参数来调用其 lambda 表达式参数。例如：

``` java
Future<Double> future0 = Future.succeededFuture(1.0);
Future<Integer> future1 = Future.failedFuture("error");
FutureUtils.join(future0, future1).use((composite, fut0, fut1) -> composite.onComplete(_ar -> {
        String status = composite.succeeded() ? "succeeded" : "failed";
        System.out.println(String.format("composite future %s, original futures: (%s, %s)", status, fut0, fut1))
}));
```

此外，还有一个新方法 `with()`，它与 `use()` 类似，只是还返回一个值。例如：

``` java
Future<Double> future0 = Future.succeededFuture(1.0);
Future<Integer> future1 = Future.failedFuture("error");
Future<String> stringFuture = join(future0, future1).with((composite, fut0, fut1) -> composite.compose(
        _x -> Future.succeededFuture(String.format("succeeded: (%s, %s)", fut0, fut1)),
        _t -> Future.succeededFuture(String.format("failed: (%s, %s)", fut0, fut1))
));
```

### 组合前批量设置默认/备用值

我们可以在组合之前为每个原始 `Future` 设置默认值以避免空（`null`）检测。例如：

``` java
Future<Integer> future0 = defaultWith(futureA, 1);
Future<Integer> future1 = defaultWith(futureB, 1);
Future<Double> future2 = defaultWith(futureC, 1.0);
Future<Double> productFuture = FutureUtils.all(future0, future1, future2)
        .mapTyped((i1, i2, d) -> i1 * i2 * d);
```

其实完全没必要引入那么多临时变量，可以用 `FutureTuple3#defaults()`
来简化之。 例如：

``` java
Future<Double> productFuture = tuple(futureA, futureB, futureC)
        .defaults(1, 1, 1.0)
        .join()
        .mapTyped((i1, i2, d) -> i1 * i2 * d);
```

工厂方法 `tuple()` 创建了一个 `FutureTuple3` 对象，然后调用其 `defaults()`
方法来设置各个默认值，再调用其 `join()` 方法得到一个 `CompositeFutureTuple3` 对象。

`FutureTuple[2-9]` 的另一个好用的方法是 `fallback()`，与 `defaults()` 类似，
可以一次性设置各个备用值。例如：

``` java
Future<Double> productFuture = tuple(futureA, futureB, futureC)
        .fallback(1, 1, 1.0)
        .all()
        .mapTyped((i1, i2, d) -> i1 * i2 * d);
```

`FutureTuple[2-9]` 中还有其他类似方法：`mapEmpty()`、 `otherwise()`、 `otherwiseEmpty()`
以及带有副作用的 `otherwise`、 `defaults()`、 `fallback()` 重载方法，
参见其 [Java doc](https://javadoc.io/doc/me.hltj/vertx-future-utils/latest/me/hltj/vertx/future/FutureTuple2.html#method.summary)
。例如：

``` java
Future<String> productFutureA = tuple(futureA, futureB, futureC)
        .otherwiseEmpty()
        .any()
        .mapTyped((i1, i2, d) -> String.format("results: (%d, %d, %f)", i1, i2, d));

Future<Double> productFutureB = tuple(futureA, futureB, futureC).fallback(
        t -> log.error("fallback on failure", t),
        () -> log.warn("fallback on empty"),
        1, 1, 1.0
).all().mapTyped((i1, i2, d) -> i1 * i2 * d);
```
