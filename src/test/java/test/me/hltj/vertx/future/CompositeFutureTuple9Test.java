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
package test.me.hltj.vertx.future;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static me.hltj.vertx.FutureUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static test.me.hltj.vertx.SharedTestUtils.assertFailedWith;
import static test.me.hltj.vertx.SharedTestUtils.assertSucceedWith;

class CompositeFutureTuple9Test {

    @Test
    void basic() {
        Future<String> future0 = Future.succeededFuture("hello");
        Future<Integer> future1 = Promise.<Integer>promise().future();
        Future<Boolean> future2 = Future.succeededFuture();
        Future<Double> future3 = Future.succeededFuture();
        Future<Character> future4 = Future.succeededFuture();
        Future<Byte> future5 = Future.succeededFuture();
        Future<Float> future6 = Future.succeededFuture();
        Future<Short> future7 = Future.succeededFuture();
        Future<Integer> future8 = Future.succeededFuture();

        val tuple = tuple(future0, future1, future2, future3, future4, future5, future6, future7, future8);
        val composite = tuple.join();
        assertSame(tuple, composite.tuple());

        val raw = composite.raw();
        assertFalse(raw.succeeded());
        assertFalse(raw.failed());
        assertTrue(raw.succeeded(0));
        assertFalse(raw.succeeded(1));
        assertFalse(raw.failed(1));
        assertTrue(raw.succeeded(2));
        assertTrue(raw.succeeded(3));
        assertTrue(raw.succeeded(4));
        assertTrue(raw.succeeded(5));
        assertTrue(raw.succeeded(6));
        assertTrue(raw.succeeded(7));
        assertTrue(raw.succeeded(8));
    }

    @Test
    void use() {
        Promise<Double> promise0 = Promise.promise();
        Future<Integer> future1 = Future.failedFuture("error");
        Future<Boolean> future2 = Future.succeededFuture();
        Future<Double> future3 = Future.succeededFuture();
        Future<Character> future4 = Future.succeededFuture();
        Future<Byte> future5 = Future.succeededFuture();
        Future<Float> future6 = Future.succeededFuture();
        Future<Short> future7 = Future.succeededFuture();
        Future<Integer> future8 = Future.succeededFuture();

        val successStatuses = new ArrayList<Boolean>();
        val resultStrings = new ArrayList<String>();
        join(promise0.future(), future1, future2, future3, future4, future5, future6, future7, future8)
                .use((composite, fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8) -> composite.onFailure(_t -> {
                    for (int i = 0; i < composite.size(); i++) {
                        successStatuses.add(composite.succeeded(i));
                        resultStrings.add("" + composite.resultAt(i));
                    }

                    assertSucceedWith(1.0, fut0);
                    assertFailedWith("error", fut1);
                    assertSucceedWith(null, fut2);
                    assertSucceedWith(null, fut3);
                    assertSucceedWith(null, fut4);
                    assertSucceedWith(null, fut5);
                    assertSucceedWith(null, fut6);
                    assertSucceedWith(null, fut7);
                    assertSucceedWith(null, fut8);
                }));

        promise0.complete(1.0);

        assertEquals(true, successStatuses.get(0));
        assertEquals("1.0", resultStrings.get(0));

        assertEquals(false, successStatuses.get(1));
        assertEquals("null", resultStrings.get(1));

        assertEquals(true, successStatuses.get(2));
        assertEquals("null", resultStrings.get(2));

        assertEquals(true, successStatuses.get(3));
        assertEquals("null", resultStrings.get(3));

        assertEquals(true, successStatuses.get(4));
        assertEquals("null", resultStrings.get(4));

        assertEquals(true, successStatuses.get(5));
        assertEquals("null", resultStrings.get(5));

        assertEquals(true, successStatuses.get(6));
        assertEquals("null", resultStrings.get(6));

        assertEquals(true, successStatuses.get(7));
        assertEquals("null", resultStrings.get(7));

        assertEquals(true, successStatuses.get(8));
        assertEquals("null", resultStrings.get(8));
    }

