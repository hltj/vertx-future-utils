/*
 * vertx-future-utils - Convenient Utilities for Vert.x Future
 * https://github.com/hltj/vertx-future-utils
 *
 * Copyright (C) 2020  JiaYanwei  https://hltj.me
 *
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Please contact me (jiaywe#at#gmail.com, replace the '#at#' with '@')
 * if you need additional information or have any questions.
 */
package me.hltj.vertx;

import io.vertx.core.*;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@ExtensionMethod({Future.class, FutureExtensions.class})
class FutureUtilsTest {

    @SneakyThrows
    @Test
    void futurize() {
        val future0 = FutureUtils.<Integer>futurize(handler -> delayParseInt("1", handler)).map(i -> i + 1);
        val future1 = FutureUtils.<Integer>futurize(handler -> delayParseInt("%", handler)).map(i -> i + 1);

        CountDownLatch latch = new CountDownLatch(1);
        CompositeFuture.join(future0, future1).onComplete(future -> {
            SharedTestUtils.assertSucceedWith(2, future0);
            assertFailedWith(NumberFormatException.class, "For input string: \"%\"", future1);
            latch.countDown();
        });

        latch.await();
    }

    @Test
    void defaultWith() {
        SharedTestUtils.assertSucceedWith("value", Future.succeededFuture("value").defaults("default"));
        SharedTestUtils.assertSucceedWith("default", Future.succeededFuture().defaults("default"));
        SharedTestUtils.assertFailedWith("error", Future.failedFuture("error").defaults("default"));
    }

    @Test
    void defaultWith_supplier() {
        val numbers = new HashSet<Integer>();

        SharedTestUtils.assertSucceedWith("value", Future.succeededFuture("value").<String>defaults(() -> {
            numbers.add(0);
            return "default";
        }));
        assertFalse(numbers.contains(0));

        SharedTestUtils.assertSucceedWith("default", Future.succeededFuture().<String>defaults(() -> {
            numbers.add(1);
            return "default";
        }));
        assertTrue(numbers.contains(1));

        SharedTestUtils.assertFailedWith("error", Future.failedFuture("error").<String>defaults(() -> {
            numbers.add(2);
            return "default";
        }));
        assertFalse(numbers.contains(2));
    }

    @Test
    void flatDefaultWith() {
        val numbers = new HashSet<Integer>();

        SharedTestUtils.assertSucceedWith("value", Future.succeededFuture("value").flatDefault(() -> {
            numbers.add(0);
            return Future.succeededFuture("default");
        }));
        assertFalse(numbers.contains(0));

        SharedTestUtils.assertSucceedWith("value", Future.succeededFuture("value").flatDefault(() -> {
            numbers.add(1);
            return Future.failedFuture("new failure");
        }));
        assertFalse(numbers.contains(1));

        SharedTestUtils.assertSucceedWith("default", Future.succeededFuture().flatDefault(() -> {
            numbers.add(2);
            return Future.succeededFuture("default");
        }));
        assertTrue(numbers.contains(2));

        SharedTestUtils.assertFailedWith("new failure", Future.succeededFuture().flatDefault(() -> {
            numbers.add(3);
            return Future.failedFuture("new failure");
        }));
        assertTrue(numbers.contains(3));

        SharedTestUtils.assertFailedWith("error", Future.failedFuture("error").flatDefault(() -> {
            numbers.add(4);
            return Future.succeededFuture("default");
        }));
        assertFalse(numbers.contains(4));

        SharedTestUtils.assertFailedWith("error", Future.failedFuture("error").flatDefault(() -> {
            numbers.add(5);
            return Future.failedFuture("new failure");
        }));
        assertFalse(numbers.contains(5));
    }

    @Test
    void fallbackWith() {
        SharedTestUtils.assertSucceedWith("value", Future.succeededFuture("value").fallback("fallback"));
        SharedTestUtils.assertSucceedWith("fallback", Future.succeededFuture().fallback("fallback"));
        SharedTestUtils.assertSucceedWith("fallback", Future.failedFuture("error").fallback("fallback"));
    }

    @Test
    void fallbackWith_function() {
        val throwables = new ArrayList<Throwable>();
        val numbers = new HashSet<Integer>();

        SharedTestUtils.assertSucceedWith("value", Future.succeededFuture("value").<String>fallback(opt -> {
            opt.ifPresent(throwables::add);
            numbers.add(0);
            return "fallback";
        }));
        assertTrue(throwables.isEmpty());
        assertFalse(numbers.contains(0));

        SharedTestUtils.assertSucceedWith("fallback", Future.succeededFuture().<String>fallback(opt -> {
            opt.ifPresent(throwables::add);
            numbers.add(1);
            return "fallback";
        }));
        assertTrue(throwables.isEmpty());
        assertTrue(numbers.contains(1));

        SharedTestUtils.assertSucceedWith("fallback", Future.failedFuture("error").<String>fallback(opt -> {
            opt.ifPresent(throwables::add);
            numbers.add(2);
            return "fallback";
        }));
        assertFalse(throwables.isEmpty());
        assertTrue(numbers.contains(2));
    }

