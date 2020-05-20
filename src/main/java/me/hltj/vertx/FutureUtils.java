/*
 * vertx-future-utils - Convenient Utilities for Vert.x Future
 * https://github.com/hltj/vertx-future-utils
 *
 * Copyright (C) 2020  JiaYanwei  https://hltj.me
 *
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Please contact me (jiaywe#at#gmail.com, replace the '#at#' with 'at')
 * if you need additional information or have any questions.
 */
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
 * Convenient Utilities for Vert.x {@link Future}.
 * <p>
 *
 * @author <a href="https://hltj.me">JiaYanwei</a>
 */
public final class FutureUtils {

    /**
     * Convert a callback style Vert.x call to {@link Future} result style.
     *
     * @param consumer callback style Vert.x call
     * @param <T>      the type parameter of the {@code AsyncResult}
     * @return the {@code Future}
     */
    public static <T> Future<T> futurize(Consumer<Handler<AsyncResult<T>>> consumer) {
        Promise<T> promise = Promise.promise();
        consumer.accept(promise);
        return promise.future();
    }

    /**
     * If a {@link Future} succeed with null, map it with the default value.
     *
     * @param future the {@code Future}
     * @param v0     the default value
     * @param <T>    the type parameter of the {@code Future}
     * @return the result {@code Future}
     */
    public static <T> Future<T> defaultWith(Future<T> future, T v0) {
        return future.map(x -> x == null ? v0 : x);
    }

    /**
     * If a {@link Future} succeed with null, map it with the default value.
     *
     * @param future   the {@code Future}
     * @param supplier a supplier to get the default value
     * @param <T>      the type parameter of the {@code Future}
     * @return the result {@code Future}
     */
    public static <T> Future<T> defaultWith(Future<T> future, Supplier<T> supplier) {
        return future.map(x -> x == null ? supplier.get() : x);
    }

    /**
     * If a {@link Future} failed or succeed with null,
     * replace it with a {@link Future} that succeed with the default value.
     *
     * @param future the {@code Future}
     * @param v0     the default value
     * @param <T>    the type parameter of the {@code Future}
     * @return the result {@code Future}
     */
    public static <T> Future<T> fallbackWith(Future<T> future, T v0) {
        return defaultWith(future.otherwise(v0), v0);
    }

    /**
     * If a {@link Future} failed or succeed with null,
     * replace it with a {@link Future} that succeed with the default value.
     *
     * @param future   the {@code Future}
     * @param function a function to get the default value
     * @param <T>      the type parameter of the {@code Future}
     * @return the result {@code Future}
     */
    public static <T> Future<T> fallbackWith(Future<T> future, Function<Optional<Throwable>, T> function) {
        return fallbackWith(future, function.compose(Optional::of), () -> function.apply(Optional.empty()));
    }

    /**
     * If a {@link Future} failed or succeed with null,
     * replace it with a {@link Future} that succeed with the default value.
     *
     * @param future   the {@code Future}
     * @param mapper   a function to get the default value on failure
     * @param supplier a function to get the default value for replacing null
     * @param <T>      the type parameter of the {@code Future}
     * @return the result {@code Future}
     */
    public static <T> Future<T> fallbackWith(Future<T> future, Function<Throwable, T> mapper, Supplier<T> supplier) {
        return defaultWith(future.otherwise(mapper), supplier);
    }

    /**
     * Wrap a evaluation result within {@link Future}.
     *
     * @param supplier the evaluation
     * @param <R>      the result type of the evaluation
     * @return succeed {@code Future} for main scenario & failed {@code Future} if a non-checked exception thrown
     */
    public static <R> Future<R> wrap(Supplier<R> supplier) {
        try {
            return Future.succeededFuture(supplier.get());
        } catch (Throwable t) {
            return Future.failedFuture(t);
        }
    }

    /**
     * Wrap a function application result within {@link Future}.
     *
     * @param v        a value
     * @param function a function applied to the value
     * @param <T>      the type of the value
     * @param <R>      the result type of the function
     * @return succeed {@code Future} for main scenario & failed {@code Future} if a non-checked exception thrown
     */
    public static <T, R> Future<R> wrap(T v, Function<T, R> function) {
        return wrap(() -> function.apply(v));
    }

    /**
     * Wrap a evaluation result within {@link Future}, where the evaluation itself return a {@link Future}.
     *
     * @param supplier the evaluation
     * @param <R>      the type parameter for the result {@code Future}
     * @return the evaluated {@code Future} for main scenario & failed {@code Future} if a non-checked exception thrown
     */
    public static <R> Future<R> joinWrap(Supplier<Future<R>> supplier) {
        try {
            return supplier.get();
        } catch (Throwable t) {
            return Future.failedFuture(t);
        }
    }

    /**
     * Wrap a function application result within {@link Future}, where the function itself return a {@link Future}.
     *
     * @param v        a value
     * @param function a function applied to the value
     * @param <T>      the type of the value
     * @param <R>      the type parameter for the result {@code Future}
     * @return the function returned {@code Future} for main scenario &
     * failed {@code Future} if a non-checked exception thrown
     */
    public static <T, R> Future<R> joinWrap(T v, Function<T, Future<R>> function) {
        return joinWrap(() -> function.apply(v));
    }
}
