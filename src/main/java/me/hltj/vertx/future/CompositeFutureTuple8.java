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
 * The composite {@link Future} tuple warps a {@link CompositeFuture} and a {@link FutureTuple8}.
 * <p>
 * Not only retains the type parameters of the original {@code Future}s,
 * but also provide many convenient operations as a complement to {@code CompositeFuture}.
 *
 * @param <T0> the type parameter of the 1st {@code Future}
 * @param <T1> the type parameter of the 2nd {@code Future}
 * @param <T2> the type parameter of the 3rd {@code Future}
 * @param <T3> the type parameter of the 4th {@code Future}
 * @param <T4> the type parameter of the 5th {@code Future}
 * @param <T5> the type parameter of the 6th {@code Future}
 * @param <T6> the type parameter of the 7th {@code Future}
 * @param <T7> the type parameter of the 8th {@code Future}
 */
@ToString(includeFieldNames = false)
final public class CompositeFutureTuple8<T0, T1, T2, T3, T4, T5, T6, T7> extends CompositeFutureWrapper {
    private final FutureTuple8<T0, T1, T2, T3, T4, T5, T6, T7> tuple8;

    private CompositeFutureTuple8(CompositeFuture composite, FutureTuple8<T0, T1, T2, T3, T4, T5, T6, T7> tuple8) {
        super(composite);
        this.tuple8 = tuple8;
    }

    /**
     * Create a {@link CompositeFutureTuple8} based on a {@link CompositeFuture} and a {@link FutureTuple8}.
     *
     * @param <T0>    the type parameter of the 1st {@code Future}
     * @param <T1>    the type parameter of the 2nd {@code Future}
     * @param <T2>    the type parameter of the 3rd {@code Future}
     * @param <T3>    the type parameter of the 4th {@code Future}
     * @param <T4>    the type parameter of the 5th {@code Future}
     * @param <T5>    the type parameter of the 6th {@code Future}
     * @param <T6>    the type parameter of the 7th {@code Future}
     * @param <T7>    the type parameter of the 8th {@code Future}
     * @param tuple8  the {@code FutureTuple8}
     * @param compose the {@code CompositeFuture}
     * @return the {@code CompositeFutureTuple8}
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7> CompositeFutureTuple8<T0, T1, T2, T3, T4, T5, T6, T7> of(
            FutureTuple8<T0, T1, T2, T3, T4, T5, T6, T7> tuple8, CompositeFuture compose
    ) {
        return new CompositeFutureTuple8<>(compose, tuple8);
    }

    /**
     * Return the original {@link FutureTuple8}.
     */
    public FutureTuple8<T0, T1, T2, T3, T4, T5, T6, T7> tuple() {
        return tuple8;
    }

    /**
     * Run side-effect code likes {@link CompositeFutureWrapper#use(Consumer)}, but the {@code consumer9} takes the
     * original 8 {@link Future}s as additional parameters.
     * <p>
     * It likes {@link CompositeFutureTuple2#use(Consumer3)} but with 8-arity.
     */
    public void use(
            Consumer9<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>,
                    Future<T6>, Future<T7>> consumer9
    ) {
        consumer9.accept(
                composite, tuple8.get_0(), tuple8.get_1(), tuple8.get_2(), tuple8.get_3(), tuple8.get_4(),
                tuple8.get_5(), tuple8.get_6(), tuple8.get_7()
        );
    }

    /**
     * Map the original {@link CompositeFuture} and the original 8 {@link Future}s.
     * <p>
     * It likes {@link CompositeFutureTuple2#with(Function3)} but with 8-arity.
     */
    public <R> R with(
            Function9<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>,
                    Future<T6>, Future<T7>, R> function8
    ) {
        return function8.apply(
                composite, tuple8.get_0(), tuple8.get_1(), tuple8.get_2(), tuple8.get_3(), tuple8.get_4(),
                tuple8.get_5(), tuple8.get_6(), tuple8.get_7()
        );
    }

    /**
     * Alias for {@link #through(Function8)}.
     */
    public <R> Future<R> mapAnyway(
            Function8<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>,
                    R> function8
    ) {
        return through(function8);
    }

    /**
     * Map a function that takes the original 8 {@link Future}s on complete no matter whether succeed or failed.
     * <p>
     * It likes {@link CompositeFutureTuple2#through(BiFunction)} but with 8-arity.
     */
    public <R> Future<R> through(
            Function8<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>,
                    R> function8
    ) {
        return joinThrough(function8.andThen(Future::succeededFuture));
    }

    /**
     * Alias for {@link #joinThrough(Function8)}.
     */
    public <R> Future<R> flatMapAnyway(
            Function8<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>,
                    Future<R>> function8
    ) {
        return joinThrough(function8);
    }

    /**
     * Map a function that takes the original 8 {@link Future}s on complete no matter whether succeed or failed,
     * and join (also known as {@code flatten}) the result before return.
     *
     * <p>
     * It likes {@link CompositeFutureTuple2#joinThrough(BiFunction)} but with 8-arity.
     */
    public <R> Future<R> joinThrough(
            Function8<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>,
                    Future<R>> function8
    ) {
        Supplier<Future<R>> supplier = () -> function8.apply(
                tuple8.get_0(), tuple8.get_1(), tuple8.get_2(), tuple8.get_3(), tuple8.get_4(), tuple8.get_5(),
                tuple8.get_6(), tuple8.get_7()
        );
        return composite.compose(_x -> joinWrap(supplier), _t -> joinWrap(supplier));
    }

    /**
     * Alias for {@link CompositeFutureTuple8#applift(Function8)}.
     */
    public <R> Future<R> mapTyped(Function8<T0, T1, T2, T3, T4, T5, T6, T7, R> function8) {
        return applift(function8);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#applift(BiFunction)} but with 8-arity.
     */
    public <R> Future<R> applift(Function8<T0, T1, T2, T3, T4, T5, T6, T7, R> function8) {
        return composite.map(future -> function8.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2), composite.resultAt(3),
                composite.resultAt(4), composite.resultAt(5), composite.resultAt(6), composite.resultAt(7)
        ));
    }

    /**
     * Alias for {@link CompositeFutureTuple8#joinApplift(Function8)}.
     */
    public <R> Future<R> flatMapTyped(Function8<T0, T1, T2, T3, T4, T5, T6, T7, Future<R>> function8) {
        return joinApplift(function8);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}, where the function itself return a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#joinApplift(BiFunction)} but with 8-arity.
     */
    public <R> Future<R> joinApplift(Function8<T0, T1, T2, T3, T4, T5, T6, T7, Future<R>> function8) {
        return composite.flatMap(future -> function8.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2), composite.resultAt(3),
                composite.resultAt(4), composite.resultAt(5), composite.resultAt(6), composite.resultAt(7)
        ));
    }
}
