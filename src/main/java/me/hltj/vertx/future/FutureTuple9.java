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
import me.hltj.vertx.function.Function9;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static me.hltj.vertx.FutureUtils.defaultWith;
import static me.hltj.vertx.FutureUtils.fallbackWith;
import static me.hltj.vertx.future.InternalUtil.toFailureMapper;
import static me.hltj.vertx.future.InternalUtil.toSupplier;

/**
 * A tuple of 9 {@link Future}s.
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
 */
@Getter
@AllArgsConstructor(staticName = "of")
@ToString(includeFieldNames = false)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> {
    Future<T0> _0;
    Future<T1> _1;
    Future<T2> _2;
    Future<T3> _3;
    Future<T4> _4;
    Future<T5> _5;
    Future<T6> _6;
    Future<T7> _7;
    Future<T8> _8;

    /**
     * Map the result of the {@link Future}s to {@code null}s.
     * <p>
     * It likes {@link FutureTuple2#mapEmpty()} but with 9-arity.
     */
    public FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> mapEmpty() {
        return of(
                _0.mapEmpty(), _1.mapEmpty(), _2.mapEmpty(), _3.mapEmpty(), _4.mapEmpty(), _5.mapEmpty(), _6.mapEmpty(),
                _7.mapEmpty(), _8.mapEmpty()
        );
    }

    /**
     * Map the failure of the {@link Future}s to specific values.
     * <p>
     * It likes {@link FutureTuple2#otherwise(Object, Object)} (Object, Object)} but with 9-arity.
     */
    public FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> otherwise(
            T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8
    ) {
        return of(
                _0.otherwise(v0), _1.otherwise(v1), _2.otherwise(v2), _3.otherwise(v3), _4.otherwise(v4),
                _5.otherwise(v5), _6.otherwise(v6), _7.otherwise(v7), _8.otherwise(v8)
        );
    }

    /**
     * Map the failure of the {@link Future}s to specific values, and execute @{code onFailure} before mapping.
     * <p>
     * It likes {@link FutureTuple2#otherwise(Consumer, Object, Object)} but with 9-arity.
     */
    public FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> otherwise(
            Consumer<Throwable> onFailure, T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8
    ) {
        return of(
                _0.otherwise(toFailureMapper(onFailure, v0)),
                _1.otherwise(toFailureMapper(onFailure, v1)),
                _2.otherwise(toFailureMapper(onFailure, v2)),
                _3.otherwise(toFailureMapper(onFailure, v3)),
                _4.otherwise(toFailureMapper(onFailure, v4)),
                _5.otherwise(toFailureMapper(onFailure, v5)),
                _6.otherwise(toFailureMapper(onFailure, v6)),
                _7.otherwise(toFailureMapper(onFailure, v7)),
                _8.otherwise(toFailureMapper(onFailure, v8))
        );
    }

    /**
     * Map the failure of the {@link Future}s to {@code null}s.
     * <p>
     * It likes {@link FutureTuple2#otherwiseEmpty()} but with 9-arity.
     */
    public FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> otherwiseEmpty() {
        return of(
                _0.otherwiseEmpty(), _1.otherwiseEmpty(), _2.otherwiseEmpty(), _3.otherwiseEmpty(), _4.otherwiseEmpty(),
                _5.otherwiseEmpty(), _6.otherwiseEmpty(), _7.otherwiseEmpty(), _8.otherwiseEmpty()
        );
    }

    /**
     * Map {@link Future}s that succeed with null to default values.
     * <p>
     * It likes {@link FutureTuple2#defaults(Object, Object)} but with 9-arity.
     */
    public FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> defaults(
            T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8
    ) {
        return of(
                defaultWith(_0, v0), defaultWith(_1, v1), defaultWith(_2, v2), defaultWith(_3, v3), defaultWith(_4, v4),
                defaultWith(_5, v5), defaultWith(_6, v6), defaultWith(_7, v7), defaultWith(_8, v8)
        );
    }

    /**
     * Map {@link Future}s that succeed with null to default values, and execute @{code onEmpty} before mapping.
     * <p>
     * It likes {@link FutureTuple2#defaults(Runnable, Object, Object)} but with 9-arity.
     */
    public FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> defaults(
            Runnable onEmpty, T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8
    ) {
        return of(
                FutureUtils.defaultWith(_0, toSupplier(onEmpty, v0)),
                FutureUtils.defaultWith(_1, toSupplier(onEmpty, v1)),
                FutureUtils.defaultWith(_2, toSupplier(onEmpty, v2)),
                FutureUtils.defaultWith(_3, toSupplier(onEmpty, v3)),
                FutureUtils.defaultWith(_4, toSupplier(onEmpty, v4)),
                FutureUtils.defaultWith(_5, toSupplier(onEmpty, v5)),
                FutureUtils.defaultWith(_6, toSupplier(onEmpty, v6)),
                FutureUtils.defaultWith(_7, toSupplier(onEmpty, v7)),
                FutureUtils.defaultWith(_8, toSupplier(onEmpty, v8))
        );
    }

    /**
     * Map {@link Future}s that failed or succeed with null to fallback values.
     * <p>
     * It likes {@link FutureTuple2#fallback(Object, Object)} but with 9-arity.
     */
    public FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> fallback(
            T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8
    ) {
        return of(
                fallbackWith(_0, v0), fallbackWith(_1, v1), fallbackWith(_2, v2), fallbackWith(_3, v3),
                fallbackWith(_4, v4), fallbackWith(_5, v5), fallbackWith(_6, v6), fallbackWith(_7, v7),
                fallbackWith(_8, v8)
        );
    }

    /**
     * Map {@link Future}s that failed or succeed with null to default values, and execute @{code onFailure}
     * / @{code onEmpty} before mapping.
     * <p>
     * It likes {@link FutureTuple2#fallback(Consumer, Runnable, Object, Object)} but with 9-arity.
     */
    public FutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> fallback(
            Consumer<Throwable> onFailure, Runnable onEmpty,
            T0 v0, T1 v1, T2 v2, T3 v3, T4 v4, T5 v5, T6 v6, T7 v7, T8 v8
    ) {
        return of(
                fallbackWith(_0, toFailureMapper(onFailure, v0), toSupplier(onEmpty, v0)),
                fallbackWith(_1, toFailureMapper(onFailure, v1), toSupplier(onEmpty, v1)),
                fallbackWith(_2, toFailureMapper(onFailure, v2), toSupplier(onEmpty, v2)),
                fallbackWith(_3, toFailureMapper(onFailure, v3), toSupplier(onEmpty, v3)),
                fallbackWith(_4, toFailureMapper(onFailure, v4), toSupplier(onEmpty, v4)),
                fallbackWith(_5, toFailureMapper(onFailure, v5), toSupplier(onEmpty, v5)),
                fallbackWith(_6, toFailureMapper(onFailure, v6), toSupplier(onEmpty, v6)),
                fallbackWith(_7, toFailureMapper(onFailure, v7), toSupplier(onEmpty, v7)),
                fallbackWith(_8, toFailureMapper(onFailure, v8), toSupplier(onEmpty, v8))
        );
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple9} with {@link CompositeFuture#all(List)}.
     * <p>
     * It likes {@link FutureTuple2#all()} but with 9-arity.
     */
    public CompositeFutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> all() {
        return compose((v0, v1, v2, v3, v4, v5, v6, v7, v8) ->
                CompositeFuture.all(Arrays.asList(v0, v1, v2, v3, v4, v5, v6, v7, v8))
        );
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple9} with {@link CompositeFuture#any(List)}.
     * <p>
     * It likes {@link FutureTuple2#any()} but with 9-arity.
     */
    public CompositeFutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> any() {
        return compose((v0, v1, v2, v3, v4, v5, v6, v7, v8) ->
                CompositeFuture.any(Arrays.asList(v0, v1, v2, v3, v4, v5, v6, v7, v8))
        );
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple9} with {@link CompositeFuture#join(List)}.
     * <p>
     * It likes {@link FutureTuple2#join()} but with 9-arity.
     */
    public CompositeFutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> join() {
        return compose((v0, v1, v2, v3, v4, v5, v6, v7, v8) ->
                CompositeFuture.join(Arrays.asList(v0, v1, v2, v3, v4, v5, v6, v7, v8))
        );
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple9}.
     * <p>
     * It likes {@link FutureTuple2#compose(BiFunction)} but with 9-arity.
     */
    public CompositeFutureTuple9<T0, T1, T2, T3, T4, T5, T6, T7, T8> compose(
            Function9<Future<T0>, Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>,
                    Future<T8>, CompositeFuture> function9
    ) {
        return CompositeFutureTuple9.of(this, function9.apply(_0, _1, _2, _3, _4, _5, _6, _7, _8));
    }
}
