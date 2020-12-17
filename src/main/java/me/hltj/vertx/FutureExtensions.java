package me.hltj.vertx;

import io.vertx.core.Future;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class FutureExtensions {

    private FutureExtensions() {
    }

    /**
     * Same as {@link FutureUtils#defaultWith(Future, Object)}.
     */
    public static <T> Future<T> defaults(Future<T> future, T v0) {
        return FutureUtils.defaultWith(future, v0);
    }

    /**
     * Same as {@link FutureUtils#defaultWith(Future, Supplier)}.
     */
    public static <T> Future<T> defaults(Future<T> future, Supplier<T> supplier) {
        return FutureUtils.defaultWith(future, supplier);
    }

    /**
     * Same as {@link FutureUtils#flatDefaultWith(Future, Supplier)}.
     */
    public static <T> Future<T> flatDefault(Future<T> future, Supplier<Future<T>> supplier) {
        return FutureUtils.flatDefaultWith(future, supplier);
    }

    /**
     * Same as {@link FutureUtils#fallbackWith(Future, Object)}.
     */
    public static <T> Future<T> fallback(Future<T> future, T v0) {
        return FutureUtils.fallbackWith(future, v0);
    }

    /**
     * Same as {@link FutureUtils#fallbackWith(Future, Function)}.
     */
    public static <T> Future<T> fallback(Future<T> future, Function<Optional<Throwable>, T> function) {
        return FutureUtils.fallbackWith(future, function);
    }

    /**
     * Same as {@link FutureUtils#fallbackWith(Future, Function, Supplier)}.
     */
    public static <T> Future<T> fallback(Future<T> future, Function<Throwable, T> mapper, Supplier<T> supplier) {
        return FutureUtils.fallbackWith(future, mapper, supplier);
    }

    /**
     * Same as {@link FutureUtils#flatFallbackWith(Future, Function)}.
     */
    public static <T> Future<T> flatFallback(Future<T> future, Function<Optional<Throwable>, Future<T>> function) {
        return FutureUtils.flatFallbackWith(future, function);
    }

    /**
     * Same as {@link FutureUtils#flatFallbackWith(Future, Function, Supplier)}.
     */
    public static <T> Future<T> flatFallback(
            Future<T> future, Function<Throwable, Future<T>> mapper, Supplier<Future<T>> supplier
    ) {
        return FutureUtils.flatFallbackWith(future, mapper, supplier);
    }

    /**
     * Same as {@link FutureUtils#nonEmpty(Future)}.
     */
    public static <T> Future<T> nonEmpty(Future<T> future) {
        return FutureUtils.nonEmpty(future);
    }

    /**
     * Same as {@link FutureUtils#mapSome(Future, Function)}.
     */
    public static <T, R> Future<R> mapSome(Future<T> future, Function<T, R> mapper) {
        return FutureUtils.mapSome(future, mapper);
    }

    /**
     * Same as {@link FutureUtils#flatMapSome(Future, Function)}.
     */
    public static <T, R> Future<R> flatMapSome(Future<T> future, Function<T, Future<R>> mapper) {
        return FutureUtils.flatMapSome(future, mapper);
    }
}
