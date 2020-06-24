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
 * The composite {@link Future} tuple warps a {@link CompositeFuture} and a {@link FutureTuple6}.
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
 */
@ToString(includeFieldNames = false)
final public class CompositeFutureTuple6<T0, T1, T2, T3, T4, T5> extends CompositeFutureWrapper {
    private final FutureTuple6<T0, T1, T2, T3, T4, T5> tuple6;

    private CompositeFutureTuple6(CompositeFuture composite, FutureTuple6<T0, T1, T2, T3, T4, T5> tuple6) {
        super(composite);
        this.tuple6 = tuple6;
    }

    /**
     * Create a {@link CompositeFutureTuple6} based on a {@link CompositeFuture} and a {@link FutureTuple6}.
     *
     * @param <T0>    the type parameter of the 1st {@code Future}
     * @param <T1>    the type parameter of the 2nd {@code Future}
     * @param <T2>    the type parameter of the 3rd {@code Future}
     * @param <T3>    the type parameter of the 4th {@code Future}
     * @param <T4>    the type parameter of the 5th {@code Future}
     * @param <T5>    the type parameter of the 6th {@code Future}
     * @param tuple6  the {@code FutureTuple6}
     * @param compose the {@code CompositeFuture}
     * @return the {@code CompositeFutureTuple6}
     */
    public static <T0, T1, T2, T3, T4, T5> CompositeFutureTuple6<T0, T1, T2, T3, T4, T5> of(
            FutureTuple6<T0, T1, T2, T3, T4, T5> tuple6, CompositeFuture compose
    ) {
        return new CompositeFutureTuple6<>(compose, tuple6);
    }

    /**
     * Return the original {@link FutureTuple6}.
     */
    public FutureTuple6<T0, T1, T2, T3, T4, T5> tuple() {
        return tuple6;
    }

    /**
     * Run side-effect code likes {@link CompositeFutureWrapper#use(Consumer)}, but the {@code consumer7} takes the
     * original 6 {@link Future}s as additional parameters.
     * <p>
     * It likes {@link CompositeFutureTuple2#use(Consumer3)} but with 6-arity.
     */
    public void use(
            Consumer7<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>,
                    Future<T5>> consumer7
    ) {
        consumer7.accept(
                composite, tuple6.get_0(), tuple6.get_1(), tuple6.get_2(), tuple6.get_3(), tuple6.get_4(),
                tuple6.get_5()
        );
    }

    /**
     * Map the original {@link CompositeFuture} and the original 6 {@link Future}s.
     * <p>
     * It likes {@link CompositeFutureTuple2#with(Function3)} but with 6-arity.
     */
    public <R> R with(
            Function7<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>,
                    R> function6
    ) {
        return function6.apply(
                composite, tuple6.get_0(), tuple6.get_1(), tuple6.get_2(), tuple6.get_3(), tuple6.get_4(),
                tuple6.get_5()
        );
    }

    /**
     * Alias for {@link #through(Function6)}.
     */
    public <R> Future<R> mapAnyway(
            Function6<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, R> function6
    ) {
        return through(function6);
    }

    /**
     * Map a function that takes the original 6 {@link Future}s on complete no matter whether succeed or failed.
     * <p>
     * It likes {@link CompositeFutureTuple2#through(BiFunction)} but with 6-arity.
     */
    public <R> Future<R> through(
            Function6<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, R> function6
    ) {
        return joinThrough(function6.andThen(Future::succeededFuture));
    }

    /**
     * Alias for {@link #joinThrough(Function6)}.
     */
    public <R> Future<R> flatMapAnyway(
            Function6<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<R>> function6
    ) {
        return joinThrough(function6);
    }

    /**
     * Map a function that takes the original 6 {@link Future}s on complete no matter whether succeed or failed,
     * and join (also known as {@code flatten}) the result before return.
     *
     * <p>
     * It likes {@link CompositeFutureTuple2#joinThrough(BiFunction)} but with 6-arity.
     */
    public <R> Future<R> joinThrough(
            Function6<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<R>> function6
    ) {
        Supplier<Future<R>> supplier = () -> function6.apply(
                tuple6.get_0(), tuple6.get_1(), tuple6.get_2(), tuple6.get_3(), tuple6.get_4(), tuple6.get_5()
        );
        return composite.compose(_x -> joinWrap(supplier), _t -> joinWrap(supplier));
    }

    /**
     * Alias for {@link CompositeFutureTuple6#applift(Function6)}.
     */
    public <R> Future<R> mapTyped(Function6<T0, T1, T2, T3, T4, T5, R> function6) {
        return applift(function6);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#applift(BiFunction)} but with 6-arity.
     */
    public <R> Future<R> applift(Function6<T0, T1, T2, T3, T4, T5, R> function6) {
        return composite.map(future -> function6.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2), composite.resultAt(3),
                composite.resultAt(4), composite.resultAt(5)
        ));
    }

    /**
     * Alias for {@link CompositeFutureTuple6#joinApplift(Function6)}.
     */
    public <R> Future<R> flatMapTyped(Function6<T0, T1, T2, T3, T4, T5, Future<R>> function6) {
        return joinApplift(function6);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}, where the function itself return a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#joinApplift(BiFunction)} but with 6-arity.
     */
    public <R> Future<R> joinApplift(Function6<T0, T1, T2, T3, T4, T5, Future<R>> function6) {
        return composite.flatMap(future -> function6.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2), composite.resultAt(3),
                composite.resultAt(4), composite.resultAt(5)
        ));
    }
}
