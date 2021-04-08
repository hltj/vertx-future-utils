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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import me.hltj.vertx.FutureUtils;
import me.hltj.vertx.function.Function5;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import static me.hltj.vertx.FutureUtils.defaultWith;
import static me.hltj.vertx.FutureUtils.fallbackWith;
import static me.hltj.vertx.future.InternalUtil.toFailureMapper;
import static me.hltj.vertx.future.InternalUtil.toSupplier;

/**
 * A tuple of 5 {@link Future}s.
 *
 * @param <T0> the type parameter of the 1st {@code Future}
 * @param <T1> the type parameter of the 2nd {@code Future}
 * @param <T2> the type parameter of the 3rd {@code Future}
 * @param <T3> the type parameter of the 4th {@code Future}
 * @param <T4> the type parameter of the 5th {@code Future}
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor(staticName = "of")
@ToString(includeFieldNames = false)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class FutureTuple5<T0, T1, T2, T3, T4> {
    Future<T0> _0;
    Future<T1> _1;
    Future<T2> _2;
    Future<T3> _3;
    Future<T4> _4;

    /**
     * Map the result of the {@link Future}s to {@code null}s.
     * <p>
     * It likes {@link FutureTuple2#mapEmpty()} but with 5-arity.
     */
    public FutureTuple5<T0, T1, T2, T3, T4> mapEmpty() {
        return of(_0.mapEmpty(), _1.mapEmpty(), _2.mapEmpty(), _3.mapEmpty(), _4.mapEmpty());
    }

    /**
     * Map the failure of the {@link Future}s to specific values.
     * <p>
     * It likes {@link FutureTuple2#otherwise(Object, Object)} (Object, Object)} but with 5-arity.
     */
    public FutureTuple5<T0, T1, T2, T3, T4> otherwise(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4) {
        return of(_0.otherwise(v0), _1.otherwise(v1), _2.otherwise(v2), _3.otherwise(v3), _4.otherwise(v4));
    }

    /**
     * Map the failure of the {@link Future}s to specific values, and execute {@code onFailure} before mapping.
     * <p>
     * It likes {@link FutureTuple2#otherwise(Consumer, Object, Object)} but with 5-arity.
     */
    public FutureTuple5<T0, T1, T2, T3, T4> otherwise(
            Consumer<Throwable> onFailure, T0 v0, T1 v1, T2 v2, T3 v3, T4 v4
    ) {
        return of(
                _0.otherwise(toFailureMapper(onFailure, v0)),
                _1.otherwise(toFailureMapper(onFailure, v1)),
                _2.otherwise(toFailureMapper(onFailure, v2)),
                _3.otherwise(toFailureMapper(onFailure, v3)),
                _4.otherwise(toFailureMapper(onFailure, v4))
        );
    }

    /**
     * Map the failure of the {@link Future}s to {@code null}s.
     * <p>
     * It likes {@link FutureTuple2#otherwiseEmpty()} but with 5-arity.
     */
    public FutureTuple5<T0, T1, T2, T3, T4> otherwiseEmpty() {
        return of(
                _0.otherwiseEmpty(), _1.otherwiseEmpty(), _2.otherwiseEmpty(), _3.otherwiseEmpty(), _4.otherwiseEmpty()
        );
    }

    /**
     * Map {@link Future}s that succeed with null to default values.
     * <p>
     * It likes {@link FutureTuple2#defaults(Object, Object)} but with 5-arity.
     */
    public FutureTuple5<T0, T1, T2, T3, T4> defaults(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4) {
        return of(
                defaultWith(_0, v0), defaultWith(_1, v1), defaultWith(_2, v2), defaultWith(_3, v3), defaultWith(_4, v4)
        );
    }

    /**
     * Map {@link Future}s that succeed with null to default values, and execute {@code onEmpty} before mapping.
     * <p>
     * It likes {@link FutureTuple2#defaults(Runnable, Object, Object)} but with 5-arity.
     */
    public FutureTuple5<T0, T1, T2, T3, T4> defaults(Runnable onEmpty, T0 v0, T1 v1, T2 v2, T3 v3, T4 v4) {
        return of(
                FutureUtils.defaultWith(_0, toSupplier(onEmpty, v0)),
                FutureUtils.defaultWith(_1, toSupplier(onEmpty, v1)),
                FutureUtils.defaultWith(_2, toSupplier(onEmpty, v2)),
                FutureUtils.defaultWith(_3, toSupplier(onEmpty, v3)),
                FutureUtils.defaultWith(_4, toSupplier(onEmpty, v4))
        );
    }

    /**
     * Map {@link Future}s that failed or succeed with null to fallback values.
     * <p>
     * It likes {@link FutureTuple2#fallback(Object, Object)} but with 5-arity.
     */
    public FutureTuple5<T0, T1, T2, T3, T4> fallback(T0 v0, T1 v1, T2 v2, T3 v3, T4 v4) {
        return of(
                fallbackWith(_0, v0), fallbackWith(_1, v1), fallbackWith(_2, v2), fallbackWith(_3, v3),
                fallbackWith(_4, v4)
        );
    }

    /**
     * Map {@link Future}s that failed or succeed with null to default values, and execute {@code onFailure}
     * / {@code onEmpty} before mapping.
     * <p>
     * It likes {@link FutureTuple2#fallback(Consumer, Runnable, Object, Object)} but with 5-arity.
     */
    public FutureTuple5<T0, T1, T2, T3, T4> fallback(
            Consumer<Throwable> onFailure, Runnable onEmpty, T0 v0, T1 v1, T2 v2, T3 v3, T4 v4
    ) {
        return of(
                fallbackWith(_0, toFailureMapper(onFailure, v0), toSupplier(onEmpty, v0)),
                fallbackWith(_1, toFailureMapper(onFailure, v1), toSupplier(onEmpty, v1)),
                fallbackWith(_2, toFailureMapper(onFailure, v2), toSupplier(onEmpty, v2)),
                fallbackWith(_3, toFailureMapper(onFailure, v3), toSupplier(onEmpty, v3)),
                fallbackWith(_4, toFailureMapper(onFailure, v4), toSupplier(onEmpty, v4))
        );
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple5}
     * with {@link CompositeFuture#all(Future, Future, Future, Future, Future)}.
     * <p>
     * It likes {@link FutureTuple2#all()} but with 5-arity.
     */
    public CompositeFutureTuple5<T0, T1, T2, T3, T4> all() {
        return compose(CompositeFuture::all);
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple5}
     * with {@link CompositeFuture#any(Future, Future, Future, Future, Future)}.
     * <p>
     * It likes {@link FutureTuple2#any()} but with 5-arity.
     */
    public CompositeFutureTuple5<T0, T1, T2, T3, T4> any() {
        return compose(CompositeFuture::any);
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple5}
     * with {@link CompositeFuture#join(Future, Future, Future, Future, Future)}.
     * <p>
     * It likes {@link FutureTuple2#join()} but with 5-arity.
     */
    public CompositeFutureTuple5<T0, T1, T2, T3, T4> join() {
        return compose(CompositeFuture::join);
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple5}.
     * <p>
     * It likes {@link FutureTuple2#compose(BiFunction)} but with 5-arity.
     */
    public CompositeFutureTuple5<T0, T1, T2, T3, T4> compose(
            Function5<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, CompositeFuture> function5
    ) {
        return CompositeFutureTuple5.of(this, function5.apply(_0, _1, _2, _3, _4));
    }
}
