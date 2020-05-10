package test.me.hltj.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import lombok.SneakyThrows;
import me.hltj.vertx.FutureUtils;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FutureUtilsTest {

    @SneakyThrows
    @org.junit.jupiter.api.Test
    void futurize_simple() {
        Future<Integer> future1 = FutureUtils.<Integer>futurize(x -> delayParseInt("1", x))
                .map(i -> i + 1)
                .otherwise(0);

        Future<Integer> future2 = FutureUtils.<Integer>futurize(x -> delayParseInt("%", x))
                .map(i -> i + 1)
                .otherwise(0);

        CountDownLatch latch = new CountDownLatch(1);
        CompositeFuture.join(future1, future2).onComplete(future -> {
            assertEquals(2, future.result().<Integer>resultAt(0));
            assertEquals(0, future.result().<Integer>resultAt(1));
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
}
