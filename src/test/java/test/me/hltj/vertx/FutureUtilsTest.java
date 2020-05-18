package test.me.hltj.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import lombok.SneakyThrows;
import me.hltj.vertx.FutureUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class FutureUtilsTest {

    @SneakyThrows
    @Test
    void futurize() {
        Future<Integer> future0 = FutureUtils.<Integer>futurize(x -> delayParseInt("1", x))
                .map(i -> i + 1);

        Future<Integer> future1 = FutureUtils.<Integer>futurize(x -> delayParseInt("%", x))
                .map(i -> i + 1);

        CountDownLatch latch = new CountDownLatch(1);
        CompositeFuture.join(future0, future1).onComplete(future -> {
            assertSucceedWith(2, future0);
            assertFailedWith(NumberFormatException.class, "For input string: \"%\"", future1);
            latch.countDown();
        });

        latch.await();
    }

    @SneakyThrows
    void delayParseInt(String s, Handler<AsyncResult<Integer>> handler) {
        Thread.sleep(1_000);

        try {
            int i = Integer.parseInt(s);
            handler.handle(Future.succeededFuture(i));
        } catch (NumberFormatException e) {
            handler.handle(Future.failedFuture(e));
        }
    }

    @Test
    void defaultWith() {
        assertSucceedWith("value", FutureUtils.defaultWith(Future.succeededFuture("value"), "default"));
        assertSucceedWith("default", FutureUtils.defaultWith(Future.succeededFuture(), "default"));
        assertFailedWith("error", FutureUtils.defaultWith(Future.failedFuture("error"), "default"));
    }

    @Test
    void defaultWith_supplier() {
        Set<Integer> numbers = new HashSet<>();

        assertSucceedWith("value", FutureUtils.<String>defaultWith(Future.succeededFuture("value"), () -> {
            numbers.add(0);
            return "default";
        }));
        assertFalse(numbers.contains(0));

        assertSucceedWith("default", FutureUtils.<String>defaultWith(Future.succeededFuture(), () -> {
            numbers.add(1);
            return "default";
        }));
        assertTrue(numbers.contains(1));

        assertFailedWith("error", FutureUtils.<String>defaultWith(Future.failedFuture("error"), () -> {
            numbers.add(2);
            return "default";
        }));
        assertFalse(numbers.contains(2));
    }

    @Test
    void fallbackWith() {
        assertSucceedWith("value", FutureUtils.fallbackWith(Future.succeededFuture("value"), "fallback"));
        assertSucceedWith("fallback", FutureUtils.fallbackWith(Future.succeededFuture(), "fallback"));
        assertSucceedWith("fallback", FutureUtils.fallbackWith(Future.failedFuture("error"), "fallback"));
    }

    @Test
    void fallbackWith_function() {
        List<Throwable> throwables = new ArrayList<>();
        Set<Integer> numbers = new HashSet<>();

        assertSucceedWith("value", FutureUtils.<String>fallbackWith(Future.succeededFuture("value"), opt -> {
            opt.ifPresent(throwables::add);
            numbers.add(0);
            return "fallback";
        }));
        assertTrue(throwables.isEmpty());
        assertFalse(numbers.contains(0));

        assertSucceedWith("fallback", FutureUtils.<String>fallbackWith(Future.succeededFuture(), opt -> {
            opt.ifPresent(throwables::add);
            numbers.add(1);
            return "fallback";
        }));
        assertTrue(throwables.isEmpty());
        assertTrue(numbers.contains(1));

        assertSucceedWith("fallback", FutureUtils.<String>fallbackWith(Future.failedFuture("error"), opt -> {
            opt.ifPresent(throwables::add);
            numbers.add(2);
            return "fallback";
        }));
        assertFalse(throwables.isEmpty());
        assertTrue(numbers.contains(2));
    }

    @Test
    void fallbackWith_mapper_supplier() {
        List<Throwable> throwables = new ArrayList<>();
        Set<Integer> numbers = new HashSet<>();

        assertSucceedWith("value", FutureUtils.fallbackWith(Future.succeededFuture("value"), t -> {
            throwables.add(t);
            return "otherwise";
        }, () -> {
            numbers.add(0);
            return "default";
        }));
        assertTrue(throwables.isEmpty());
        assertFalse(numbers.contains(0));

        assertSucceedWith("default", FutureUtils.fallbackWith(Future.succeededFuture(), t -> {
            throwables.add(t);
            return "otherwise";
        }, () -> {
            numbers.add(1);
            return "default";
        }));
        assertTrue(throwables.isEmpty());
        assertTrue(numbers.contains(1));

        assertSucceedWith("otherwise", FutureUtils.fallbackWith(Future.failedFuture("error"), t -> {
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
    void wrap() {
        assertSucceedWith(1, FutureUtils.wrap(() -> Integer.parseInt("1")));
        assertFailedWith(NumberFormatException.class, "For input string: \"@\"",
                FutureUtils.wrap(() -> Integer.parseInt("@"))
        );
    }

    @Test
    void wrap_function() {
        assertSucceedWith(1, FutureUtils.wrap("1", Integer::parseInt));
        assertFailedWith(
                NumberFormatException.class, "For input string: \"@\"",
                FutureUtils.wrap("@", Integer::parseInt)
        );
    }

    @Test
    void joinWrap() {
        Future<Integer> future0 = FutureUtils.wrap("0", Integer::parseInt);
        Future<Integer> future1 = FutureUtils.wrap("1", Integer::parseInt);

        assertFailedWith(
                ArithmeticException.class, "/ by zero",
                FutureUtils.joinWrap(() -> future0.map(i -> 2 / i))
        );
        assertSucceedWith(2, FutureUtils.joinWrap(() -> future1.map(i -> 2 / i)));
        //noinspection ConstantConditions
        assertFailedWith(
                NullPointerException.class,
                FutureUtils.joinWrap(() -> ((Future<Integer>) null).map(i -> 2 / i))
        );
    }

    @Test
    void joinWrap_function() {
        Function<String, Future<Integer>> stringToIntFuture = s -> FutureUtils.wrap(s, Integer::parseInt);
        assertSucceedWith(1, FutureUtils.joinWrap("1", stringToIntFuture));
        assertFailedWith(
                NumberFormatException.class, "For input string: \"!\"",
                FutureUtils.joinWrap("!", stringToIntFuture)
        );
        assertFailedWith(NumberFormatException.class, "null", FutureUtils.joinWrap(null, stringToIntFuture));
    }

    private <T> void assertSucceedWith(T expected, Future<T> actual) {
        assertTrue(actual.succeeded());
        assertEquals(expected, actual.result());
    }

    @SuppressWarnings("SameParameterValue")
    private <T> void assertFailedWith(String expectedMessage, Future<T> actual) {
        assertTrue(actual.failed());
        assertEquals(expectedMessage, actual.cause().getMessage());
    }

    @SuppressWarnings("SameParameterValue")
    private <T, E> void assertFailedWith(Class<E> clazz, Future<T> actual) {
        assertTrue(actual.failed());
        assertTrue(clazz.isInstance(actual.cause()));
    }

    private <T, E> void assertFailedWith(Class<E> clazz, String expectedMessage, Future<T> actual) {
        assertTrue(actual.failed());
        assertTrue(clazz.isInstance(actual.cause()));
        assertEquals(expectedMessage, actual.cause().getMessage());
    }
}