    @Test
    void fallbackWith_mapper_supplier() {
        val throwables = new ArrayList<Throwable>();
        val numbers = new HashSet<Integer>();

        SharedTestUtils.assertSucceedWith("value", Future.succeededFuture("value").fallback(t -> {
            throwables.add(t);
            return "otherwise";
        }, () -> {
            numbers.add(0);
            return "default";
        }));
        assertTrue(throwables.isEmpty());
        assertFalse(numbers.contains(0));

        SharedTestUtils.assertSucceedWith("default", Future.succeededFuture().fallback(t -> {
            throwables.add(t);
            return "otherwise";
        }, () -> {
            numbers.add(1);
            return "default";
        }));
        assertTrue(throwables.isEmpty());
        assertTrue(numbers.contains(1));

        SharedTestUtils.assertSucceedWith("otherwise", Future.failedFuture("error").fallback(t -> {
            throwables.add(t);
            return "otherwise";
        }, () -> {
            numbers.add(2);
            return "default";
        }));
        assertFalse(throwables.isEmpty());
        assertFalse(numbers.contains(2));
    }

    @Test
    void flatFallbackWith_function() {
        val throwables = new ArrayList<Throwable>();
        val numbers = new HashSet<Integer>();

        SharedTestUtils.assertSucceedWith("value", Future.succeededFuture("value").flatFallback(opt -> {
            opt.ifPresent(throwables::add);
            numbers.add(0);
            return Future.succeededFuture("fallback");
        }));
        assertTrue(throwables.isEmpty());
        assertFalse(numbers.contains(0));

        SharedTestUtils.assertSucceedWith("value", Future.succeededFuture("value").flatFallback(opt -> {
            opt.ifPresent(throwables::add);
            numbers.add(1);
            return Future.failedFuture("new failure");
        }));
        assertTrue(throwables.isEmpty());
        assertFalse(numbers.contains(1));

        SharedTestUtils.assertSucceedWith("fallback", Future.succeededFuture().flatFallback(opt -> {
            opt.ifPresent(throwables::add);
            numbers.add(2);
            return Future.succeededFuture("fallback");
        }));
        assertTrue(throwables.isEmpty());
        assertTrue(numbers.contains(2));

        SharedTestUtils.assertFailedWith("new failure", Future.succeededFuture().flatFallback(opt -> {
            opt.ifPresent(throwables::add);
            numbers.add(3);
            return Future.failedFuture("new failure");
        }));
        assertTrue(throwables.isEmpty());
        assertTrue(numbers.contains(3));

        SharedTestUtils.assertSucceedWith("fallback", Future.failedFuture("error").flatFallback(opt -> {
            opt.ifPresent(throwables::add);
            numbers.add(4);
            return Future.succeededFuture("fallback");
        }));
        assertFalse(throwables.isEmpty());
        assertTrue(numbers.contains(4));

        throwables.clear();
        SharedTestUtils.assertFailedWith("new failure", Future.failedFuture("error").flatFallback(opt -> {
            opt.ifPresent(throwables::add);
            numbers.add(5);
            return Future.failedFuture("new failure");
        }));
        assertFalse(throwables.isEmpty());
        assertTrue(numbers.contains(5));
    }

    @Test
    @SuppressWarnings("java:S5961")
    void flatFallbackWith_mapper_supplier() {
        val throwables = new ArrayList<Throwable>();
        val numbers = new HashSet<Integer>();

        SharedTestUtils.assertSucceedWith("value", Future.succeededFuture("value").flatFallback(t -> {
            throwables.add(t);

            return Future.succeededFuture("otherwise");
        }, () -> {
            numbers.add(0);
            return Future.succeededFuture("default");
        }));
        assertTrue(throwables.isEmpty());
        assertFalse(numbers.contains(0));

        SharedTestUtils.assertSucceedWith("value", Future.succeededFuture("value").flatFallback(t -> {
            throwables.add(t);
            return Future.failedFuture("otherwise");
        }, () -> {
            numbers.add(1);
            return Future.failedFuture("default");
        }));
        assertTrue(throwables.isEmpty());
        assertFalse(numbers.contains(1));

        SharedTestUtils.assertSucceedWith("default", Future.succeededFuture().flatFallback(t -> {
            throwables.add(t);
            return Future.succeededFuture("otherwise");
        }, () -> {
            numbers.add(2);
            return Future.succeededFuture("default");
        }));
        assertTrue(throwables.isEmpty());
        assertTrue(numbers.contains(2));

        SharedTestUtils.assertFailedWith("default", Future.succeededFuture().flatFallback(t -> {
            throwables.add(t);
            return Future.failedFuture("otherwise");
        }, () -> {
            numbers.add(3);
            return Future.failedFuture("default");
        }));
        assertTrue(throwables.isEmpty());
        assertTrue(numbers.contains(3));

        SharedTestUtils.assertSucceedWith("otherwise", Future.failedFuture("error").flatFallback(t -> {
            throwables.add(t);
            return Future.succeededFuture("otherwise");
        }, () -> {
            numbers.add(4);
            return Future.succeededFuture("default");
        }));
        assertFalse(throwables.isEmpty());
        assertFalse(numbers.contains(4));

        SharedTestUtils.assertFailedWith("otherwise", Future.failedFuture("error").flatFallback(t -> {
            throwables.add(t);
            return Future.failedFuture("otherwise");
        }, () -> {
            numbers.add(5);
            return Future.failedFuture("default");
        }));
        assertFalse(throwables.isEmpty());
        assertFalse(numbers.contains(5));
    }