    @Test
    void with() {
        Promise<Double> promise0 = Promise.promise();
        Future<Integer> future1 = Future.succeededFuture();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(2.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Short> future7 = Future.succeededFuture((short) 1);
        Future<Integer> future8 = Future.succeededFuture(3);

        Future<String> resultFuture = join(
                promise0.future(), future1, future2, future3, future4, future5, future6, future7, future8
        ).with((composite, fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8) -> composite.map(_t -> {
            assertSucceedWith(1.0, fut0);
            assertSucceedWith(null, fut1);
            assertSucceedWith(true, fut2);
            assertSucceedWith(2.0, fut3);
            assertSucceedWith('a', fut4);
            assertSucceedWith((byte) 1, fut5);
            assertSucceedWith(1f, fut6);
            assertSucceedWith((short) 1, fut7);
            assertSucceedWith(3, fut8);
            return String.format(
                    "(%s, %s, %s, %s, %s, %s, %s, %s, %s)", fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8
            );
        }));

        promise0.complete(1.0);
        assertEquals(
                "(Future{result=1.0}, Future{result=null}, Future{result=true}, Future{result=2.0}" +
                        ", Future{result=a}, Future{result=1}, Future{result=1.0}, Future{result=1}, Future{result=3})",
                resultFuture.result()
        );
    }

    @Test
    void through_mapAnyway() {
        Promise<Double> promise0 = Promise.promise();
        Promise<Integer> promise1 = Promise.promise();
        Future<Long> future2 = Future.succeededFuture(0L);
        Future<Double> future3 = Future.succeededFuture(0.0);
        Future<Character> future4 = Future.succeededFuture('\0');
        Future<Byte> future5 = Future.succeededFuture((byte) 0);
        Future<Float> future6 = Future.succeededFuture(0f);
        Future<Short> future7 = Future.succeededFuture((short) 0);
        Future<Integer> future8 = Future.succeededFuture(0);
        val composite = join(
                promise0.future(), promise1.future(), future2, future3, future4, future5, future6, future7, future8
        );

        Future<Double> sumFutureA = composite.through((fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8) ->
                fallbackWith(fut0, 0.0).result() +
                        fallbackWith(fut1, 0).result() +
                        fallbackWith(fut2, 0L).result() +
                        fallbackWith(fut3, 0.0).result() +
                        fallbackWith(fut4, '\0').result() +
                        fallbackWith(fut5, (byte) 0).result() +
                        fallbackWith(fut6, 0f).result() +
                        fallbackWith(fut7, (short) 0).result() +
                        fallbackWith(fut8, 0).result()
        );

        Future<Double> sumFutureB = composite.mapAnyway((fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8) ->
                fallbackWith(fut0, 0.0).result() +
                        fallbackWith(fut1, 0).result() +
                        fallbackWith(fut2, 0L).result() +
                        fallbackWith(fut3, 0.0).result() +
                        fallbackWith(fut4, '\0').result() +
                        fallbackWith(fut5, (byte) 0).result() +
                        fallbackWith(fut6, 0f).result() +
                        fallbackWith(fut7, (short) 0).result() +
                        fallbackWith(fut8, 0).result()
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
        Future<Long> future2 = Future.succeededFuture(0L);
        Future<Double> future3 = Future.succeededFuture(0.0);
        Future<Character> future4 = Future.succeededFuture('\0');
        Future<Byte> future5 = Future.succeededFuture((byte) 0);
        Future<Float> future6 = Future.succeededFuture(0f);
        Future<Short> future7 = Future.succeededFuture((short) 0);
        Future<Integer> future8 = Future.succeededFuture(0);
        val composite = join(
                promise0.future(), promise1.future(), future2, future3, future4, future5, future6, future7, future8
        );

        Future<Double> sumFutureA = composite.through((fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8) ->
                fut0.result() + fut1.result() + fut2.result() + fut3.result() + fut4.result() + fut5.result() +
                        fut6.result() + fut7.result() + fut8.result()
        );

        Future<Double> sumFutureB = composite.mapAnyway((fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8) ->
                fut0.result() + fut1.result() + fut2.result() + fut3.result() + fut4.result() + fut5.result() +
                        fut6.result() + fut7.result() + fut8.result()
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
        Future<Long> future2 = Future.succeededFuture(0L);
        Future<Double> future3 = Future.succeededFuture(0.0);
        Future<Character> future4 = Future.succeededFuture('\0');
        Future<Byte> future5 = Future.succeededFuture((byte) 0);
        Future<Float> future6 = Future.succeededFuture(0f);
        Future<Short> future7 = Future.succeededFuture((short) 0);
        Future<Integer> future8 = Future.succeededFuture(0);
        val composite = join(
                promise0.future(), promise1.future(), future2, future3, future4, future5, future6, future7, future8
        );

        Future<Double> sumFutureA = composite.joinThrough((fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8) ->
                wrap(() -> fallbackWith(fut0, 0.0).result() +
                        fallbackWith(fut1, 0).result() +
                        fallbackWith(fut2, 0L).result() +
                        fallbackWith(fut3, 0.0).result() +
                        fallbackWith(fut4, '\0').result() +
                        fallbackWith(fut5, (byte) 0).result() +
                        fallbackWith(fut6, 0f).result() +
                        fallbackWith(fut7, (short) 0).result() +
                        fallbackWith(fut8, 0).result()
                )
        );

        Future<Double> sumFutureB = composite.flatMapAnyway((fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8) ->
                wrap(() -> fallbackWith(fut0, 0.0).result() +
                        fallbackWith(fut1, 0).result() +
                        fallbackWith(fut2, 0L).result() +
                        fallbackWith(fut3, 0.0).result() +
                        fallbackWith(fut4, '\0').result() +
                        fallbackWith(fut5, (byte) 0).result() +
                        fallbackWith(fut6, 0f).result() +
                        fallbackWith(fut7, (short) 0).result() +
                        fallbackWith(fut8, 0).result()
                )
        );

        promise0.fail("error");
        promise1.complete(9);

        assertSucceedWith(9.0, sumFutureA);
        assertSucceedWith(9.0, sumFutureB);
    }

    @Test
    void joinThrough_flatMapAnyway_NPE() {
        Promise<Double> promise0 = Promise.promise();
        Promise<Integer> promise1 = Promise.promise();
        Future<Long> future2 = Future.succeededFuture(0L);
        Future<Double> future3 = Future.succeededFuture(0.0);
        Future<Character> future4 = Future.succeededFuture('\0');
        Future<Byte> future5 = Future.succeededFuture((byte) 0);
        Future<Float> future6 = Future.succeededFuture(0f);
        Future<Short> future7 = Future.succeededFuture((short) 0);
        Future<Integer> future8 = Future.succeededFuture(0);
        val composite = join(
                promise0.future(), promise1.future(), future2, future3, future4, future5, future6, future7, future8
        );

        Future<Double> sumFutureA = composite.joinThrough((fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8) ->
                wrap(null, (Double init) -> init +
                        fallbackWith(fut0, 0.0).result() +
                        fallbackWith(fut1, 0).result() +
                        fallbackWith(fut2, 0L).result() +
                        fallbackWith(fut3, 0.0).result() +
                        fallbackWith(fut4, '\0').result() +
                        fallbackWith(fut5, (byte) 0).result() +
                        fallbackWith(fut6, 0f).result() +
                        fallbackWith(fut7, (short) 0).result() +
                        fallbackWith(fut8, 0).result()
                )
        );

        Future<Double> sumFutureB = composite.flatMapAnyway((fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8) ->
                wrap(null, (Double init) -> init +
                        fallbackWith(fut0, 0.0).result() +
                        fallbackWith(fut1, 0).result() +
                        fallbackWith(fut2, 0L).result() +
                        fallbackWith(fut3, 0.0).result() +
                        fallbackWith(fut4, '\0').result() +
                        fallbackWith(fut5, (byte) 0).result() +
                        fallbackWith(fut6, 0f).result() +
                        fallbackWith(fut7, (short) 0).result() +
                        fallbackWith(fut8, 0).result()
                )
        );

        @SuppressWarnings("ConstantConditions")
        Future<Double> sumFutureC = composite.joinThrough((fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8) ->
                ((Future<Integer>) null).map(
                        fallbackWith(fut0, 0.0).result() +
                                fallbackWith(fut1, 0).result() +
                                fallbackWith(fut2, 0L).result() +
                                fallbackWith(fut3, 0.0).result() +
                                fallbackWith(fut4, '\0').result() +
                                fallbackWith(fut5, (byte) 0).result() +
                                fallbackWith(fut6, 0f).result() +
                                fallbackWith(fut7, (short) 0).result() +
                                fallbackWith(fut8, 0).result()
                )
        );

        @SuppressWarnings("ConstantConditions")
        Future<Double> sumFutureD = composite.flatMapAnyway((fut0, fut1, fut2, fut3, fut4, fut5, fut6, fut7, fut8) ->
                ((Future<Integer>) null).map(
                        fallbackWith(fut0, 0.0).result() +
                                fallbackWith(fut1, 0).result() +
                                fallbackWith(fut2, 0L).result() +
                                fallbackWith(fut3, 0.0).result() +
                                fallbackWith(fut4, '\0').result() +
                                fallbackWith(fut5, (byte) 0).result() +
                                fallbackWith(fut6, 0f).result() +
                                fallbackWith(fut7, (short) 0).result() +
                                fallbackWith(fut8, 0).result()
                )
        );

        promise0.fail("error");
        promise1.complete(9);

        assertFailedWith(NullPointerException.class, sumFutureA);
        assertFailedWith(NullPointerException.class, sumFutureB);
        assertFailedWith(NullPointerException.class, sumFutureC);
        assertFailedWith(NullPointerException.class, sumFutureD);
    }

    @Test
    void applift_mapTyped() {
        Promise<Double> promise0 = Promise.promise();
        Promise<Integer> promise1 = Promise.promise();
        Future<Long> future2 = Future.succeededFuture(0L);
        Future<Double> future3 = Future.succeededFuture(0.0);
        Future<Character> future4 = Future.succeededFuture('\0');
        Future<Byte> future5 = Future.succeededFuture((byte) 0);
        Future<Float> future6 = Future.succeededFuture(0f);
        Future<Short> future7 = Future.succeededFuture((short) 0);
        Future<Integer> future8 = Future.succeededFuture(0);
        val composite = tuple(
                promise0.future(), promise1.future(), future2, future3, future4, future5, future6, future7, future8
        ).fallback(0.0, 0, 0L, 0.0, '\0', (byte) 0, 0f, (short) 0, 0).all();

        Future<Double> sumFutureA = composite
                .applift((d0, i1, l2, d3, c4, b5, f6, s7, i8) -> d0 + 1.0 * i1 + l2 + d3 + c4 + b5 + f6 + s7 + i8);
        Future<Double> sumFutureB = composite
                .mapTyped((d0, i1, l2, d3, c4, b5, f6, s7, i8) -> d0 + 1.0 * i1 + l2 + d3 + c4 + b5 + f6 + s7 + i8);

        promise0.fail("error");
        promise1.complete(9);

        assertSucceedWith(9.0, sumFutureA);
        assertSucceedWith(9.0, sumFutureB);
    }

    @Test
    void applift_mapTyped_Failure() {
        Promise<Double> promise0 = Promise.promise();
        Promise<Integer> promise1 = Promise.promise();
        Future<Long> future2 = Future.succeededFuture(0L);
        Future<Double> future3 = Future.succeededFuture(0.0);
        Future<Character> future4 = Future.succeededFuture('\0');
        Future<Byte> future5 = Future.succeededFuture((byte) 0);
        Future<Float> future6 = Future.succeededFuture(0f);
        Future<Short> future7 = Future.succeededFuture((short) 0);
        Future<Integer> future8 = Future.succeededFuture(0);
        val composite = join(
                promise0.future(), promise1.future(), future2, future3, future4, future5, future6, future7, future8
        );

        Future<Double> sumFutureA = composite
                .applift((d0, i1, l2, d3, c4, b5, f6, s7, i8) -> d0 + 1.0 * i1 + l2 + d3 + c4 + b5 + f6 + s7 + i8);
        Future<Double> sumFutureB = composite
                .mapTyped((d0, i1, l2, d3, c4, b5, f6, s7, i8) -> d0 + 1.0 * i1 + l2 + d3 + c4 + b5 + f6 + s7 + i8);

        promise0.fail("error");
        promise1.complete(9);

        assertFailedWith("error", sumFutureA);
        assertFailedWith("error", sumFutureB);
    }

    @Test
    void joinApplift_flatMapTyped() {
        Promise<Double> promise0 = Promise.promise();
        Promise<Integer> promise1 = Promise.promise();
        Future<Long> future2 = Future.succeededFuture(0L);
        Future<Double> future3 = Future.succeededFuture(0.0);
        Future<Character> future4 = Future.succeededFuture('\0');
        Future<Byte> future5 = Future.succeededFuture((byte) 0);
        Future<Float> future6 = Future.succeededFuture(0f);
        Future<Short> future7 = Future.succeededFuture((short) 0);
        Future<Integer> future8 = Future.succeededFuture(0);
        val composite = tuple(
                promise0.future(), promise1.future(), future2, future3, future4, future5, future6, future7, future8
        ).fallback(0.0, 0, 0L, 0.0, '\0', (byte) 0, 0f, (short) 0, 0).all();

        Future<Double> sumFutureA = composite.joinApplift((d0, i1, l2, d3, c4, b5, f6, s7, i8) -> wrap(() ->
                d0 + 1.0 * i1 + l2 + d3 + c4 + b5 + f6 + s7 + i8
        ));
        Future<Double> sumFutureB = composite.flatMapTyped((d0, i1, l2, d3, c4, b5, f6, s7, i8) -> wrap(() ->
                d0 + 1.0 * i1 + l2 + d3 + c4 + b5 + f6 + s7 + i8
        ));

        promise0.fail("error");
        promise1.complete(9);

        assertSucceedWith(9.0, sumFutureA);
        assertSucceedWith(9.0, sumFutureB);
    }

    @Test
    void joinApplift_flatMapTyped_Failure() {
        Promise<Double> promise0 = Promise.promise();
        Promise<Integer> promise1 = Promise.promise();
        Future<Long> future2 = Future.succeededFuture(0L);
        Future<Double> future3 = Future.succeededFuture(0.0);
        Future<Character> future4 = Future.succeededFuture('\0');
        Future<Byte> future5 = Future.succeededFuture((byte) 0);
        Future<Float> future6 = Future.succeededFuture(0f);
        Future<Short> future7 = Future.succeededFuture((short) 0);
        Future<Integer> future8 = Future.succeededFuture(0);
        val compositeA = join(
                promise0.future(), promise1.future(), future2, future3, future4, future5, future6, future7, future8
        );
        val compositeC = tuple(
                promise0.future(), promise1.future(), future2, future3, future4, future5, future6, future7, future8
        ).otherwise(0.0, 0, 0L, 0.0, '\0', (byte) 0, 0f, (short) 0, 0).all();

        Future<Double> sumFutureA = compositeA.joinApplift((d0, i1, l2, d3, c4, b5, f6, s7, i8) -> wrap(() ->
                d0 + 1.0 * i1 + l2 + d3 + c4 + b5 + f6 + s7 + i8
        ));
        Future<Double> sumFutureB = compositeA.flatMapTyped((d0, i1, l2, d3, c4, b5, f6, s7, i8) -> wrap(() ->
                d0 + 1.0 * i1 + l2 + d3 + c4 + b5 + f6 + s7 + i8
        ));
        @SuppressWarnings("ConstantConditions")
        Future<Double> sumFutureC = compositeC.joinApplift((d0, i1, l2, d3, c4, b5, f6, s7, i8) ->
                ((Future<Integer>) null).map(d0 + i1 + l2 + d3 + c4 + b5 + f6 + s7 + i8)
        );
        @SuppressWarnings("ConstantConditions")
        Future<Double> sumFutureD = compositeC.flatMapTyped((d0, i1, l2, d3, c4, b5, f6, s7, i8) ->
                ((Future<Integer>) null).map(d0 + i1 + l2 + d3 + c4 + b5 + f6 + s7 + i8)
        );

        promise0.fail("error");
        promise1.complete(9);

        assertFailedWith("error", sumFutureA);
        assertFailedWith("error", sumFutureB);
        assertFailedWith(NullPointerException.class, sumFutureC);
        assertFailedWith(NullPointerException.class, sumFutureD);
    }
}