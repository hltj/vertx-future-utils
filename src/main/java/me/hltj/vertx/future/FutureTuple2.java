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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import me.hltj.vertx.FutureUtils;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * A tuple of two {@link Future}s.
 *
 * @param <T0> the type parameter of the 1st {@code Future}
 * @param <T1> the type parameter of the 2nd {@code Future}
 */
@Getter
@AllArgsConstructor(staticName = "of")
@ToString(includeFieldNames = false)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class FutureTuple2<T0, T1> {
    Future<T0> _0;
    Future<T1> _1;

    /**
     * Map the result of the {@link Future}s to {@code null}s.
     * <p>
     * It behaves as applying {@link Future#mapEmpty()} to each {@code Future}.
     *
     * @return the mapped {@code Future}s
     */
    public FutureTuple2<T0, T1> mapEmpty() {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map the failure of the {@link Future}s to specific values.
     * <p>
     * It behaves as applying {@link Future#otherwise(Object)} to each {@code Future}.
     *
     * @param v0 specific the value used for mapping the 1st {@code Future}
     * @param v1 specific the value used for mapping the 2nd {@code Future}
     * @return the mapped {@code Future}s
     */
    public FutureTuple2<T0, T1> otherwise(T0 v0, T1 v1) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map the failure of the {@link Future}s to specific values, and execute @{code onFailure} before mapping.
     * <p>
     * If the {@code onFailure} throws an exception, the returned {@code Future} will be failed with this exception.
     * Otherwise, the return value as the same as applying {@link Future#otherwise(Object)} to each {@code Future}.
     *
     * @param onFailure a side-effect function that take the failure cause as parameter
     * @param v0        specific the value used for mapping the 1st {@code Future}
     * @param v1        specific the value used for mapping the 2nd {@code Future}
     * @return the mapped {@code Future}s
     */
    public FutureTuple2<T0, T1> otherwise(Consumer<Throwable> onFailure, T0 v0, T1 v1) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map the failure of the {@link Future}s to {@code null}s.
     * <p>
     * It behaves as applying {@link Future#otherwiseEmpty()} to each {@code Future}.
     *
     * @return the mapped {@code Future}s
     */
    public FutureTuple2<T0, T1> otherwiseEmpty() {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map {@link Future}s that succeed with null to default values.
     * <p>
     * It behaves as applying {@link FutureUtils#defaultWith(Future, Object)} to each {@code Future}.
     *
     * @param v0 the default value used for mapping the 1st {@code Future}
     * @param v1 the default value used for mapping the 2nd {@code Future}
     * @return the mapped {@code Future}s
     */
    public FutureTuple2<T0, T1> defaults(T0 v0, T1 v1) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map {@link Future}s that succeed with null to default values, and execute @{code onEmpty} before mapping.
     * <p>
     * If the {@code onEmpty} throws an exception, the returned {@code Future} will be failed with this exception.
     * Otherwise, the return value as the same as applying {@link FutureUtils#defaultWith(Future, Object)} to each
     * {@code Future}.
     *
     * @param onEmpty a side-effect
     * @param v0      the default value used for mapping the 1st {@code Future}
     * @param v1      the default value used for mapping the 2nd {@code Future}
     * @return the mapped {@code Future}s
     */
    public FutureTuple2<T0, T1> defaults(Runnable onEmpty, T0 v0, T1 v1) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map {@link Future}s that failed or succeed with null to fallback values.
     * <p>
     * It behaves as applying {@link FutureUtils#fallbackWith(Future, Object)} to each {@code Future}.
     *
     * @param v0 the fallback value used for mapping the 1st {@code Future}
     * @param v1 the fallback value used for mapping the 2nd {@code Future}
     * @return the mapped {@code Future}s
     */
    public FutureTuple2<T0, T1> fallback(T0 v0, T1 v1) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map {@link Future}s that failed or succeed with null to default values, and execute @{code onFailure}
     * / @{code onEmpty} before mapping.
     * <p>
     * If @{code onFailure} / {@code onEmpty} throws an exception,the returned {@code Future} will be failed with this
     * exception. Otherwise, the return value as the same as applying {@link FutureUtils#fallbackWith(Future, Object)}
     * to each {@code Future}.
     *
     * @param onFailure a side-effect on failure
     * @param onEmpty   a side-effect on empty
     * @param v0        the default value used for mapping the 1st {@code Future}
     * @param v1        the default value used for mapping the 2nd {@code Future}
     * @return the mapped {@code Future}s
     */
    public FutureTuple2<T0, T1> fallback(Consumer<Throwable> onFailure, Runnable onEmpty, T0 v0, T1 v1) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple2} with {@link CompositeFuture#all(Future, Future)}.
     *
     * @return the {@code CompositeFutureTuple2}
     */
    public CompositeFutureTuple2<T0, T1> all() {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple2} with {@link CompositeFuture#any(Future, Future)}.
     *
     * @return the {@code CompositeFutureTuple2}
     */
    public CompositeFutureTuple2<T0, T1> any() {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple2} with {@link CompositeFuture#join(Future, Future)}.
     *
     * @return the {@code CompositeFutureTuple2}
     */
    public CompositeFutureTuple2<T0, T1> join() {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple2}.
     *
     * @param function2 a factory function to construct {@link CompositeFuture} with two {@code Futures}
     * @return the {@code CompositeFutureTuple2}
     */
    public CompositeFutureTuple2<T0, T1> compose(BiFunction<Future<T0>, Future<T1>, CompositeFuture> function2) {
        throw new RuntimeException("unimplemented");
    }
}
