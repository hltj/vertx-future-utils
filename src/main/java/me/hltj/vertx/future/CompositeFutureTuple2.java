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
import me.hltj.vertx.function.Function3;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static me.hltj.vertx.FutureUtils.joinWrap;

/**
 * The composite {@link Future} tuple warps a {@link CompositeFuture} and a {@link FutureTuple2}.
 * <p>
 * Not only retains the type parameters of the original {@code Future}s,
 * but also provide many convenient operations as a complement to {@code CompositeFuture}.
 *
 * @param <T0> the type parameter of the 1st {@code Future}
 * @param <T1> the type parameter of the 2nd {@code Future}
 */
@ToString(includeFieldNames = false)
final public class CompositeFutureTuple2<T0, T1> extends CompositeFutureWrapper {
    private final FutureTuple2<T0, T1> tuple2;

    private CompositeFutureTuple2(CompositeFuture composite, FutureTuple2<T0, T1> tuple2) {
        super(composite);
        this.tuple2 = tuple2;
    }

    /**
     * Create a {@link CompositeFutureTuple2} based on a {@link CompositeFuture} and a {@link FutureTuple2}.
     *
     * @param <T0>    the type parameter of the 1st {@code Future}
     * @param <T1>    the type parameter of the 2nd {@code Future}
     * @param tuple2  the {@code FutureTuple2}
     * @param compose the {@code CompositeFuture}
     * @return the {@code CompositeFutureTuple2}
     */
    public static <T0, T1> CompositeFutureTuple2<T0, T1> of(FutureTuple2<T0, T1> tuple2, CompositeFuture compose) {
        return new CompositeFutureTuple2<>(compose, tuple2);
    }

    /**
     * Return the original {@link FutureTuple2}.
     */
    public FutureTuple2<T0, T1> tuple() {
        return tuple2;
    }

    /**
     * Run side-effect code likes {@link CompositeFutureWrapper#use(Consumer)}, but the {@code consumer3} takes the
     * original two {@link Future}s as additional parameters.
     * <p>
     * Likes {@link CompositeFutureWrapper#use(Consumer)}, a useful scenario is to capture the original
     * two {@code Future}s in the functions that passed as parameters of {@code CompositeFuture}'s methods. e.g. :
     * <pre>
     *     Future&lt;Double&gt; future0 = Future.succeededFuture(1.0);
     *     Future&lt;Integer&gt; future1 = Future.failedFuture("error");
     *     FutureUtils.join(future0, future1).use((composite, fut0, fut1) -&gt;
     *             composite.onComplete(_ar -&gt;
     *                     System.out.println(String.format("original futures: (%s, %s)", fut0, fut1))
     *             )
     *     );
     * </pre>
     *
     * @param consumer3 the side-effect code that takes the {@code CompositeFuture} and the original
     *                  two {@code Future}s as parameters
     */
    public void use(Consumer3<CompositeFuture, Future<T0>, Future<T1>> consumer3) {
        consumer3.accept(composite, tuple2.get_0(), tuple2.get_1());
    }

    /**
     * Map the original {@link CompositeFuture} and the original two {@link Future}s.
     * <p>
     * Likes {@link CompositeFutureTuple2#use(Consumer3)} but return a {@code Future}, a useful scenario is to capture
     * the original two {@code Future}s in the functions that passed as parameters of {@code CompositeFuture}'s
     * methods. e.g. :
     *
     * <pre>
     *     Future&lt;Double&gt; future0 = Future.succeededFuture(1.0);
     *     Future&lt;Integer&gt; future1 = Future.failedFuture("error");
     *     Future&lt;String&gt; strFuture = FutureUtils.tuple(future0, future1)
     *             .otherwiseEmpty()
     *             .join()
     *             .with((composite, fut0, fut1) -&gt;
     *                     composite.map(_x -&gt; String.format("original futures: (%s, %s)", fut0, fut1))
     *             );
     * </pre>
     *
     * @param function3 the function to map
     * @param <R>       the result type of the {@code function3}
     * @return the result of {@code function3} application
     */
    public <R> R with(Function3<CompositeFuture, Future<T0>, Future<T1>, R> function3) {
        return function3.apply(composite, tuple2.get_0(), tuple2.get_1());
    }

