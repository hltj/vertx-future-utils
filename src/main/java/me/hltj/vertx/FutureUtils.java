package me.hltj.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;

import java.util.function.Consumer;

/**
 * Convenient Utils for Vert.x Future
 */
public final class FutureUtils {

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

    /**
     * if a Future succeed with null, map it with the default value
     *
     * @param future the Future
     * @param v0     the default value
     * @param <T>    the type parameter of the Future
     * @return the result Future
     */
    public static <T> Future<T> defaultWith(Future<T> future, T v0) {
        return future.map(x -> x == null ? v0 : x);
    }

    /**
     * if a Future failed or succeed with null, replace it with a Future that succeed with the default value
     *
     * @param future the Future
     * @param v0     the default value
     * @param <T>    the type parameter of the Future
     * @return the result Future
     */
    public static <T> Future<T> fallbackWith(Future<T> future, T v0) {
        return defaultWith(future.otherwise(v0), v0);
    }
}
