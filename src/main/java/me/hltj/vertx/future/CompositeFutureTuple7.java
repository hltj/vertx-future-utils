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
 * The composite {@link Future} tuple warps a {@link CompositeFuture} and a {@link FutureTuple7}.
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
 */
@ToString(includeFieldNames = false)
final public class CompositeFutureTuple7<T0, T1, T2, T3, T4, T5, T6> extends CompositeFutureWrapper {
    private final FutureTuple7<T0, T1, T2, T3, T4, T5, T6> tuple7;

    private CompositeFutureTuple7(CompositeFuture composite, FutureTuple7<T0, T1, T2, T3, T4, T5, T6> tuple7) {
        super(composite);
        this.tuple7 = tuple7;
    }

    /**
     * Create a {@link CompositeFutureTuple7} based on a {@link CompositeFuture} and a {@link FutureTuple7}.
     *
     * @param <T0>    the type parameter of the 1st {@code Future}
     * @param <T1>    the type parameter of the 2nd {@code Future}
     * @param <T2>    the type parameter of the 3rd {@code Future}
     * @param <T3>    the type parameter of the 4th {@code Future}
     * @param <T4>    the type parameter of the 5th {@code Future}
     * @param <T5>    the type parameter of the 6th {@code Future}
     * @param <T6>    the type parameter of the 7th {@code Future}
     * @param tuple7  the {@code FutureTuple7}
     * @param compose the {@code CompositeFuture}
     * @return the {@code CompositeFutureTuple7}
     */
    public static <T0, T1, T2, T3, T4, T5, T6> CompositeFutureTuple7<T0, T1, T2, T3, T4, T5, T6> of(
            FutureTuple7<T0, T1, T2, T3, T4, T5, T6> tuple7, CompositeFuture compose
    ) {
        return new CompositeFutureTuple7<>(compose, tuple7);
    }

    /**
     * Return the original {@link FutureTuple7}.
     */
    public FutureTuple7<T0, T1, T2, T3, T4, T5, T6> tuple() {
        return tuple7;
    }

    /**
     * Run side-effect code likes {@link CompositeFutureWrapper#use(Consumer)}, but the {@code consumer8} takes the
     * original 7 {@link Future}s as additional parameters.
     * <p>
     * It likes {@link CompositeFutureTuple2#use(Consumer3)} but with 7-arity.
     */
    public void use(
            Consumer8<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>,
                    Future<T6>> consumer8
    ) {
        consumer8.accept(
                composite, tuple7.get_0(), tuple7.get_1(), tuple7.get_2(), tuple7.get_3(), tuple7.get_4(),
                tuple7.get_5(), tuple7.get_6()
        );
    }

    /**
     * Map the original {@link CompositeFuture} and the original 7 {@link Future}s.
     * <p>
     * It likes {@link CompositeFutureTuple2#with(Function3)} but with 7-arity.
     */
    public <R> R with(
            Function8<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>,
                    Future<T6>, R> function7
    ) {
        return function7.apply(
                composite, tuple7.get_0(), tuple7.get_1(), tuple7.get_2(), tuple7.get_3(), tuple7.get_4(),
                tuple7.get_5(), tuple7.get_6()
        );
    }

    /**
     * Alias for {@link #through(Function7)}.
     */
    public <R> Future<R> mapAnyway(
            Function7<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, R> function7
    ) {
        return through(function7);
    }

    /**
     * Map a function that takes the original 7 {@link Future}s on complete no matter whether succeed or failed.
     * <p>
     * It likes {@link CompositeFutureTuple2#through(BiFunction)} but with 7-arity.
     */
    public <R> Future<R> through(
            Function7<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, R> function7
    ) {
        return joinThrough(function7.andThen(Future::succeededFuture));
    }

    /**
     * Alias for {@link #joinThrough(Function7)}.
     */
    public <R> Future<R> flatMapAnyway(
            Function7<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>,
                    Future<R>> function7
    ) {
        return joinThrough(function7);
    }

    /**
     * Map a function that takes the original 7 {@link Future}s on complete no matter whether succeed or failed,
     * and join (also known as {@code flatten}) the result before return.
     *
     * <p>
     * It likes {@link CompositeFutureTuple2#joinThrough(BiFunction)} but with 7-arity.
     */
    public <R> Future<R> joinThrough(
            Function7<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>,
                    Future<R>> function7
    ) {
        Supplier<Future<R>> supplier = () -> function7.apply(
                tuple7.get_0(), tuple7.get_1(), tuple7.get_2(), tuple7.get_3(), tuple7.get_4(), tuple7.get_5(),
                tuple7.get_6()
        );
        return composite.compose(_x -> joinWrap(supplier), _t -> joinWrap(supplier));
    }

    /**
     * Alias for {@link CompositeFutureTuple7#applift(Function7)}.
     */
    public <R> Future<R> mapTyped(Function7<T0, T1, T2, T3, T4, T5, T6, R> function7) {
        return applift(function7);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#applift(BiFunction)} but with 7-arity.
     */
    public <R> Future<R> applift(Function7<T0, T1, T2, T3, T4, T5, T6, R> function7) {
        return composite.map(future -> function7.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2), composite.resultAt(3),
                composite.resultAt(4), composite.resultAt(5), composite.resultAt(6)
        ));
    }

    /**
     * Alias for {@link CompositeFutureTuple7#joinApplift(Function7)}.
     */
    public <R> Future<R> flatMapTyped(Function7<T0, T1, T2, T3, T4, T5, T6, Future<R>> function7) {
        return joinApplift(function7);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}, where the function itself return a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#joinApplift(BiFunction)} but with 7-arity.
     */
    public <R> Future<R> joinApplift(Function7<T0, T1, T2, T3, T4, T5, T6, Future<R>> function7) {
        return composite.flatMap(future -> function7.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2), composite.resultAt(3),
                composite.resultAt(4), composite.resultAt(5), composite.resultAt(6)
        ));
    }
}
