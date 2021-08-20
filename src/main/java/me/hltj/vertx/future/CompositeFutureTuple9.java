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
 * The composite {@link Future} tuple warps a {@link CompositeFuture} and a {@link FutureTuple9}.
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
 * @param <T8> the type parameter of the 9th {@code Future}
 * @since 1.0.0
 */
@ToString(includeFieldNames = false)
public final class CompositeFutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> extends CompositeFutureWrapper {
    private final FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> tuple9;

    private CompositeFutureTuple9(CompositeFuture composite, FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> tuple9) {
        super(composite);
        this.tuple9 = tuple9;
    }

    /**
     * Create a {@link CompositeFutureTuple9} based on a {@link CompositeFuture} and a {@link FutureTuple9}.
     *
     * @param <T0>    the type parameter of the 1st {@code Future}
     * @param <T1>    the type parameter of the 2nd {@code Future}
     * @param <T2>    the type parameter of the 3rd {@code Future}
     * @param <T3>    the type parameter of the 4th {@code Future}
     * @param <T4>    the type parameter of the 5th {@code Future}
     * @param <T5>    the type parameter of the 6th {@code Future}
     * @param <T6>    the type parameter of the 7th {@code Future}
     * @param <T7>    the type parameter of the 8th {@code Future}
     * @param <T8>    the type parameter of the 9th {@code Future}
     * @param tuple9  the {@code FutureTuple9}
     * @param compose the {@code CompositeFuture}
     * @return the {@code CompositeFutureTuple9}
     */
    public static <T0, T1, T2, T3, T4, T5, T6, T7, T8> CompositeFutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> of(
            FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> tuple9, CompositeFuture compose
    ) {
        return new CompositeFutureTuple9<>(compose, tuple9);
    }

    /**
     * Return the original {@link FutureTuple9}.
     */
    public FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> tuple() {
        return tuple9;
    }

    /**
     * Run side-effect code likes {@link CompositeFutureWrapper#use(Consumer)}, but the {@code consumer10} takes the
     * original 9 {@link Future}s as additional parameters.
     * <p>
     * It likes {@link CompositeFutureTuple2#use(Consumer3)} but with 9-arity.
     */
    public void use(
            Consumer10<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>,
                    Future<T6>, Future<T7>, Future<T8>> consumer10
    ) {
        consumer10.accept(
                composite, tuple9.get_0(), tuple9.get_1(), tuple9.get_2(), tuple9.get_3(), tuple9.get_4(),
                tuple9.get_5(), tuple9.get_6(), tuple9.get_7(), tuple9.get_8()
        );
    }

    /**
     * Map the original {@link CompositeFuture} and the original 9 {@link Future}s.
     * <p>
     * It likes {@link CompositeFutureTuple2#with(Function3)} but with 9-arity.
     */
    public <R> R with(
            Function10<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>,
                    Future<T6>, Future<T7>, Future<T8>, R> function10
    ) {
        return function10.apply(
                composite, tuple9.get_0(), tuple9.get_1(), tuple9.get_2(), tuple9.get_3(), tuple9.get_4(),
                tuple9.get_5(), tuple9.get_6(), tuple9.get_7(), tuple9.get_8()
        );
    }

    /**
     * Alias for {@link #through(Function9)}.
     */
    public <R> Future<R> mapAnyway(
            Function9<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>,
                    Future<T8>, R> function9
    ) {
        return through(function9);
    }

    /**
     * Map a function that takes the original 9 {@link Future}s on complete no matter whether succeeded or failed.
     * <p>
     * It likes {@link CompositeFutureTuple2#through(BiFunction)} but with 9-arity.
     */
    public <R> Future<R> through(
            Function9<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>,
                    Future<T8>, R> function9
    ) {
        return joinThrough(function9.andThen(Future::succeededFuture));
    }

    /**
     * Alias for {@link #joinThrough(Function9)}.
     */
    public <R> Future<R> flatMapAnyway(
            Function9<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>,
                    Future<T8>, Future<R>> function9
    ) {
        return joinThrough(function9);
    }

    /**
     * Map a function that takes the original 9 {@link Future}s on complete no matter whether succeeded or failed,
     * and join (also known as {@code flatten}) the result before return.
     *
     * <p>
     * It likes {@link CompositeFutureTuple2#joinThrough(BiFunction)} but with 9-arity.
     */
    @SuppressWarnings("java:S117")
    public <R> Future<R> joinThrough(
            Function9<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>,
                    Future<T8>, Future<R>> function9
    ) {
        Supplier<Future<R>> supplier = () -> function9.apply(
                tuple9.get_0(), tuple9.get_1(), tuple9.get_2(), tuple9.get_3(), tuple9.get_4(), tuple9.get_5(),
                tuple9.get_6(), tuple9.get_7(), tuple9.get_8()
        );
        return composite.compose(_x -> joinWrap(supplier), _t -> joinWrap(supplier));
    }

    /**
     * Alias for {@link CompositeFutureTuple9#applift(Function9)}.
     */
    public <R> Future<R> mapTyped(Function9<T0, T1, T2, T3, T4, T5, T6, T7, T8, R> function9) {
        return applift(function9);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#applift(BiFunction)} but with 9-arity.
     */
    public <R> Future<R> applift(Function9<T0, T1, T2, T3, T4, T5, T6, T7, T8, R> function9) {
        return composite.map(future -> function9.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2), composite.resultAt(3),
                composite.resultAt(4), composite.resultAt(5), composite.resultAt(6), composite.resultAt(7),
                composite.resultAt(8)
        ));
    }

    /**
     * Alias for {@link CompositeFutureTuple9#joinApplift(Function9)}.
     */
    public <R> Future<R> flatMapTyped(Function9<T0, T1, T2, T3, T4, T5, T6, T7, T8, Future<R>> function9) {
        return joinApplift(function9);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}, where the function itself return a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#joinApplift(BiFunction)} but with 9-arity.
     */
    public <R> Future<R> joinApplift(Function9<T0, T1, T2, T3, T4, T5, T6, T7, T8, Future<R>> function9) {
        return composite.flatMap(future -> function9.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2), composite.resultAt(3),
                composite.resultAt(4), composite.resultAt(5), composite.resultAt(6), composite.resultAt(7),
                composite.resultAt(8)
        ));
    }
}