    @Test
    void nonEmpty() {
        SharedTestUtils.assertSucceedWith("value", Future.succeededFuture("value").nonEmpty());
        SharedTestUtils.assertFailedWith(NullPointerException.class, Future.succeededFuture().nonEmpty());
        SharedTestUtils.assertFailedWith("error", Future.failedFuture("error").nonEmpty());
    }

    @Test
    void mapSome() {
        val numbers = new HashSet<Integer>();

        SharedTestUtils.assertSucceedWith(5, Future.succeededFuture("value").mapSome(v -> {
            numbers.add(0);
            return v.length();
        }));
        assertTrue(numbers.contains(0));

        SharedTestUtils.assertSucceedWith(null, Future.<String>succeededFuture().mapSome(v -> {
            numbers.add(1);
            return v.length();
        }));
        assertFalse(numbers.contains(1));

        SharedTestUtils.assertFailedWith("error", Future.<String>failedFuture("error").mapSome(v -> {
            numbers.add(2);
            return v.length();
        }));
        assertFalse(numbers.contains(2));
    }

    @Test
    void flatMapSome() {
        val numbers = new HashSet<Integer>();

        SharedTestUtils.assertSucceedWith(5, Future.succeededFuture("value").flatMapSome(v -> {
            numbers.add(0);
            return Future.succeededFuture(v.length());
        }));
        assertTrue(numbers.contains(0));

        SharedTestUtils.assertFailedWith("new failure", Future.succeededFuture("value").flatMapSome(v -> {
            numbers.add(1);
            return Future.failedFuture("new failure");
        }));
        assertTrue(numbers.contains(1));

        SharedTestUtils.assertSucceedWith(null, Future.<String>succeededFuture().flatMapSome(v -> {
            numbers.add(2);
            return Future.succeededFuture(v.length());
        }));
        assertFalse(numbers.contains(2));

        SharedTestUtils.assertSucceedWith(null, Future.<String>succeededFuture().flatMapSome(v -> {
            numbers.add(3);
            return Future.failedFuture("new failure");
        }));
        assertFalse(numbers.contains(3));

        SharedTestUtils.assertFailedWith("error", Future.<String>failedFuture("error").flatMapSome(v -> {
            numbers.add(4);
            return Future.succeededFuture(v.length());
        }));
        assertFalse(numbers.contains(4));

        SharedTestUtils.assertFailedWith("error", Future.<String>failedFuture("error").flatMapSome(v -> {
            numbers.add(5);
            return Future.failedFuture("new failure");


        }));
        assertFalse(numbers.contains(5));
    }

    @Test
    void wrap() {
        SharedTestUtils.assertSucceedWith(1, FutureUtils.wrap(() -> Integer.parseInt("1")));
        assertFailedWith(NumberFormatException.class, "For input string: \"@\"",
                FutureUtils.wrap(() -> Integer.parseInt("@"))
        );
    }

    @Test
    void wrap_function() {
        SharedTestUtils.assertSucceedWith(1, FutureUtils.wrap("1", Integer::parseInt));
        assertFailedWith(
                NumberFormatException.class, "For input string: \"@\"",
                FutureUtils.wrap("@", Integer::parseInt)
        );
    }

