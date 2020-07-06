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
import me.hltj.vertx.function.*;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static me.hltj.vertx.FutureUtils.joinWrap;

/**
 * The composite {@link Future} tuple warps a {@link CompositeFuture} and a {@link FutureTuple4}.
 * <p>
 * Not only retains the type parameters of the original {@code Future}s,
 * but also provide many convenient operations as a complement to {@code CompositeFuture}.
 *
 * @param <T0> the type parameter of the 1st {@code Future}
 * @param <T1> the type parameter of the 2nd {@code Future}
 * @param <T2> the type parameter of the 3rd {@code Future}
 * @param <T3> the type parameter of the 4th {@code Future}
 * @since 1.0.0
 */
@ToString(includeFieldNames = false)
final public class CompositeFutureTuple4<T0, T1, T2, T3> extends CompositeFutureWrapper {
    private final FutureTuple4<T0, T1, T2, T3> tuple4;

    private CompositeFutureTuple4(CompositeFuture composite, FutureTuple4<T0, T1, T2, T3> tuple4) {
        super(composite);
        this.tuple4 = tuple4;
    }

    /**
     * Create a {@link CompositeFutureTuple4} based on a {@link CompositeFuture} and a {@link FutureTuple4}.
     *
     * @param <T0>    the type parameter of the 1st {@code Future}
     * @param <T1>    the type parameter of the 2nd {@code Future}
     * @param <T2>    the type parameter of the 3rd {@code Future}
     * @param <T3>    the type parameter of the 4th {@code Future}
     * @param tuple4  the {@code FutureTuple4}
     * @param compose the {@code CompositeFuture}
     * @return the {@code CompositeFutureTuple4}
     */
    public static <T0, T1, T2, T3> CompositeFutureTuple4<T0, T1, T2, T3> of(
            FutureTuple4<T0, T1, T2, T3> tuple4, CompositeFuture compose
    ) {
        return new CompositeFutureTuple4<>(compose, tuple4);
    }

    /**
     * Return the original {@link FutureTuple4}.
     */
    public FutureTuple4<T0, T1, T2, T3> tuple() {
        return tuple4;
    }

    /**
     * Run side-effect code likes {@link CompositeFutureWrapper#use(Consumer)}, but the {@code consumer5} takes the
     * original 4 {@link Future}s as additional parameters.
     * <p>
     * It likes {@link CompositeFutureTuple2#use(Consumer3)} but with 4-arity.
     */
    public void use(Consumer5<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, Future<T3>> consumer5) {
        consumer5.accept(composite, tuple4.get_0(), tuple4.get_1(), tuple4.get_2(), tuple4.get_3());
    }

    /**
     * Map the original {@link CompositeFuture} and the original 4 {@link Future}s.
     * <p>
     * It likes {@link CompositeFutureTuple2#with(Function3)} but with 4-arity.
     */
    public <R> R with(Function5<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, Future<T3>, R> function5) {
        return function5.apply(composite, tuple4.get_0(), tuple4.get_1(), tuple4.get_2(), tuple4.get_3());
    }

    /**
     * Alias for {@link #through(Function4)}.
     */
    public <R> Future<R> mapAnyway(Function4<Future<T0>, Future<T1>, Future<T2>, Future<T3>, R> function4) {
        return through(function4);
    }

    /**
     * Map a function that takes the original 4 {@link Future}s on complete no matter whether succeeded or failed.
     * <p>
     * It likes {@link CompositeFutureTuple2#through(BiFunction)} but with 4-arity.
     */
    public <R> Future<R> through(Function4<Future<T0>, Future<T1>, Future<T2>, Future<T3>, R> function4) {
        return joinThrough(function4.andThen(Future::succeededFuture));
    }

    /**
     * Alias for {@link #joinThrough(Function4)}.
     */
    public <R> Future<R> flatMapAnyway(Function4<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<R>> function4) {
        return joinThrough(function4);
    }

    /**
     * Map a function that takes the original 4 {@link Future}s on complete no matter whether succeeded or failed,
     * and join (also known as {@code flatten}) the result before return.
     *
     * <p>
     * It likes {@link CompositeFutureTuple2#joinThrough(BiFunction)} but with 4-arity.
     */
    public <R> Future<R> joinThrough(Function4<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<R>> function4) {
        Supplier<Future<R>> supplier = () -> function4.apply(
                tuple4.get_0(), tuple4.get_1(), tuple4.get_2(), tuple4.get_3()
        );
        return composite.compose(_x -> joinWrap(supplier), _t -> joinWrap(supplier));
    }

    /**
     * Alias for {@link CompositeFutureTuple4#applift(Function4)}.
     */
    public <R> Future<R> mapTyped(Function4<T0, T1, T2, T3, R> function4) {
        return applift(function4);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#applift(BiFunction)} but with 4-arity.
     */
    public <R> Future<R> applift(Function4<T0, T1, T2, T3, R> function4) {
        return composite.map(future -> function4.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2), composite.resultAt(3)
        ));
    }

    /**
     * Alias for {@link CompositeFutureTuple4#joinApplift(Function4)}.
     */
    public <R> Future<R> flatMapTyped(Function4<T0, T1, T2, T3, Future<R>> function4) {
        return joinApplift(function4);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}, where the function itself return a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#joinApplift(BiFunction)} but with 4-arity.
     */
    public <R> Future<R> joinApplift(Function4<T0, T1, T2, T3, Future<R>> function4) {
        return composite.flatMap(future -> function4.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2), composite.resultAt(3)
        ));
    }
}
