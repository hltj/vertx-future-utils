package me.hltj.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
     * if a Future succeed with null, map it with the default value
     *
     * @param future   the Future
     * @param supplier a supplier to get the default value
     * @param <T>      the type parameter of the Future
     * @return the result Future
     */
    public static <T> Future<T> defaultWith(Future<T> future, Supplier<T> supplier) {
        return future.map(x -> x == null ? supplier.get() : x);
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

    /**
     * if a Future failed or succeed with null, replace it with a Future that succeed with the default value
     *
     * @param future   the Future
     * @param function a function to get the default value
     * @param <T>      the type parameter of the Future
     * @return the result Future
     */
    public static <T> Future<T> fallbackWith(Future<T> future, Function<Optional<Throwable>, T> function) {
        return fallbackWith(future, function.compose(Optional::of), () -> function.apply(Optional.empty()));
    }

    /**
     * if a Future failed or succeed with null, replace it with a Future that succeed with the default value
     *
     * @param future   the Future
     * @param mapper   a function to get the default value on failure
     * @param supplier a function to get the default value for replacing null
     * @param <T>      the type parameter of the Future
     * @return the result Future
     */
    public static <T> Future<T> fallbackWith(Future<T> future, Function<Throwable, T> mapper, Supplier<T> supplier) {
        return defaultWith(future.otherwise(mapper), supplier);
    }

    /**
     * wrap a evaluation result within Future
     *
     * @param supplier the evaluation
     * @param <R>      the result type of the evaluation
     * @return succeed Future for main scenario & failed Future if a non-checked Exception thrown
     */
    public static <R> Future<R> wrap(Supplier<R> supplier) {
        try {
            return Future.succeededFuture(supplier.get());
        } catch (Throwable t) {
            return Future.failedFuture(t);
        }
    }

    /**
     * wrap a function application result within Future
     *
     * @param v        a value
     * @param function a function applied to the value
     * @param <T>      the type of the value
     * @param <R>      the result type of the function
     * @return succeed Future for main scenario & failed Future if a non-checked Exception thrown
     */
    public static <T, R> Future<R> wrap(T v, Function<T, R> function) {
        return wrap(() -> function.apply(v));
    }
}
