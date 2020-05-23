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
import io.vertx.core.Handler;
import lombok.AllArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A {@link CompositeFuture} wrapper that provide some convenient operations as a complement.
 */
@AllArgsConstructor
public class CompositeFutureWrapper {
    private final CompositeFuture composite;

    /**
     * Create a {@link CompositeFutureWrapper} based on a {@link CompositeFuture}.
     */
    public static CompositeFutureWrapper of(CompositeFuture composite) {
        return new CompositeFutureWrapper(composite);
    }

    /**
     * Return the original {@link CompositeFuture}.
     */
    public CompositeFuture raw() {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Run side-effect code with the original {@link CompositeFuture}.
     * <p>
     * A useful scenario is to capture the {@code CompositeFuture} itself in the functions that passed as parameters
     * of {@code CompositeFuture}'s methods. e.g. :
     *
     * <pre>
     *     Future&lt;Double&gt; future0 = Future.succeededFuture(1.0);
     *     Future&lt;Integer&gt; future1 = Future.failedFuture("error");
     *     CompositeFutureExt.of(CompositeFuture.join(future0, future1)).use(composite -> composite.onFailure(_t -> {
     *         for (int i = 0; i < composite.size(); i++) {
     *             System.out.println("Future " + i + " " + (composite.succeeded(i) ? "succeed" : "failed"));
     *             System.out.println("Future " + i + "result: " + composite.resultAt(i));
     *         }
     *     }));
     * </pre>
     *
     * @param consumer the side-effect code that takes the original {@code CompositeFuture} as parameter
     */
    public void use(Consumer<CompositeFuture> consumer) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Alias for {@link #through(Function)}.
     */
    public <R> Future<R> mapAnyway(Function<CompositeFuture, R> function) {
        return through(function);
    }

    /**
     * Map a {@code function} to the original {@link CompositeFuture} on complete no matter whether succeed or failed.
     * <p>
     * It likes a {@code Future} returned through {@link CompositeFuture#onComplete(Handler)}, this is also the origin
     * of the name.
     * <p>
     * The {@code function} will be called on complete. If the function throws a non-checked exception, the returned
     * future will be failed with this exception. Otherwise, the returned future will be a future succeed with
     * the {@code function} return value.
     *
     * @param function the function to map on complete
     * @param <R>      the result type of the {@code function}
     * @return the result {@code Future}
     */
    public <R> Future<R> through(Function<CompositeFuture, R> function) {
        throw new RuntimeException("unimplemented");
    }

    /**
     * Alias for {@link #joinThrough(Function)}.
     */
    public <R> Future<R> flatMapAnyway(Function<CompositeFuture, Future<R>> function) {
        return joinThrough(function);
    }

    /**
     * Map a {@code function} to the original {@link CompositeFuture} on complete no matter whether succeed or failed,
     * the {@code function} itself return {@code Future}, the result will be join (also known as {@code flatten})
     * before return.
     * <p>
     * It behaves as {@link #through(Function)} and then {@code join}, this is also the origin of the name.
     * <p>
     * The {@code function} will be called on complete. If the function throws a non-checked exception, the returned
     * future will be failed with this exception. Otherwise, the returned future will be exactly what
     * the {@code function} returned.
     *
     * @param function the function to map on complete
     * @param <R>      the type parameter of the result {@code Future}
     * @return the result {@code Future}
     */
    public <R> Future<R> joinThrough(Function<CompositeFuture, Future<R>> function) {
        throw new RuntimeException("unimplemented");
    }
}
