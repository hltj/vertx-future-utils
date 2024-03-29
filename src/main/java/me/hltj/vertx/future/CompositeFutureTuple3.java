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
package me.hltj.vertx.future;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import lombok.ToString;
import me.hltj.vertx.function.Consumer3;
import me.hltj.vertx.function.Consumer4;
import me.hltj.vertx.function.Function3;
import me.hltj.vertx.function.Function4;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static me.hltj.vertx.FutureUtils.joinWrap;

/**
 * The composite {@link Future} tuple warps a {@link CompositeFuture} and a {@link FutureTuple3}.
 * <p>
 * Not only retains the type parameters of the original {@code Future}s,
 * but also provide many convenient operations as a complement to {@code CompositeFuture}.
 *
 * @param <T0> the type parameter of the 1st {@code Future}
 * @param <T1> the type parameter of the 2nd {@code Future}
 * @param <T2> the type parameter of the 3rd {@code Future}
 * @since 1.0.0
 */
@ToString(includeFieldNames = false)
public final class CompositeFutureTuple3<T0, T1, T2> extends CompositeFutureWrapper {
    private final FutureTuple3<T0, T1, T2> tuple3;

    private CompositeFutureTuple3(CompositeFuture composite, FutureTuple3<T0, T1, T2> tuple3) {
        super(composite);
        this.tuple3 = tuple3;
    }

    /**
     * Create a {@link CompositeFutureTuple3} based on a {@link CompositeFuture} and a {@link FutureTuple3}.
     *
     * @param <T0>    the type parameter of the 1st {@code Future}
     * @param <T1>    the type parameter of the 2nd {@code Future}
     * @param <T2>    the type parameter of the 3rd {@code Future}
     * @param tuple3  the {@code FutureTuple3}
     * @param compose the {@code CompositeFuture}
     * @return the {@code CompositeFutureTuple3}
     */
    public static <T0, T1, T2> CompositeFutureTuple3<T0, T1, T2> of(
            FutureTuple3<T0, T1, T2> tuple3, CompositeFuture compose
    ) {
        return new CompositeFutureTuple3<>(compose, tuple3);
    }

    /**
     * Return the original {@link FutureTuple3}.
     */
    public FutureTuple3<T0, T1, T2> tuple() {
        return tuple3;
    }

    /**
     * Run side-effect code likes {@link CompositeFutureWrapper#use(Consumer)}, but the {@code consumer4} takes the
     * original 3 {@link Future}s as additional parameters.
     * <p>
     * It likes {@link CompositeFutureTuple2#use(Consumer3)} but with 3-arity.
     */
    public void use(Consumer4<CompositeFuture, Future<T0>, Future<T1>, Future<T2>> consumer4) {
        consumer4.accept(composite, tuple3.get_0(), tuple3.get_1(), tuple3.get_2());
    }

    /**
     * Map the original {@link CompositeFuture} and the original 3 {@link Future}s.
     * <p>
     * It likes {@link CompositeFutureTuple2#with(Function3)} but with 3-arity.
     */
    public <R> R with(Function4<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, R> function4) {
        return function4.apply(composite, tuple3.get_0(), tuple3.get_1(), tuple3.get_2());
    }

    /**
     * Alias for {@link #through(Function3)}.
     */
    public <R> Future<R> mapAnyway(Function3<Future<T0>, Future<T1>, Future<T2>, R> function3) {
        return through(function3);
    }

    /**
     * Map a function that takes the original 3 {@link Future}s on complete no matter whether succeeded or failed.
     * <p>
     * It likes {@link CompositeFutureTuple2#through(BiFunction)} but with 3-arity.
     */
    public <R> Future<R> through(Function3<Future<T0>, Future<T1>, Future<T2>, R> function3) {
        return joinThrough(function3.andThen(Future::succeededFuture));
    }

    /**
     * Alias for {@link #joinThrough(Function3)}.
     */
    public <R> Future<R> flatMapAnyway(Function3<Future<T0>, Future<T1>, Future<T2>, Future<R>> function3) {
        return joinThrough(function3);
    }

    /**
     * Map a function that takes the original 3 {@link Future}s on complete no matter whether succeeded or failed,
     * and join (also known as {@code flatten}) the result before return.
     *
     * <p>
     * It likes {@link CompositeFutureTuple2#joinThrough(BiFunction)} but with 3-arity.
     */
    @SuppressWarnings("java:S117")
    public <R> Future<R> joinThrough(Function3<Future<T0>, Future<T1>, Future<T2>, Future<R>> function3) {
        Supplier<Future<R>> supplier = () -> function3.apply(tuple3.get_0(), tuple3.get_1(), tuple3.get_2());
        return composite.compose(_x -> joinWrap(supplier), _t -> joinWrap(supplier));
    }

    /**
     * Alias for {@link CompositeFutureTuple3#applift(Function3)}.
     */
    public <R> Future<R> mapTyped(Function3<T0, T1, T2, R> function3) {
        return applift(function3);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#applift(BiFunction)} but with 3-arity.
     */
    public <R> Future<R> applift(Function3<T0, T1, T2, R> function3) {
        return composite.map(future -> function3.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2)
        ));
    }

    /**
     * Alias for {@link CompositeFutureTuple3#joinApplift(Function3)}.
     */
    public <R> Future<R> flatMapTyped(Function3<T0, T1, T2, Future<R>> function3) {
        return joinApplift(function3);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}, where the function itself return a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#joinApplift(BiFunction)} but with 3-arity.
     */
    public <R> Future<R> joinApplift(Function3<T0, T1, T2, Future<R>> function3) {
        return composite.flatMap(future -> function3.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2)
        ));
    }
}
