package me.hltj.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;

import java.util.function.Consumer;

/**
 * Convenient Utils for Vert.x Future
 */
public class FutureUtils {

    /**
     * convert a callback style Vert.x call to Future result style
     *
     * @param consumer callback style Vert.x call
     * @param <T>      the type parameter of the AsyncResult
     * @return the Future
     */
    public static <T> Future<T> futurize(Consumer<Handler<AsyncResult<T>>> consumer) {
        Promise<T> promise = Promise.promise();
        consumer.accept(promise);
        return promise.future();
    }
}