    @Test
    void joinWrap_flatWrap() {
        Future<Integer> future0 = FutureUtils.wrap("0", Integer::parseInt);
        Future<Integer> future1 = FutureUtils.wrap("1", Integer::parseInt);

        assertFailedWith(
                ArithmeticException.class, "/ by zero",
                FutureUtils.joinWrap(() -> future0.map(i -> 2 / i))
        );
        assertFailedWith(
                ArithmeticException.class, "/ by zero",
                FutureUtils.flatWrap(() -> future0.map(i -> 2 / i))
        );

        SharedTestUtils.assertSucceedWith(2, FutureUtils.joinWrap(() -> future1.map(i -> 2 / i)));
        SharedTestUtils.assertSucceedWith(2, FutureUtils.flatWrap(() -> future1.map(i -> 2 / i)));

        //noinspection ConstantConditions
        SharedTestUtils.assertFailedWith(
                NullPointerException.class,
                FutureUtils.joinWrap(() -> ((Future<Integer>) null).map(i -> 2 / i))
        );
        //noinspection ConstantConditions
        SharedTestUtils.assertFailedWith(
                NullPointerException.class,
                FutureUtils.flatWrap(() -> ((Future<Integer>) null).map(i -> 2 / i))
        );
    }

    @Test
    void joinWrap_flatWrap_function() {
        Function<String, Future<Integer>> stringToIntFuture = s -> FutureUtils.wrap(s, Integer::parseInt);

        SharedTestUtils.assertSucceedWith(1, FutureUtils.joinWrap("1", stringToIntFuture));
        SharedTestUtils.assertSucceedWith(1, FutureUtils.flatWrap("1", stringToIntFuture));

        assertFailedWith(
                NumberFormatException.class, "For input string: \"!\"",
                FutureUtils.joinWrap("!", stringToIntFuture)
        );
        assertFailedWith(
                NumberFormatException.class, "For input string: \"!\"",
                FutureUtils.flatWrap("!", stringToIntFuture)
        );

        assertFailedWith(NumberFormatException.class, "null", FutureUtils.joinWrap(null, stringToIntFuture));
        assertFailedWith(NumberFormatException.class, "null", FutureUtils.flatWrap(null, stringToIntFuture));
    }

    @SuppressWarnings("java:S2925")
    @SneakyThrows
    private static void delayParseInt(String s, Handler<AsyncResult<Integer>> handler) {
        Thread.sleep(1_000);
        handler.handle(FutureUtils.wrap(s, Integer::parseInt));
    }

    private static <T, E> void assertFailedWith(Class<E> clazz, String expectedMessage, Future<T> actual) {
        assertTrue(actual.failed());
        assertTrue(clazz.isInstance(actual.cause()));
        assertEquals(expectedMessage, actual.cause().getMessage());
    }

    @Test
    void tuple2() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();