    /**
     * Alias for {@link #through(BiFunction)}.
     */
    public <R> Future<R> mapAnyway(BiFunction<Future<T0>, Future<T1>, R> function2) {
        return through(function2);
    }

    /**
     * Map a function that takes the original two {@link Future}s on complete no matter whether succeeded or failed.
     *
     * @param function2 the function to map on complete
     * @param <R>       the result type of the {@code function2}
     * @return the result {@code Future}
     */
    public <R> Future<R> through(BiFunction<Future<T0>, Future<T1>, R> function2) {
        return joinThrough(function2.andThen(Future::succeededFuture));
    }

    /**
     * Alias for {@link #joinThrough(BiFunction)}.
     */
    public <R> Future<R> flatMapAnyway(BiFunction<Future<T0>, Future<T1>, Future<R>> function2) {
        return joinThrough(function2);
    }

    /**
     * Map a function that takes the original two {@link Future}s on complete no matter whether succeeded or failed,
     * and join (also known as {@code flatten}) the result before return.
     *
     * @param function2 the function to map on complete
     * @param <R>       the type parameter of the result type of the {@code function}
     * @return the result {@code Future}
     */
    public <R> Future<R> joinThrough(BiFunction<Future<T0>, Future<T1>, Future<R>> function2) {
        Supplier<Future<R>> supplier = () -> function2.apply(tuple2.get_0(), tuple2.get_1());
        return composite.compose(_x -> joinWrap(supplier), _t -> joinWrap(supplier));
    }

    /**
     * Alias for {@link CompositeFutureTuple2#applift(BiFunction)}.
     */
    public <R> Future<R> mapTyped(BiFunction<T0, T1, R> function2) {
        return applift(function2);
    }

    /**
     * Apply a function that accept both results of the original {@link Future}s on success, and return
     * a {@link Future}.
     * <p>
     * It likes lifting the function and then applying it to the original {@code Future}s, this is also the origin of
     * the name.
     * <p>
     * When the composite future fails, the failure will be propagated to the returned future and the function will not
     * be called.
     * <p>
     * When the composite future succeeds, the function will be called. If the function throws a non-checked exception,
     * the returned future will be failed with this exception. Otherwise, the returned future will be a future succeed
     * with the function return value.
     *
     * @param function2 the function
     * @param <R>       the result type of {@code function2}
     * @return the result {@code Future}
     */
    public <R> Future<R> applift(BiFunction<T0, T1, R> function2) {
        return composite.map(future -> function2.apply(composite.resultAt(0), composite.resultAt(1)));
    }

    /**
     * Alias for {@link CompositeFutureTuple2#joinApplift(BiFunction)}.
     */
    public <R> Future<R> flatMapTyped(BiFunction<T0, T1, Future<R>> function2) {
        return joinApplift(function2);
    }

    /**
     * Apply a function that accept both results of the original {@link Future}s on success, and return
     * a {@link Future}, where the function itself return a {@link Future}.
     * <p>
     * It likes lifting the function and then applying it to the original {@code Future}s, and joining (also known
     * as {@code flatten}ing) the nested result {@code Future}s before return, this is also the origin of the name.
     * <p>
     * When the composite future fails, the failure will be propagated to the returned future and the function will not
     * be called.
     * <p>
     * When the composite future succeeds, the function will be called. If the function throws a non-checked exception,
     * the returned future will be failed with this exception. Otherwise, the returned future will be the function
     * returned future.
     *
     * @param function2 the function
     * @param <R>       the result type of {@code function2}
     * @return the result {@code Future}
     */
    public <R> Future<R> joinApplift(BiFunction<T0, T1, Future<R>> function2) {
        return composite.flatMap(future -> function2.apply(composite.resultAt(0), composite.resultAt(1)));
    }
}
