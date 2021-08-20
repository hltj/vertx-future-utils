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

import io.vertx.core.Future;
import io.vertx.core.Promise;
import lombok.val;
import me.hltj.vertx.SharedTestUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class FutureTuple7Test {

    @Test
    void basic() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Float> future6 = Future.succeededFuture(1f);

        val tuple = FutureTuple7.of(future0, future1, future2, future3, future4, future5, future6);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
        assertSame(future2, tuple.get_2());
        assertSame(future3, tuple.get_3());
        assertSame(future4, tuple.get_4());
        assertSame(future5, tuple.get_5());
        assertSame(future6, tuple.get_6());

        assertEquals(
                "FutureTuple7(Future{result=null}, Future{unresolved}, Future{result=true}, " +
                        "Future{result=1.0}, Future{result=a}, Future{result=1}, Future{result=1.0})",
                tuple.toString()
        );
    }

    @Test
    void mapEmpty() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> failedFuture1 = Future.failedFuture("fail1");

        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Boolean> failedFuture2 = Future.failedFuture("fail2");

        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Double> failedFuture3 = Future.failedFuture("fail3");

        Future<Character> future4 = Future.succeededFuture('a');
        Future<Character> failedFuture4 = Future.failedFuture("fail4");

        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Byte> failedFuture5 = Future.failedFuture("fail5");

        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Float> failedFuture6 = Future.failedFuture("fail6");

        val tupleA = FutureTuple7.of(future0, future1, future2, future3, future4, future5, future6).mapEmpty();
        SharedTestUtils.assertSucceedWith(null, tupleA.get_0());
        SharedTestUtils.assertSucceedWith(null, tupleA.get_1());
        SharedTestUtils.assertSucceedWith(null, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(null, tupleA.get_3());
        SharedTestUtils.assertSucceedWith(null, tupleA.get_4());
        SharedTestUtils.assertSucceedWith(null, tupleA.get_5());
        SharedTestUtils.assertSucceedWith(null, tupleA.get_6());

        val tupleB = FutureTuple7.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3, failedFuture4, failedFuture5, failedFuture6
        ).mapEmpty();
        SharedTestUtils.assertFailedWith("fail0", tupleB.get_0());
        SharedTestUtils.assertFailedWith("fail1", tupleB.get_1());
        SharedTestUtils.assertFailedWith("fail2", tupleB.get_2());
        SharedTestUtils.assertFailedWith("fail3", tupleB.get_3());
        SharedTestUtils.assertFailedWith("fail4", tupleB.get_4());
        SharedTestUtils.assertFailedWith("fail5", tupleB.get_5());
        SharedTestUtils.assertFailedWith("fail6", tupleB.get_6());
    }

    @Test
    void otherwise() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> failedFuture1 = Future.failedFuture("fail1");

        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Boolean> failedFuture2 = Future.failedFuture("fail2");

        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Double> failedFuture3 = Future.failedFuture("fail3");

        Future<Character> future4 = Future.succeededFuture('a');
        Future<Character> failedFuture4 = Future.failedFuture("fail4");

        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Byte> failedFuture5 = Future.failedFuture("fail5");

        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Float> failedFuture6 = Future.failedFuture("fail6");

        val tupleA = FutureTuple7.of(future0, future1, future2, future3, future4, future5, future6)
                .otherwise(0, "default", false, 0.0, '\0', (byte) 0, 0f);
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());
        SharedTestUtils.assertSucceedWith((byte) 1, tupleA.get_5());
        SharedTestUtils.assertSucceedWith(1f, tupleA.get_6());

        val tupleB = FutureTuple7.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3, failedFuture4, failedFuture5, failedFuture6
        ).otherwise(0, "default", false, 0.0, '\0', (byte) 0, 0f);
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        SharedTestUtils.assertSucceedWith(false, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(0.0, tupleB.get_3());
        SharedTestUtils.assertSucceedWith('\0', tupleB.get_4());
        SharedTestUtils.assertSucceedWith((byte) 0, tupleB.get_5());
        SharedTestUtils.assertSucceedWith(0f, tupleB.get_6());
    }

    @Test
    @SuppressWarnings("java:S5961")
    void otherwise_withEffect() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> failedFuture1 = Future.failedFuture("fail1");

        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Boolean> failedFuture2 = Future.failedFuture("fail2");

        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Double> failedFuture3 = Future.failedFuture("fail3");

        Future<Character> future4 = Future.succeededFuture('a');
        Future<Character> failedFuture4 = Future.failedFuture("fail4");

        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Byte> failedFuture5 = Future.failedFuture("fail5");

        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Float> failedFuture6 = Future.failedFuture("fail6");

        val throwablesA = new ArrayList<Throwable>();
        val tupleA = FutureTuple7.of(
                future0, future1, future2, future3, future4, future5, future6
        ).otherwise(throwablesA::add, 0, "default", false, 0.0, '\0', (byte) 0, 0f);
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());
        SharedTestUtils.assertSucceedWith((byte) 1, tupleA.get_5());
        SharedTestUtils.assertSucceedWith(1f, tupleA.get_6());
        assertTrue(throwablesA.isEmpty());

        val throwablesB = new ArrayList<Throwable>();
        val tupleB = FutureTuple7.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3, failedFuture4, failedFuture5, failedFuture6
        ).otherwise(throwablesB::add, 0, "default", false, 0.0, '\0', (byte) 0, 0f);
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        SharedTestUtils.assertSucceedWith(false, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(0.0, tupleB.get_3());
        SharedTestUtils.assertSucceedWith('\0', tupleB.get_4());
        SharedTestUtils.assertSucceedWith((byte) 0, tupleB.get_5());
        SharedTestUtils.assertSucceedWith(0f, tupleB.get_6());
        assertEquals(7, throwablesB.size());
        assertEquals("fail0", throwablesB.get(0).getMessage());
        assertEquals("fail1", throwablesB.get(1).getMessage());
        assertEquals("fail2", throwablesB.get(2).getMessage());
        assertEquals("fail3", throwablesB.get(3).getMessage());
        assertEquals("fail4", throwablesB.get(4).getMessage());
        assertEquals("fail5", throwablesB.get(5).getMessage());
        assertEquals("fail6", throwablesB.get(6).getMessage());
    }

    @Test
    void otherwiseEmpty() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> failedFuture1 = Future.failedFuture("fail1");

        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Boolean> failedFuture2 = Future.failedFuture("fail2");

        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Double> failedFuture3 = Future.failedFuture("fail3");

        Future<Character> future4 = Future.succeededFuture('a');
        Future<Character> failedFuture4 = Future.failedFuture("fail4");

        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Byte> failedFuture5 = Future.failedFuture("fail5");

        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Float> failedFuture6 = Future.failedFuture("fail6");

        val tupleA = FutureTuple7.of(future0, future1, future2, future3, future4, future5, future6).otherwiseEmpty();
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());
        SharedTestUtils.assertSucceedWith((byte) 1, tupleA.get_5());
        SharedTestUtils.assertSucceedWith(1f, tupleA.get_6());

        val tupleB = FutureTuple7.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3, failedFuture4, failedFuture5, failedFuture6
        ).otherwiseEmpty();
        SharedTestUtils.assertSucceedWith(null, tupleB.get_0());
        SharedTestUtils.assertSucceedWith(null, tupleB.get_1());
        SharedTestUtils.assertSucceedWith(null, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(null, tupleB.get_3());
        SharedTestUtils.assertSucceedWith(null, tupleB.get_4());
        SharedTestUtils.assertSucceedWith(null, tupleB.get_5());
        SharedTestUtils.assertSucceedWith(null, tupleB.get_6());
    }

    @Test
    void defaults() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> emptyFuture0 = Future.succeededFuture();

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> emptyFuture1 = Future.succeededFuture();

        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Boolean> emptyFuture2 = Future.succeededFuture();

        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Double> emptyFuture3 = Future.succeededFuture();

        Future<Character> future4 = Future.succeededFuture('a');
        Future<Character> emptyFuture4 = Future.succeededFuture();

        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Byte> emptyFuture5 = Future.succeededFuture();

        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Float> emptyFuture6 = Future.succeededFuture();

        val tupleA = FutureTuple7.of(future0, future1, future2, future3, future4, future5, future6)
                .defaults(0, "default", false, 0.0, '\0', (byte) 0, 0f);
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());
        SharedTestUtils.assertSucceedWith((byte) 1, tupleA.get_5());
        SharedTestUtils.assertSucceedWith(1f, tupleA.get_6());

        val tupleB = FutureTuple7.of(
                emptyFuture0, emptyFuture1, emptyFuture2, emptyFuture3, emptyFuture4, emptyFuture5, emptyFuture6
        ).defaults(0, "default", false, 0.0, '\0', (byte) 0, 0f);
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        SharedTestUtils.assertSucceedWith(false, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(0.0, tupleB.get_3());
        SharedTestUtils.assertSucceedWith('\0', tupleB.get_4());
        SharedTestUtils.assertSucceedWith((byte) 0, tupleB.get_5());
        SharedTestUtils.assertSucceedWith(0f, tupleB.get_6());
    }

    @Test
    void defaults_withEffect() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> emptyFuture0 = Future.succeededFuture();

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> emptyFuture1 = Future.succeededFuture();

        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Boolean> emptyFuture2 = Future.succeededFuture();

        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Double> emptyFuture3 = Future.succeededFuture();

        Future<Character> future4 = Future.succeededFuture('a');
        Future<Character> emptyFuture4 = Future.succeededFuture();

        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Byte> emptyFuture5 = Future.succeededFuture();

        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Float> emptyFuture6 = Future.succeededFuture();

        val nullCountA = new AtomicInteger();
        val tupleA = FutureTuple7.of(future0, future1, future2, future3, future4, future5, future6)
                .defaults(nullCountA::incrementAndGet, 0, "default", false, 0.0, '\0', (byte) 0, 0f);
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());
        SharedTestUtils.assertSucceedWith((byte) 1, tupleA.get_5());
        SharedTestUtils.assertSucceedWith(1f, tupleA.get_6());
        assertEquals(0, nullCountA.get());

        val nullCountB = new AtomicInteger();
        val tupleB = FutureTuple7.of(
                emptyFuture0, emptyFuture1, emptyFuture2, emptyFuture3, emptyFuture4, emptyFuture5, emptyFuture6
        ).defaults(nullCountB::incrementAndGet, 0, "default", false, 0.0, '\0', (byte) 0, 0f);
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        SharedTestUtils.assertSucceedWith(false, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(0.0, tupleB.get_3());
        SharedTestUtils.assertSucceedWith('\0', tupleB.get_4());
        SharedTestUtils.assertSucceedWith((byte) 0, tupleB.get_5());
        SharedTestUtils.assertSucceedWith(0f, tupleB.get_6());
        assertEquals(7, nullCountB.get());
    }

    @Test
    void fallback() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> emptyFuture1 = Future.succeededFuture();

        Future<Boolean> future2 = Future.succeededFuture(true);

        Future<Double> future3 = Future.succeededFuture(1.0);

        Future<Character> future4 = Future.succeededFuture('a');

        Future<Byte> future5 = Future.succeededFuture((byte) 1);

        Future<Float> future6 = Future.succeededFuture(1f);

        val tupleA = FutureTuple7.of(future0, future1, future2, future3, future4, future5, future6)
                .fallback(0, "default", false, 0.0, '\0', (byte) 0, 0f);
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());
        SharedTestUtils.assertSucceedWith((byte) 1, tupleA.get_5());
        SharedTestUtils.assertSucceedWith(1f, tupleA.get_6());

        val tupleB = FutureTuple7.of(failedFuture0, emptyFuture1, future2, future3, future4, future5, future6)
                .fallback(0, "default", false, 0.0, '\0', (byte) 0, 0f);
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleB.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleB.get_4());
        SharedTestUtils.assertSucceedWith((byte) 1, tupleB.get_5());
        SharedTestUtils.assertSucceedWith(1f, tupleB.get_6());
    }

    @Test
    void fallback_withEffect() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> emptyFuture1 = Future.succeededFuture();

        Future<Boolean> future2 = Future.succeededFuture(true);

        Future<Double> future3 = Future.succeededFuture(1.0);

        Future<Character> future4 = Future.succeededFuture('a');

        Future<Byte> future5 = Future.succeededFuture((byte) 1);

        Future<Float> future6 = Future.succeededFuture(1f);

        val nullCountA = new AtomicInteger();
        val throwablesA = new ArrayList<Throwable>();
        val tupleA = FutureTuple7.of(future0, future1, future2, future3, future4, future5, future6)
                .fallback(throwablesA::add, nullCountA::incrementAndGet, 0, "default", false, 0.0, '\0', (byte) 0, 0f);
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());
        SharedTestUtils.assertSucceedWith((byte) 1, tupleA.get_5());
        SharedTestUtils.assertSucceedWith(1f, tupleA.get_6());
        assertEquals(0, nullCountA.get());
        assertTrue(throwablesA.isEmpty());

        val nullCountB = new AtomicInteger();
        val throwablesB = new ArrayList<Throwable>();
        val tupleB = FutureTuple7.of(failedFuture0, emptyFuture1, future2, future3, future4, future5, future6)
                .fallback(throwablesB::add, nullCountB::incrementAndGet, 0, "default", false, 0.0, '\0', (byte) 0, 0f);
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleB.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleB.get_4());
        SharedTestUtils.assertSucceedWith((byte) 1, tupleB.get_5());
        SharedTestUtils.assertSucceedWith(1f, tupleB.get_6());
        assertEquals(1, nullCountB.get());
        assertEquals(1, throwablesB.size());
        assertEquals("fail0", throwablesB.get(0).getMessage());
    }
}