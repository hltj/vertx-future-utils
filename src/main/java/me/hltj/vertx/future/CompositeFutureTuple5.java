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
 * The composite {@link Future} tuple warps a {@link CompositeFuture} and a {@link FutureTuple5}.
 * <p>
 * Not only retains the type parameters of the original {@code Future}s,
 * but also provide many convenient operations as a complement to {@code CompositeFuture}.
 *
 * @param <T0> the type parameter of the 1st {@code Future}
 * @param <T1> the type parameter of the 2nd {@code Future}
 * @param <T2> the type parameter of the 3rd {@code Future}
 * @param <T3> the type parameter of the 4th {@code Future}
 * @param <T4> the type parameter of the 5th {@code Future}
 */
@ToString(includeFieldNames = false)
final public class CompositeFutureTuple5<T0, T1, T2, T3, T4> extends CompositeFutureWrapper {
    private final FutureTuple5<T0, T1, T2, T3, T4> tuple5;

    private CompositeFutureTuple5(CompositeFuture composite, FutureTuple5<T0, T1, T2, T3, T4> tuple5) {
        super(composite);
        this.tuple5 = tuple5;
    }

    /**
     * Create a {@link CompositeFutureTuple5} based on a {@link CompositeFuture} and a {@link FutureTuple5}.
     *
     * @param <T0>    the type parameter of the 1st {@code Future}
     * @param <T1>    the type parameter of the 2nd {@code Future}
     * @param <T2>    the type parameter of the 3rd {@code Future}
     * @param <T3>    the type parameter of the 4th {@code Future}
     * @param <T4>    the type parameter of the 5th {@code Future}
     * @param tuple5  the {@code FutureTuple5}
     * @param compose the {@code CompositeFuture}
     * @return the {@code CompositeFutureTuple5}
     */
    public static <T0, T1, T2, T3, T4> CompositeFutureTuple5<T0, T1, T2, T3, T4> of(
            FutureTuple5<T0, T1, T2, T3, T4> tuple5, CompositeFuture compose
    ) {
        return new CompositeFutureTuple5<>(compose, tuple5);
    }

    /**
     * Return the original {@link FutureTuple5}.
     */
    public FutureTuple5<T0, T1, T2, T3, T4> tuple() {
        return tuple5;
    }

    /**
     * Run side-effect code likes {@link CompositeFutureWrapper#use(Consumer)}, but the {@code consumer6} takes the
     * original 5 {@link Future}s as additional parameters.
     * <p>
     * It likes {@link CompositeFutureTuple2#use(Consumer3)} but with 5-arity.
     */
    public void use(Consumer6<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>> consumer6) {
        consumer6.accept(composite, tuple5.get_0(), tuple5.get_1(), tuple5.get_2(), tuple5.get_3(), tuple5.get_4());
    }

    /**
     * Map the original {@link CompositeFuture} and the original 5 {@link Future}s.
     * <p>
     * It likes {@link CompositeFutureTuple2#with(Function3)} but with 5-arity.
     */
    public <R> R with(
            Function6<CompositeFuture, Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, R> function6
    ) {
        return function6.apply(
                composite, tuple5.get_0(), tuple5.get_1(), tuple5.get_2(), tuple5.get_3(), tuple5.get_4()
        );
    }

    /**
     * Alias for {@link #through(Function5)}.
     */
    public <R> Future<R> mapAnyway(Function5<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, R> function5) {
        return through(function5);
    }

    /**
     * Map a function that takes the original 5 {@link Future}s on complete no matter whether succeed or failed.
     * <p>
     * It likes {@link CompositeFutureTuple2#through(BiFunction)} but with 5-arity.
     */
    public <R> Future<R> through(Function5<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, R> function5) {
        return joinThrough(function5.andThen(Future::succeededFuture));
    }

    /**
     * Alias for {@link #joinThrough(Function5)}.
     */
    public <R> Future<R> flatMapAnyway(
            Function5<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<R>> function5
    ) {
        return joinThrough(function5);
    }

    /**
     * Map a function that takes the original 5 {@link Future}s on complete no matter whether succeed or failed,
     * and join (also known as {@code flatten}) the result before return.
     *
     * <p>
     * It likes {@link CompositeFutureTuple2#joinThrough(BiFunction)} but with 5-arity.
     */
    public <R> Future<R> joinThrough(
            Function5<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<R>> function5
    ) {
        Supplier<Future<R>> supplier = () -> function5.apply(
                tuple5.get_0(), tuple5.get_1(), tuple5.get_2(), tuple5.get_3(), tuple5.get_4()
        );
        return composite.compose(_x -> joinWrap(supplier), _t -> joinWrap(supplier));
    }

    /**
     * Alias for {@link CompositeFutureTuple5#applift(Function5)}.
     */
    public <R> Future<R> mapTyped(Function5<T0, T1, T2, T3, T4, R> function5) {
        return applift(function5);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#applift(BiFunction)} but with 5-arity.
     */
    public <R> Future<R> applift(Function5<T0, T1, T2, T3, T4, R> function5) {
        return composite.map(future -> function5.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2), composite.resultAt(3),
                composite.resultAt(4)
        ));
    }

    /**
     * Alias for {@link CompositeFutureTuple5#joinApplift(Function5)}.
     */
    public <R> Future<R> flatMapTyped(Function5<T0, T1, T2, T3, T4, Future<R>> function5) {
        return joinApplift(function5);
    }

    /**
     * Apply a function that accept all results of the original {@link Future}s on success, and return
     * a {@link Future}, where the function itself return a {@link Future}.
     * <p>
     * It likes {@link CompositeFutureTuple2#joinApplift(BiFunction)} but with 5-arity.
     */
    public <R> Future<R> joinApplift(Function5<T0, T1, T2, T3, T4, Future<R>> function5) {
        return composite.flatMap(future -> function5.apply(
                composite.resultAt(0), composite.resultAt(1), composite.resultAt(2), composite.resultAt(3),
                composite.resultAt(4)
        ));
    }
}
