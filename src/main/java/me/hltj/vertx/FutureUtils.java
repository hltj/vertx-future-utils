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
import me.hltj.vertx.future.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Convenient Utilities for Vert.x {@link Future}.
 * <p>
 *
 * @author <a href="https://hltj.me">JiaYanwei</a>
 * @since 1.0.0
 */
public final class FutureUtils {

    private FutureUtils() {
    }

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
     * If a {@link Future} succeed with null, replace it with the default {@link Future}.
     *
     * @param future   the {@code Future}
     * @param supplier a supplier to get the default {@code Future}
     * @param <T>      the type parameter of the {@code Future}
     * @return the result {@code Future}
     */
    public static <T> Future<T> flatDefaultWith(Future<T> future, Supplier<Future<T>> supplier) {
        return future.flatMap(x -> x == null ? supplier.get() : Future.succeededFuture(x));
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
     * If a {@link Future} failed or succeed with null,
     * replace it with a default {@link Future}.
     *
     * @param future   the {@code Future}
     * @param function a function to get the default {@code Future}
     * @param <T>      the type parameter of the {@code Future}
     * @return the result {@code Future}
     */
    public static <T> Future<T> flatFallbackWith(Future<T> future, Function<Optional<Throwable>, Future<T>> function) {
        return flatFallbackWith(future, function.compose(Optional::of), () -> function.apply(Optional.empty()));
    }

    /**
     * If a {@link Future} failed or succeed with null,
     * replace it with a default {@link Future}.
     *
     * @param future   the {@code Future}
     * @param mapper   a function to get the default {@code Future} on failure
     * @param supplier a function to get the default {@code Future} on success with null
     * @param <T>      the type parameter of the {@code Future}
     * @return the result {@code Future}
     */
    public static <T> Future<T> flatFallbackWith(Future<T> future, Function<Throwable, Future<T>> mapper, Supplier<Future<T>> supplier) {
        return flatDefaultWith(future.recover(mapper), supplier);
    }

    /**
     * Wraps an evaluation result within {@link Future}.
     *
     * @param supplier the evaluation
     * @param <R>      the result type of the evaluation
     * @return succeed {@code Future} for main scenario and failed {@code Future} if a non-checked exception thrown
     */
    public static <R> Future<R> wrap(Supplier<R> supplier) {
        try {
            return Future.succeededFuture(supplier.get());
        } catch (Throwable t) {
            return Future.failedFuture(t);
        }
    }

    /**
     * Wraps a {@code function} application result within {@link Future}.
     *
     * @param v        a value
     * @param function a function applied to the value
     * @param <T>      the type of the value
     * @param <R>      the result type of the function
     * @return succeed {@code Future} for main scenario and failed {@code Future} if a non-checked exception thrown
     */
    public static <T, R> Future<R> wrap(T v, Function<T, R> function) {
        return wrap(() -> function.apply(v));
    }

    /**
     * Alias for {@link FutureUtils#joinWrap(Supplier)}.
     */
    public static <R> Future<R> flatWrap(Supplier<Future<R>> supplier) {
        return joinWrap(supplier);
    }

    /**
     * Wraps an evaluation result within {@link Future}, where the evaluation result itself is a {@link Future},
     * the result will be join (also known as {@code flatten}) before return.
     *
     * @param supplier the evaluation
     * @param <R>      the type parameter for the result {@code Future}
     * @return the evaluated {@code Future} for main scenario and failed {@code Future} if a non-checked exception
     * thrown
     */
    public static <R> Future<R> joinWrap(Supplier<Future<R>> supplier) {
        try {
            return supplier.get();
        } catch (Throwable t) {
            return Future.failedFuture(t);
        }
    }

    /**
     * Alias for {@link FutureUtils#joinWrap(Object, Function)}.
     */
    public static <T, R> Future<R> flatWrap(T v, Function<T, Future<R>> function) {
        return joinWrap(v, function);
    }

    /**
     * Wraps a {@code function} application result within {@link Future}, where the {@code function} itself return
     * a {@link Future}, the result will be join (also known as {@code flatten}) before return.
     *
     * @param v        a value
     * @param function a function applied to the value
     * @param <T>      the type of the value
     * @param <R>      the type parameter for the result {@code Future}
     * @return the function returned {@code Future} for main scenario and failed {@code Future} if a non-checked
     * exception thrown
     */
    public static <T, R> Future<R> joinWrap(T v, Function<T, Future<R>> function) {
        return joinWrap(() -> function.apply(v));
    }

    /**
     * Create a future tuple with two {@link Future}s.
     */
    public static <T0, T1> FutureTuple2<T0, T1> tuple(Future<T0> future0, Future<T1> future1) {
        return FutureTuple2.of(future0, future1);
    }

    /**
     * Create a future tuple with 3 {@link Future}s.
     */
    public static <T0, T1, T2> FutureTuple3<T0, T1, T2> tuple(
            Future<T0> future0, Future<T1> future1, Future<T2> future2
    ) {
        return FutureTuple3.of(future0, future1, future2);
    }

    /**
     * Create a future tuple with 4 {@link Future}s.
     */
    public static <T0, T1, T2, T3> FutureTuple4<T0, T1, T2, T3> tuple(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3
    ) {
        return FutureTuple4.of(future0, future1, future2, future3);
    }

    /**
     * Create a future tuple with 5 {@link Future}s.
     */
    public static <T0, T1, T2, T3, T4> FutureTuple5<T0, T1, T2, T3, T4> tuple(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4
    ) {
        return FutureTuple5.of(future0, future1, future2, future3, future4);
    }

    /**
     * Create a future tuple with 6 {@link Future}s.
     */
    public static <T0, T1, T2, T3, T4, T5> FutureTuple6<T0, T1, T2, T3, T4, T5> tuple(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5
    ) {
        return FutureTuple6.of(future0, future1, future2, future3, future4, future5);
    }

    /**
     * Create a future tuple with 7 {@link Future}s.
     */
    public static <T0, T1, T2, T3, T4, T5, T6> FutureTuple7<T0, T1, T2, T3, T4, T5, T6> tuple(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5, Future<T6> future6
    ) {
        return FutureTuple7.of(future0, future1, future2, future3, future4, future5, future6);
    }

    /**
     * Create a future tuple with 8 {@link Future}s.
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7> FutureTuple8<T0, T1, T2, T3, T4, T5, T6, T7> tuple(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5, Future<T6> future6, Future<T7> future7
    ) {
        return FutureTuple8.of(future0, future1, future2, future3, future4, future5, future6, future7);
    }

    /**
     * Create a future tuple with 9 {@link Future}s.
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8> FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> tuple(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5, Future<T6> future6, Future<T7> future7, Future<T8> future8
    ) {
        return FutureTuple9.of(future0, future1, future2, future3, future4, future5, future6, future7, future8);
    }

    /**
     * Create a composite future tuple with two {@link Future}s and {@link CompositeFuture#all(Future, Future)}.
     */
    public static <T0, T1> CompositeFutureTuple2<T0, T1> all(Future<T0> future0, Future<T1> future1) {
        return FutureTuple2.of(future0, future1).all();
    }

    /**
     * Create a composite future tuple with 3 {@link Future}s and {@link CompositeFuture#all(Future, Future, Future)}.
     */
    public static <T0, T1, T2> CompositeFutureTuple3<T0, T1, T2> all(
            Future<T0> future0, Future<T1> future1, Future<T2> future2
    ) {
        return FutureTuple3.of(future0, future1, future2).all();
    }

    /**
     * Create a composite future tuple with 4 {@link Future}s
     * and {@link CompositeFuture#all(Future, Future, Future, Future)}.
     */
    public static <T0, T1, T2, T3> CompositeFutureTuple4<T0, T1, T2, T3> all(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3
    ) {
        return FutureTuple4.of(future0, future1, future2, future3).all();
    }

    /**
     * Create a composite future tuple with 5 {@link Future}s
     * and {@link CompositeFuture#all(Future, Future, Future, Future, Future)}.
     */
    public static <T0, T1, T2, T3, T4> CompositeFutureTuple5<T0, T1, T2, T3, T4> all(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4
    ) {
        return FutureTuple5.of(future0, future1, future2, future3, future4).all();
    }

    /**
     * Create a composite future tuple with 6 {@link Future}s
     * and {@link CompositeFuture#all(Future, Future, Future, Future, Future, Future)}.
     */
    public static <T0, T1, T2, T3, T4, T5> CompositeFutureTuple6<T0, T1, T2, T3, T4, T5> all(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5
    ) {
        return FutureTuple6.of(future0, future1, future2, future3, future4, future5).all();
    }

    /**
     * Create a composite future tuple with 7 {@link Future}s and {@link CompositeFuture#all(List)}.
     */
    public static <T0, T1, T2, T3, T4, T5, T6> CompositeFutureTuple7<T0, T1, T2, T3, T4, T5, T6> all(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5, Future<T6> future6
    ) {
        return FutureTuple7.of(future0, future1, future2, future3, future4, future5, future6).all();
    }

    /**
     * Create a composite future tuple with 8 {@link Future}s and {@link CompositeFuture#all(List)}.
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7> CompositeFutureTuple8<T0, T1, T2, T3, T4, T5, T6, T7> all(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5, Future<T6> future6, Future<T7> future7
    ) {
        return FutureTuple8.of(future0, future1, future2, future3, future4, future5, future6, future7).all();
    }

    /**
     * Create a composite future tuple with 9 {@link Future}s and {@link CompositeFuture#all(List)}.
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8> CompositeFutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> all(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5, Future<T6> future6, Future<T7> future7, Future<T8> future8
    ) {
        return FutureTuple9.of(future0, future1, future2, future3, future4, future5, future6, future7, future8).all();
    }

    /**
     * Create a composite future tuple with two {@link Future}s {@link CompositeFuture#any(Future, Future)}.
     */
    public static <T0, T1> CompositeFutureTuple2<T0, T1> any(Future<T0> future0, Future<T1> future1) {
        return FutureTuple2.of(future0, future1).any();
    }

    /**
     * Create a composite future tuple with 3 {@link Future}s and {@link CompositeFuture#any(Future, Future, Future)}.
     */
    public static <T0, T1, T2> CompositeFutureTuple3<T0, T1, T2> any(
            Future<T0> future0, Future<T1> future1, Future<T2> future2
    ) {
        return FutureTuple3.of(future0, future1, future2).any();
    }

    /**
     * Create a composite future tuple with 4 {@link Future}s
     * and {@link CompositeFuture#any(Future, Future, Future, Future)}.
     */
    public static <T0, T1, T2, T3> CompositeFutureTuple4<T0, T1, T2, T3> any(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3
    ) {
        return FutureTuple4.of(future0, future1, future2, future3).any();
    }

    /**
     * Create a composite future tuple with 5 {@link Future}s
     * and {@link CompositeFuture#any(Future, Future, Future, Future, Future)}.
     */
    public static <T0, T1, T2, T3, T4> CompositeFutureTuple5<T0, T1, T2, T3, T4> any(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4
    ) {
        return FutureTuple5.of(future0, future1, future2, future3, future4).any();
    }

    /**
     * Create a composite future tuple with 6 {@link Future}s
     * and {@link CompositeFuture#any(Future, Future, Future, Future, Future, Future)}.
     */
    public static <T0, T1, T2, T3, T4, T5> CompositeFutureTuple6<T0, T1, T2, T3, T4, T5> any(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5
    ) {
        return FutureTuple6.of(future0, future1, future2, future3, future4, future5).any();
    }

    /**
     * Create a composite future tuple with 7 {@link Future}s and {@link CompositeFuture#any(List)}.
     */
    public static <T0, T1, T2, T3, T4, T5, T6> CompositeFutureTuple7<T0, T1, T2, T3, T4, T5, T6> any(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5, Future<T6> future6
    ) {
        return FutureTuple7.of(future0, future1, future2, future3, future4, future5, future6).any();
    }

    /**
     * Create a composite future tuple with 8 {@link Future}s and {@link CompositeFuture#any(List)}.
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7> CompositeFutureTuple8<T0, T1, T2, T3, T4, T5, T6, T7> any(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5, Future<T6> future6, Future<T7> future7
    ) {
        return FutureTuple8.of(future0, future1, future2, future3, future4, future5, future6, future7).any();
    }

    /**
     * Create a composite future tuple with 9 {@link Future}s and {@link CompositeFuture#any(List)}.
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8> CompositeFutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> any(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5, Future<T6> future6, Future<T7> future7, Future<T8> future8
    ) {
        return FutureTuple9.of(future0, future1, future2, future3, future4, future5, future6, future7, future8).any();
    }

    /**
     * Create a composite future tuple with two {@link Future}s and {@link CompositeFuture#join(Future, Future)}.
     */
    public static <T0, T1> CompositeFutureTuple2<T0, T1> join(Future<T0> future0, Future<T1> future1) {
        return FutureTuple2.of(future0, future1).join();
    }

    /**
     * Create a composite future tuple with 3 {@link Future}s and {@link CompositeFuture#join(Future, Future, Future)}.
     */
    public static <T0, T1, T2> CompositeFutureTuple3<T0, T1, T2> join(
            Future<T0> future0, Future<T1> future1, Future<T2> future2
    ) {
        return FutureTuple3.of(future0, future1, future2).join();
    }

    /**
     * Create a composite future tuple with 4 {@link Future}s
     * and {@link CompositeFuture#join(Future, Future, Future, Future)}.
     */
    public static <T0, T1, T2, T3> CompositeFutureTuple4<T0, T1, T2, T3> join(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3
    ) {
        return FutureTuple4.of(future0, future1, future2, future3).join();
    }

    /**
     * Create a composite future tuple with 5 {@link Future}s
     * and {@link CompositeFuture#join(Future, Future, Future, Future, Future)}.
     */
    public static <T0, T1, T2, T3, T4> CompositeFutureTuple5<T0, T1, T2, T3, T4> join(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4
    ) {
        return FutureTuple5.of(future0, future1, future2, future3, future4).join();
    }

    /**
     * Create a composite future tuple with 6 {@link Future}s
     * and {@link CompositeFuture#join(Future, Future, Future, Future, Future, Future)}.
     */
    public static <T0, T1, T2, T3, T4, T5> CompositeFutureTuple6<T0, T1, T2, T3, T4, T5> join(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5
    ) {
        return FutureTuple6.of(future0, future1, future2, future3, future4, future5).join();
    }

    /**
     * Create a composite future tuple with 7 {@link Future}s and {@link CompositeFuture#join(List)}.
     */
    public static <T0, T1, T2, T3, T4, T5, T6> CompositeFutureTuple7<T0, T1, T2, T3, T4, T5, T6> join(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5, Future<T6> future6
    ) {
        return FutureTuple7.of(future0, future1, future2, future3, future4, future5, future6).join();
    }

    /**
     * Create a composite future tuple with 8 {@link Future}s and {@link CompositeFuture#join(List)}.
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7> CompositeFutureTuple8<T0, T1, T2, T3, T4, T5, T6, T7> join(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5, Future<T6> future6, Future<T7> future7
    ) {
        return FutureTuple8.of(future0, future1, future2, future3, future4, future5, future6, future7).join();
    }

    /**
     * Create a composite future tuple with 9 {@link Future}s and {@link CompositeFuture#join(List)}.
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8> CompositeFutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> join(
            Future<T0> future0, Future<T1> future1, Future<T2> future2, Future<T3> future3, Future<T4> future4,
            Future<T5> future5, Future<T6> future6, Future<T7> future7, Future<T8> future8
    ) {
        return FutureTuple9.of(future0, future1, future2, future3, future4, future5, future6, future7, future8).join();
    }
}
