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
import io.vertx.core.Promise;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static me.hltj.vertx.FutureUtils.wrap;
import static me.hltj.vertx.SharedTestUtils.assertFailedWith;
import static me.hltj.vertx.SharedTestUtils.assertSucceedWith;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CompositeFutureWrapperTest {

    @Test
    void raw() {
        val composite = CompositeFuture.all(Future.succeededFuture(1), Future.succeededFuture("hello"));
        val wrapper = CompositeFutureWrapper.of(composite);
        assertEquals(composite, wrapper.raw());
    }

    @Test
    void use() {
        Promise<Double> promise0 = Promise.promise();
        Future<Integer> future1 = Future.failedFuture("error");

        val successStatuses = new ArrayList<Boolean>();
        val resultStrings = new ArrayList<String>();
        CompositeFutureWrapper.of(CompositeFuture.join(promise0.future(), future1)).use(composite ->
                composite.onFailure(_t -> {
                    for (int i = 0; i < composite.size(); i++) {
                        successStatuses.add(composite.succeeded(i));
                        resultStrings.add("" + composite.resultAt(i));
                    }
                })
        );

        promise0.complete(1.0);

        assertEquals(true, successStatuses.get(0));
        assertEquals("1.0", resultStrings.get(0));

        assertEquals(false, successStatuses.get(1));
        assertEquals("null", resultStrings.get(1));
    }

    @Test
    void through_mapAnyway() {
        Promise<Double> promise0 = Promise.promise();
        Promise<Integer> promise1 = Promise.promise();
        val wrapper = CompositeFutureWrapper.of(CompositeFuture.join(promise0.future(), promise1.future()));

        Future<Double> sumFutureA = wrapper.through(composite ->
                (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
                        (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
        );

        Future<Double> sumFutureB = wrapper.mapAnyway(composite ->
                (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
                        (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
        );

        promise0.fail("error");
        promise1.complete(9);

        assertSucceedWith(9.0, sumFutureA);
        assertSucceedWith(9.0, sumFutureB);
    }

    @Test
    void through_mapAnyway_NPE() {
        Promise<Double> promise0 = Promise.promise();
        Promise<Integer> promise1 = Promise.promise();
        val wrapper = CompositeFutureWrapper.of(CompositeFuture.join(promise0.future(), promise1.future()));

        Future<Double> sumFutureA = wrapper.through(composite ->
                (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
                        (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
        );

        Future<Double> sumFutureB = wrapper.mapAnyway(composite ->
                (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
                        (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
        );

        promise0.complete();
        promise1.complete(9);

        assertFailedWith(NullPointerException.class, sumFutureA);
        assertFailedWith(NullPointerException.class, sumFutureB);
    }

    @Test
    void joinThrough_flatMapAnyway() {
        Promise<Double> promise0 = Promise.promise();
        Promise<Integer> promise1 = Promise.promise();
        val wrapper = CompositeFutureWrapper.of(CompositeFuture.join(promise0.future(), promise1.future()));

        Future<Double> sumFutureA = wrapper.joinThrough(composite -> wrap(() ->
                (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
                        (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
        ));

        Future<Double> sumFutureB = wrapper.flatMapAnyway(composite -> wrap(() ->
                (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
                        (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
        ));

        promise0.fail("error");
        promise1.complete(9);

        assertSucceedWith(9.0, sumFutureA);
        assertSucceedWith(9.0, sumFutureB);
    }

    @Test
    void joinThrough_flatMapAnyway_npe() {
        Promise<Double> promise0 = Promise.promise();
        Promise<Integer> promise1 = Promise.promise();
        val wrapper = CompositeFutureWrapper.of(CompositeFuture.join(promise0.future(), promise1.future()));

        Future<Double> sumFutureA = wrapper.joinThrough(composite -> wrap(null, (Double init) -> init +
                (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
                (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
        ));

        Future<Double> sumFutureB = wrapper.flatMapAnyway(composite -> wrap(null, (Double init) -> init +
                (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
                (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
        ));

        @SuppressWarnings("ConstantConditions")
        Future<Double> sumFutureC = wrapper.joinThrough(composite -> ((Future<Integer>) null).map(
                (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
                        (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
        ));

        @SuppressWarnings("ConstantConditions")
        Future<Double> sumFutureD = wrapper.flatMapAnyway(composite -> ((Future<Integer>) null).map(
                (composite.succeeded(0) ? composite.<Double>resultAt(0) : 0.0) +
                        (composite.succeeded(1) ? composite.<Integer>resultAt(1) : 0)
        ));

        promise0.fail("error");
        promise1.complete(9);

        assertFailedWith(NullPointerException.class, sumFutureA);
        assertFailedWith(NullPointerException.class, sumFutureB);
        assertFailedWith(NullPointerException.class, sumFutureC);
        assertFailedWith(NullPointerException.class, sumFutureD);
    }
}