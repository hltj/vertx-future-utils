package test.me.hltj.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import lombok.SneakyThrows;
import me.hltj.vertx.FutureUtils;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
            assertFailedWith("For input string: \"%\"", future1);
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

    @SuppressWarnings("SameParameterValue")
    private <T> void assertSucceedWith(T expected, Future<T> actual) {
        assertTrue(actual.succeeded());
        assertEquals(expected, actual.result());
    }

    @SuppressWarnings("SameParameterValue")
    private <T> void assertFailedWith(String expectedMessage, Future<T> actual) {
        assertTrue(actual.failed());
        assertEquals(expectedMessage, actual.cause().getMessage());
    }
}
