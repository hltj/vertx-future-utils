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

import java.util.function.BiFunction;
import java.util.function.Consumer;

import static me.hltj.vertx.FutureUtils.defaultWith;
import static me.hltj.vertx.FutureUtils.fallbackWith;
import static me.hltj.vertx.future.InternalUtil.toFailureMapper;
import static me.hltj.vertx.future.InternalUtil.toSupplier;

/**
 * A tuple of two {@link Future}s.
 *
 * @param <T0> the type parameter of the 1st {@code Future}
 * @param <T1> the type parameter of the 2nd {@code Future}
 * @since 1.0.0
 */
@SuppressWarnings("java:S116")
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
        return of(_0.mapEmpty(), _1.mapEmpty());
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
        return of(_0.otherwise(v0), _1.otherwise(v1));
    }

    /**
     * Map the failure of the {@link Future}s to specific values, and execute {@code onFailure} before mapping.
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
        return of(_0.otherwise(toFailureMapper(onFailure, v0)), _1.otherwise(toFailureMapper(onFailure, v1)));
    }

    /**
     * Map the failure of the {@link Future}s to {@code null}s.
     * <p>
     * It behaves as applying {@link Future#otherwiseEmpty()} to each {@code Future}.
     *
     * @return the mapped {@code Future}s
     */
    public FutureTuple2<T0, T1> otherwiseEmpty() {
        return of(_0.otherwiseEmpty(), _1.otherwiseEmpty());
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
        return of(defaultWith(_0, v0), defaultWith(_1, v1));
    }

    /**
     * Map {@link Future}s that succeed with null to default values, and execute {@code onEmpty} before mapping.
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
        return of(
                FutureUtils.defaultWith(_0, toSupplier(onEmpty, v0)),
                FutureUtils.defaultWith(_1, toSupplier(onEmpty, v1))
        );
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
        return of(fallbackWith(_0, v0), fallbackWith(_1, v1));
    }

    /**
     * Map {@link Future}s that failed or succeed with null to default values, and execute {@code onFailure}
     * / {@code onEmpty} before mapping.
     * <p>
     * If {@code onFailure} / {@code onEmpty} throws an exception,the returned {@code Future} will be failed with this
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
        return of(
                fallbackWith(_0, toFailureMapper(onFailure, v0), toSupplier(onEmpty, v0)),
                fallbackWith(_1, toFailureMapper(onFailure, v1), toSupplier(onEmpty, v1))
        );
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple2} with {@link CompositeFuture#all(Future, Future)}.
     *
     * @return the {@code CompositeFutureTuple2}
     */
    public CompositeFutureTuple2<T0, T1> all() {
        return compose(CompositeFuture::all);
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple2} with {@link CompositeFuture#any(Future, Future)}.
     *
     * @return the {@code CompositeFutureTuple2}
     */
    public CompositeFutureTuple2<T0, T1> any() {
        return compose(CompositeFuture::any);
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple2} with {@link CompositeFuture#join(Future, Future)}.
     *
     * @return the {@code CompositeFutureTuple2}
     */
    public CompositeFutureTuple2<T0, T1> join() {
        return compose(CompositeFuture::join);
    }

    /**
     * Composite this future tuple to a {@link CompositeFutureTuple2}.
     *
     * @param function2 a factory function to construct {@link CompositeFuture} with two {@code Futures}
     * @return the {@code CompositeFutureTuple2}
     */
    public CompositeFutureTuple2<T0, T1> compose(BiFunction<Future<T0>, Future<T1>, CompositeFuture> function2) {
        return CompositeFutureTuple2.of(this, function2.apply(_0, _1));
    }
}
