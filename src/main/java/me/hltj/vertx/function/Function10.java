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
package me.hltj.vertx.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts 10 arguments and produces a result.
 * This is the 10-arity specialization of {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method
 * is {@link #apply(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}.
 *
 * @param <T0> the type of the 1st argument to the function
 * @param <T1> the type of the 2nd argument to the function
 * @param <T2> the type of the 3rd argument to the function
 * @param <T3> the type of the 4th argument to the function
 * @param <T4> the type of the 5th argument to the function
 * @param <T5> the type of the 6th argument to the function
 * @param <T6> the type of the 7th argument to the function
 * @param <T7> the type of the 8th argument to the function
 * @param <T8> the type of the 9th argument to the function
 * @param <T9> the type of the 10th argument to the function
 * @param <R>  the type of the result of the function
 * @see Function
 * @since 1.0.0
 */
@SuppressWarnings("java:S107")
@FunctionalInterface
public interface Function10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param v0 the 1st function argument
     * @param v1 the 2nd function argument
     * @param v2 the 3rd function argument
     * @param v3 the 4th function argument
     * @param v4 the 5th function argument
     * @param v5 the 6th function argument
     * @param v6 the 7th function argument
     * @param v7 the 8th function argument
     * @param v8 the 9th function argument
     * @param v9 the 10th function argument
     * @return the function result
     */
    R apply(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <R1>  the type of output of the {@code after} function, and of the
     *              composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <R1> Function10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, R1> andThen(
            Function<? super R, ? extends R1> after
    ) {
        Objects.requireNonNull(after);
        return (T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9) ->
                after.apply(apply(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9));
    }
}