        val tuple = FutureUtils.tuple(future0, future1);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
    }

    @Test
    void tuple3() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();
        Future<Boolean> future2 = Future.succeededFuture(true);

        val tuple = FutureUtils.tuple(future0, future1, future2);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
        assertSame(future2, tuple.get_2());
    }

    @Test
    void tuple4() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);

        val tuple = FutureUtils.tuple(future0, future1, future2, future3);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
        assertSame(future2, tuple.get_2());
        assertSame(future3, tuple.get_3());
    }

    @Test
    void tuple5() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');

        val tuple = FutureUtils.tuple(future0, future1, future2, future3, future4);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
        assertSame(future2, tuple.get_2());
        assertSame(future3, tuple.get_3());
        assertSame(future4, tuple.get_4());
    }

    @Test
    void tuple6() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);

        val tuple = FutureUtils.tuple(future0, future1, future2, future3, future4, future5);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
        assertSame(future2, tuple.get_2());
        assertSame(future3, tuple.get_3());
        assertSame(future4, tuple.get_4());
        assertSame(future5, tuple.get_5());
    }

    @Test
    void tuple7() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Float> future6 = Future.succeededFuture(1f);

        val tuple = FutureUtils.tuple(future0, future1, future2, future3, future4, future5, future6);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
        assertSame(future2, tuple.get_2());
        assertSame(future3, tuple.get_3());
        assertSame(future4, tuple.get_4());
        assertSame(future5, tuple.get_5());
        assertSame(future6, tuple.get_6());
    }

    @Test
    void tuple8() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Short> future7 = Future.succeededFuture((short) 1);

        val tuple = FutureUtils.tuple(future0, future1, future2, future3, future4, future5, future6, future7);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
        assertSame(future2, tuple.get_2());
        assertSame(future3, tuple.get_3());
        assertSame(future4, tuple.get_4());
        assertSame(future5, tuple.get_5());
        assertSame(future6, tuple.get_6());
        assertSame(future7, tuple.get_7());
    }

    @Test
    void tuple9() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Short> future7 = Future.succeededFuture((short) 1);
        Future<Integer> future8 = Future.succeededFuture(1);

        val tuple = FutureUtils.tuple(future0, future1, future2, future3, future4, future5, future6, future7, future8);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
        assertSame(future2, tuple.get_2());
        assertSame(future3, tuple.get_3());
        assertSame(future4, tuple.get_4());
        assertSame(future5, tuple.get_5());
        assertSame(future6, tuple.get_6());
        assertSame(future7, tuple.get_7());
        assertSame(future8, tuple.get_8());
    }

    @Test
    void all2() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();

        val compositeA = FutureUtils.all(future0, future1);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.all(Future.<Double>failedFuture("fail"), future1);
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
    }

    @Test
    void all3() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);

        val compositeA = FutureUtils.all(future0, future1, future2);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.all(Future.<Double>failedFuture("fail"), future1, future2);
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
    }

    @Test
    void all4() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);

        val compositeA = FutureUtils.all(future0, future1, future2, future3);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());
        assertSame(future3, compositeA.tuple().get_3());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.all(Future.<Double>failedFuture("fail"), future1, future2, future3);
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
    }

    @Test
    void all5() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');

        val compositeA = FutureUtils.all(future0, future1, future2, future3, future4);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());
        assertSame(future3, compositeA.tuple().get_3());
        assertSame(future4, compositeA.tuple().get_4());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.all(Future.<Double>failedFuture("fail"), future1, future2, future3, future4);
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
    }

    @Test
    void all6() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);

        val compositeA = FutureUtils.all(future0, future1, future2, future3, future4, future5);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());
        assertSame(future3, compositeA.tuple().get_3());
        assertSame(future4, compositeA.tuple().get_4());
        assertSame(future5, compositeA.tuple().get_5());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.all(
                Future.<Double>failedFuture("fail"), future1, future2, future3, future4, future5
        );
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
    }

    @Test
    void all7() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Float> future6 = Future.succeededFuture(1f);

        val compositeA = FutureUtils.all(future0, future1, future2, future3, future4, future5, future6);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());
        assertSame(future3, compositeA.tuple().get_3());
        assertSame(future4, compositeA.tuple().get_4());
        assertSame(future5, compositeA.tuple().get_5());
        assertSame(future6, compositeA.tuple().get_6());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.all(
                Future.<Double>failedFuture("fail"), future1, future2, future3, future4, future5, future6
        );
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
    }

    @Test
    void all8() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Short> future7 = Future.succeededFuture((short) 1);

        val compositeA = FutureUtils.all(future0, future1, future2, future3, future4, future5, future6, future7);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());
        assertSame(future3, compositeA.tuple().get_3());
        assertSame(future4, compositeA.tuple().get_4());
        assertSame(future5, compositeA.tuple().get_5());
        assertSame(future6, compositeA.tuple().get_6());
        assertSame(future7, compositeA.tuple().get_7());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.all(
                Future.<Double>failedFuture("fail"), future1, future2, future3, future4, future5, future6, future7
        );
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
    }

    @Test
    void all9() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Short> future7 = Future.succeededFuture((short) 1);
        Future<Integer> future8 = Future.succeededFuture(1);

        val compositeA = FutureUtils.all(
                future0, future1, future2, future3, future4, future5, future6, future7, future8
        );
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());
        assertSame(future3, compositeA.tuple().get_3());
        assertSame(future4, compositeA.tuple().get_4());
        assertSame(future5, compositeA.tuple().get_5());
        assertSame(future6, compositeA.tuple().get_6());
        assertSame(future7, compositeA.tuple().get_7());
        assertSame(future8, compositeA.tuple().get_8());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.all(
                Future.<Double>failedFuture("fail"), future1, future2, future3, future4, future5, future6, future7,
                future8
        );
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
    }

    @Test
    void any2() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();

        val compositeA = FutureUtils.any(future0, future1);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());

        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());

        val compositeB = FutureUtils.any(Future.<Double>failedFuture("fail0"), future1);
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.fail("fail1");
        SharedTestUtils.assertFailedWith("fail1", compositeB.raw());

        val compositeC = FutureUtils.any(future0, Future.<Double>failedFuture("failC1"));
        SharedTestUtils.assertSucceedWith(compositeC.raw(), compositeC.raw());

        val compositeD = FutureUtils.any(Future.failedFuture("failD0"), Future.<Double>failedFuture("failD1"));
        SharedTestUtils.assertFailedWith("failD1", compositeD.raw());
    }

    @Test
    void any3() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> failedFuture2 = Future.failedFuture("fail2");

        val compositeA = FutureUtils.any(future0, future1, failedFuture2);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(failedFuture2, compositeA.tuple().get_2());

        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());

        val compositeB = FutureUtils.any(Future.<Double>failedFuture("fail0"), future1, failedFuture2);
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.fail("fail1");
        SharedTestUtils.assertFailedWith("fail1", compositeB.raw());

        val compositeC = FutureUtils.any(future0, Future.<Double>failedFuture("failC1"), failedFuture2);
        SharedTestUtils.assertSucceedWith(compositeC.raw(), compositeC.raw());

        val compositeD = FutureUtils.any(
                Future.failedFuture("failD0"), Future.<Double>failedFuture("failD1"), failedFuture2
        );
        SharedTestUtils.assertFailedWith("fail2", compositeD.raw());
    }

    @Test
    void any4() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> failedFuture2 = Future.failedFuture("fail2");
        Future<Double> failedFuture3 = Future.failedFuture("fail3");

        val compositeA = FutureUtils.any(future0, future1, failedFuture2, failedFuture3);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(failedFuture2, compositeA.tuple().get_2());
        assertSame(failedFuture3, compositeA.tuple().get_3());

        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());

        val compositeB = FutureUtils.any(Future.<Double>failedFuture("fail0"), future1, failedFuture2, failedFuture3);
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.fail("fail1");
        SharedTestUtils.assertFailedWith("fail1", compositeB.raw());

        val compositeC = FutureUtils.any(future0, Future.<Double>failedFuture("failC1"), failedFuture2, failedFuture3);
        SharedTestUtils.assertSucceedWith(compositeC.raw(), compositeC.raw());

        val compositeD = FutureUtils.any(
                Future.failedFuture("failD0"), Future.<Double>failedFuture("failD1"), failedFuture2, failedFuture3
        );
        SharedTestUtils.assertFailedWith("fail3", compositeD.raw());
    }

    @Test
    void any5() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> failedFuture2 = Future.failedFuture("fail2");
        Future<Double> failedFuture3 = Future.failedFuture("fail3");
        Future<Character> failedFuture4 = Future.failedFuture("fail4");

        val compositeA = FutureUtils.any(future0, future1, failedFuture2, failedFuture3, failedFuture4);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(failedFuture2, compositeA.tuple().get_2());
        assertSame(failedFuture3, compositeA.tuple().get_3());
        assertSame(failedFuture4, compositeA.tuple().get_4());

        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());

        val compositeB = FutureUtils.any(
                Future.<Double>failedFuture("fail0"), future1, failedFuture2, failedFuture3, failedFuture4
        );
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.fail("fail1");
        SharedTestUtils.assertFailedWith("fail1", compositeB.raw());

        val compositeC = FutureUtils.any(
                future0, Future.<Double>failedFuture("failC1"), failedFuture2, failedFuture3, failedFuture4
        );
        SharedTestUtils.assertSucceedWith(compositeC.raw(), compositeC.raw());

        val compositeD = FutureUtils.any(
                Future.failedFuture("failD0"), Future.<Double>failedFuture("failD1"), failedFuture2, failedFuture3,
                failedFuture4
        );
        SharedTestUtils.assertFailedWith("fail4", compositeD.raw());
    }

    @Test
    void any6() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> failedFuture2 = Future.failedFuture("fail2");
        Future<Double> failedFuture3 = Future.failedFuture("fail3");
        Future<Character> failedFuture4 = Future.failedFuture("fail4");
        Future<Byte> failedFuture5 = Future.failedFuture("fail5");

        val compositeA = FutureUtils.any(future0, future1, failedFuture2, failedFuture3, failedFuture4, failedFuture5);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(failedFuture2, compositeA.tuple().get_2());
        assertSame(failedFuture3, compositeA.tuple().get_3());
        assertSame(failedFuture4, compositeA.tuple().get_4());
        assertSame(failedFuture5, compositeA.tuple().get_5());

        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());

        val compositeB = FutureUtils.any(
                Future.<Double>failedFuture("fail0"), future1, failedFuture2, failedFuture3, failedFuture4,
                failedFuture5
        );
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.fail("fail1");
        SharedTestUtils.assertFailedWith("fail1", compositeB.raw());

        val compositeC = FutureUtils.any(
                future0, Future.<Double>failedFuture("failC1"), failedFuture2, failedFuture3, failedFuture4,
                failedFuture5
        );
        SharedTestUtils.assertSucceedWith(compositeC.raw(), compositeC.raw());

        val compositeD = FutureUtils.any(
                Future.failedFuture("failD0"), Future.<Double>failedFuture("failD1"), failedFuture2, failedFuture3,
                failedFuture4, failedFuture5
        );
        SharedTestUtils.assertFailedWith("fail5", compositeD.raw());
    }

    @Test
    void any7() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> failedFuture2 = Future.failedFuture("fail2");
        Future<Double> failedFuture3 = Future.failedFuture("fail3");
        Future<Character> failedFuture4 = Future.failedFuture("fail4");
        Future<Byte> failedFuture5 = Future.failedFuture("fail5");
        Future<Float> failedFuture6 = Future.failedFuture("fail6");

        val compositeA = FutureUtils.any(
                future0, future1, failedFuture2, failedFuture3, failedFuture4, failedFuture5, failedFuture6
        );
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(failedFuture2, compositeA.tuple().get_2());
        assertSame(failedFuture3, compositeA.tuple().get_3());
        assertSame(failedFuture4, compositeA.tuple().get_4());
        assertSame(failedFuture5, compositeA.tuple().get_5());
        assertSame(failedFuture6, compositeA.tuple().get_6());

        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());

        val compositeB = FutureUtils.any(
                Future.<Double>failedFuture("fail0"), future1, failedFuture2, failedFuture3, failedFuture4,
                failedFuture5, failedFuture6
        );
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.fail("fail1");
        SharedTestUtils.assertFailedWith("fail1", compositeB.raw());

        val compositeC = FutureUtils.any(
                future0, Future.<Double>failedFuture("failC1"), failedFuture2, failedFuture3, failedFuture4,
                failedFuture5, failedFuture6
        );
        SharedTestUtils.assertSucceedWith(compositeC.raw(), compositeC.raw());

        val compositeD = FutureUtils.any(
                Future.failedFuture("failD0"), Future.<Double>failedFuture("failD1"), failedFuture2, failedFuture3,
                failedFuture4, failedFuture5, failedFuture6
        );
        SharedTestUtils.assertFailedWith("fail6", compositeD.raw());
    }

    @Test
    @SuppressWarnings("java:S5961")
    void any8() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> failedFuture2 = Future.failedFuture("fail2");
        Future<Double> failedFuture3 = Future.failedFuture("fail3");
        Future<Character> failedFuture4 = Future.failedFuture("fail4");
        Future<Byte> failedFuture5 = Future.failedFuture("fail5");
        Future<Float> failedFuture6 = Future.failedFuture("fail6");
        Future<Short> failedFuture7 = Future.failedFuture("fail7");

        val compositeA = FutureUtils.any(
                future0, future1, failedFuture2, failedFuture3, failedFuture4, failedFuture5, failedFuture6,
                failedFuture7
        );
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(failedFuture2, compositeA.tuple().get_2());
        assertSame(failedFuture3, compositeA.tuple().get_3());
        assertSame(failedFuture4, compositeA.tuple().get_4());
        assertSame(failedFuture5, compositeA.tuple().get_5());
        assertSame(failedFuture6, compositeA.tuple().get_6());
        assertSame(failedFuture7, compositeA.tuple().get_7());

        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());

        val compositeB = FutureUtils.any(
                Future.<Double>failedFuture("fail0"), future1, failedFuture2, failedFuture3, failedFuture4,
                failedFuture5, failedFuture6, failedFuture7
        );
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.fail("fail1");
        SharedTestUtils.assertFailedWith("fail1", compositeB.raw());

        val compositeC = FutureUtils.any(
                future0, Future.<Double>failedFuture("failC1"), failedFuture2, failedFuture3, failedFuture4,
                failedFuture5, failedFuture6, failedFuture7
        );
        SharedTestUtils.assertSucceedWith(compositeC.raw(), compositeC.raw());

        val compositeD = FutureUtils.any(
                Future.failedFuture("failD0"), Future.<Double>failedFuture("failD1"), failedFuture2, failedFuture3,
                failedFuture4, failedFuture5, failedFuture6, failedFuture7
        );
        SharedTestUtils.assertFailedWith("fail7", compositeD.raw());
    }

    @Test
    @SuppressWarnings("java:S5961")
    void any9() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> failedFuture2 = Future.failedFuture("fail2");
        Future<Double> failedFuture3 = Future.failedFuture("fail3");
        Future<Character> failedFuture4 = Future.failedFuture("fail4");
        Future<Byte> failedFuture5 = Future.failedFuture("fail5");
        Future<Float> failedFuture6 = Future.failedFuture("fail6");
        Future<Short> failedFuture7 = Future.failedFuture("fail7");
        Future<Integer> failedFuture8 = Future.failedFuture("fail8");

        val compositeA = FutureUtils.any(
                future0, future1, failedFuture2, failedFuture3, failedFuture4, failedFuture5, failedFuture6,
                failedFuture7, failedFuture8
        );
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(failedFuture2, compositeA.tuple().get_2());
        assertSame(failedFuture3, compositeA.tuple().get_3());
        assertSame(failedFuture4, compositeA.tuple().get_4());
        assertSame(failedFuture5, compositeA.tuple().get_5());
        assertSame(failedFuture6, compositeA.tuple().get_6());
        assertSame(failedFuture7, compositeA.tuple().get_7());
        assertSame(failedFuture8, compositeA.tuple().get_8());

        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());

        val compositeB = FutureUtils.any(
                Future.<Double>failedFuture("fail0"), future1, failedFuture2, failedFuture3, failedFuture4,
                failedFuture5, failedFuture6, failedFuture7, failedFuture8
        );
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.fail("fail1");
        SharedTestUtils.assertFailedWith("fail1", compositeB.raw());

        val compositeC = FutureUtils.any(
                future0, Future.<Double>failedFuture("failC1"), failedFuture2, failedFuture3, failedFuture4,
                failedFuture5, failedFuture6, failedFuture7, failedFuture8
        );
        SharedTestUtils.assertSucceedWith(compositeC.raw(), compositeC.raw());

        val compositeD = FutureUtils.any(
                Future.failedFuture("failD0"), Future.<Double>failedFuture("failD1"), failedFuture2, failedFuture3,
                failedFuture4, failedFuture5, failedFuture6, failedFuture7, failedFuture8
        );
        SharedTestUtils.assertFailedWith("fail8", compositeD.raw());
    }

    @Test
    void join2() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();

        val compositeA = FutureUtils.join(future0, future1);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.join(Future.<Double>failedFuture("fail"), future1);
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());
    }

    @Test
    void join3() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);

        val compositeA = FutureUtils.join(future0, future1, future2);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.join(Future.<Double>failedFuture("fail"), future1, future2);
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());
    }

    @Test
    void join4() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);

        val compositeA = FutureUtils.join(future0, future1, future2, future3);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());
        assertSame(future3, compositeA.tuple().get_3());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.join(Future.<Double>failedFuture("fail"), future1, future2, future3);
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());
    }

    @Test
    void join5() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');

        val compositeA = FutureUtils.join(future0, future1, future2, future3, future4);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());
        assertSame(future3, compositeA.tuple().get_3());
        assertSame(future4, compositeA.tuple().get_4());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.join(Future.<Double>failedFuture("fail"), future1, future2, future3, future4);
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());
    }

    @Test
    void join6() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);

        val compositeA = FutureUtils.join(future0, future1, future2, future3, future4, future5);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());
        assertSame(future3, compositeA.tuple().get_3());
        assertSame(future4, compositeA.tuple().get_4());
        assertSame(future5, compositeA.tuple().get_5());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.join(
                Future.<Double>failedFuture("fail"), future1, future2, future3, future4, future5
        );
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());
    }

    @Test
    void join7() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Float> future6 = Future.succeededFuture(1f);

        val compositeA = FutureUtils.join(future0, future1, future2, future3, future4, future5, future6);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());
        assertSame(future3, compositeA.tuple().get_3());
        assertSame(future4, compositeA.tuple().get_4());
        assertSame(future5, compositeA.tuple().get_5());
        assertSame(future6, compositeA.tuple().get_6());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.join(
                Future.<Double>failedFuture("fail"), future1, future2, future3, future4, future5, future6
        );
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());
    }

    @Test
    void join8() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Short> future7 = Future.succeededFuture((short) 1);

        val compositeA = FutureUtils.join(future0, future1, future2, future3, future4, future5, future6, future7);
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());
        assertSame(future3, compositeA.tuple().get_3());
        assertSame(future4, compositeA.tuple().get_4());
        assertSame(future5, compositeA.tuple().get_5());
        assertSame(future6, compositeA.tuple().get_6());
        assertSame(future7, compositeA.tuple().get_7());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.join(
                Future.<Double>failedFuture("fail"), future1, future2, future3, future4, future5, future6, future7
        );
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());
    }

    @Test
    void join9() {
        Future<String> future0 = Future.succeededFuture();
        Promise<Integer> promise1 = Promise.promise();
        Future<Integer> future1 = promise1.future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Short> future7 = Future.succeededFuture((short) 1);
        Future<Integer> future8 = Future.succeededFuture(1);

        val compositeA = FutureUtils.join(
                future0, future1, future2, future3, future4, future5, future6, future7, future8
        );
        assertSame(future0, compositeA.tuple().get_0());
        assertSame(future1, compositeA.tuple().get_1());
        assertSame(future2, compositeA.tuple().get_2());
        assertSame(future3, compositeA.tuple().get_3());
        assertSame(future4, compositeA.tuple().get_4());
        assertSame(future5, compositeA.tuple().get_5());
        assertSame(future6, compositeA.tuple().get_6());
        assertSame(future7, compositeA.tuple().get_7());
        assertSame(future8, compositeA.tuple().get_8());

        assertFalse(compositeA.raw().succeeded());
        assertFalse(compositeA.raw().failed());

        val compositeB = FutureUtils.join(
                Future.<Double>failedFuture("fail"), future1, future2, future3, future4, future5, future6, future7,
                future8
        );
        assertFalse(compositeB.raw().succeeded());
        assertFalse(compositeB.raw().failed());

        promise1.complete(1);
        SharedTestUtils.assertSucceedWith(compositeA.raw(), compositeA.raw());
        SharedTestUtils.assertFailedWith("fail", compositeB.raw());
    }
}
