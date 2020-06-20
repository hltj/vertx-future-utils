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
import me.hltj.vertx.function.Function3;

import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * A tuple of 3 {@link Future}s.
 *
 * @param <T0> the type parameter of the 1st {@code Future}
 * @param <T1> the type parameter of the 2nd {@code Future}
 * @param <T2> the type parameter of the 3rd {@code Future}
 */
@Getter
@AllArgsConstructor(staticName = "of")
@ToString(includeFieldNames = false)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class FutureTuple3<T0, T1, T2> {
    Future<T0> _0;
    Future<T1> _1;
    Future<T2> _2;

    /**
     * Map the result of the {@link Future}s to {@code null}s.
     * <p>
     * It likes {@link FutureTuple2#mapEmpty()} but with 3-arity.
     */
    public FutureTuple3<T0, T1, T2> mapEmpty() {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map the failure of the {@link Future}s to specific values.
     * <p>
     * It likes {@link FutureTuple2#otherwise(Object, Object)} (Object, Object)} but with 3-arity.
     */
    public FutureTuple3<T0, T1, T2> otherwise(T0 v0, T1 v1, T2 v2) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map the failure of the {@link Future}s to specific values, and execute @{code onFailure} before mapping.
     * <p>
     * It likes {@link FutureTuple2#otherwise(Consumer, Object, Object)} but with 3-arity.
     */
    public FutureTuple3<T0, T1, T2> otherwise(Consumer<Throwable> onFailure, T0 v0, T1 v1, T2 v2) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map the failure of the {@link Future}s to {@code null}s.
     * <p>
     * It likes {@link FutureTuple2#otherwiseEmpty()} but with 3-arity.
     */
    public FutureTuple3<T0, T1, T2> otherwiseEmpty() {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map {@link Future}s that succeed with null to default values.
     * <p>
     * It likes {@link FutureTuple2#defaults(Object, Object)} but with 3-arity.
     */
    public FutureTuple3<T0, T1, T2> defaults(T0 v0, T1 v1, T2 v2) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map {@link Future}s that succeed with null to default values, and execute @{code onEmpty} before mapping.
     * <p>
     * It likes {@link FutureTuple2#defaults(Runnable, Object, Object)} but with 3-arity.
     */
    public FutureTuple3<T0, T1, T2> defaults(Runnable onEmpty, T0 v0, T1 v1, T2 v2) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map {@link Future}s that failed or succeed with null to fallback values.
     * <p>
     * It likes {@link FutureTuple2#fallback(Object, Object)} but with 3-arity.
     */
    public FutureTuple3<T0, T1, T2> fallback(T0 v0, T1 v1, T2 v2) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Map {@link Future}s that failed or succeed with null to default values, and execute @{code onFailure}
     * / @{code onEmpty} before mapping.
     * <p>
     * It likes {@link FutureTuple2#fallback(Consumer, Runnable, Object, Object)} but with 3-arity.
     */
    public FutureTuple3<T0, T1, T2> fallback(Consumer<Throwable> onFailure, Runnable onEmpty, T0 v0, T1 v1, T2 v2) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple3}
     * with {@link CompositeFuture#all(Future, Future, Future)}.
     * <p>
     * It likes {@link FutureTuple2#all()} but with 3-arity.
     */
    public CompositeFutureTuple3<T0, T1, T2> all() {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple3}
     * with {@link CompositeFuture#any(Future, Future, Future)}.
     * <p>
     * It likes {@link FutureTuple2#any()} but with 3-arity.
     */
    public CompositeFutureTuple3<T0, T1, T2> any() {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple3}
     * with {@link CompositeFuture#join(Future, Future, Future)}.
     * <p>
     * It likes {@link FutureTuple2#join()} but with 3-arity.
     */
    public CompositeFutureTuple3<T0, T1, T2> join() {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple3}.
     * <p>
     * It likes {@link FutureTuple2#compose(BiFunction)} but with 3-arity.
     */
    public CompositeFutureTuple3<T0, T1, T2> compose(
            Function3<Future<T0>, Future<T1>, Future<T2>, CompositeFuture> function3
    ) {
        throw new RuntimeException("unimplemented");
    }
}
