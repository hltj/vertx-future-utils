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
import java.util.function.Consumer;

/**
 * Represents an operation that accepts 10 input arguments and returns no
 * result.  This is the 10-arity specialization of {@link Consumer}.
 * Unlike most other functional interfaces, {@code Consumer10} is expected
 * to operate via side-effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method
 * is {@link #accept(Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)}.
 *
 * @param <T0> the type of the 1st argument to the operation
 * @param <T1> the type of the 2nd argument to the operation
 * @param <T2> the type of the 3rd argument to the operation
 * @param <T3> the type of the 4th argument to the operation
 * @param <T4> the type of the 5th argument to the operation
 * @param <T5> the type of the 6th argument to the operation
 * @param <T6> the type of the 7th argument to the operation
 * @param <T7> the type of the 8th argument to the operation
 * @param <T8> the type of the 9th argument to the operation
 * @param <T9> the type of the 10th argument to the operation
 * @see Consumer
 * @since 1.0.0
 */
@SuppressWarnings("java:S107")
@FunctionalInterface
public interface Consumer10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param v0 the 1st input argument
     * @param v1 the 2nd input argument
     * @param v2 the 3rd input argument
     * @param v3 the 4th input argument
     * @param v4 the 5th input argument
     * @param v5 the 6th input argument
     * @param v6 the 7th input argument
     * @param v7 the 8th input argument
     * @param v8 the 9th input argument
     * @param v9 the 10th input argument
     */
    void accept(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8, T9 v9);

    /**
     * Returns a composed {@code Consumer10} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer10} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default Consumer10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9> andThen(
            Consumer10<? super T0, ? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7,
                    ? super T8, ? super T9> after
    ) {
        Objects.requireNonNull(after);

        return (v0, v1, v2, v3, v4, v5, v6, v7, v8, v9) -> {
            accept(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9);
            after.accept(v0, v1, v2, v3, v4, v5, v6, v7, v8, v9);
        };
    }
}
