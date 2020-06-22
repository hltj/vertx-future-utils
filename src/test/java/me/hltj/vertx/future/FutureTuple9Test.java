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

import io.vertx.core.Future;
import io.vertx.core.Promise;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static me.hltj.vertx.SharedTestUtils.assertFailedWith;
import static me.hltj.vertx.SharedTestUtils.assertSucceedWith;
import static org.junit.jupiter.api.Assertions.*;

class FutureTuple9Test {

    @Test
    void basic() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');
        Future<Byte> future5 = Future.succeededFuture((byte) 1);
        Future<Float> future6 = Future.succeededFuture(1f);
        Future<Short> future7 = Future.succeededFuture((short) 1);
        Future<Integer> future8 = Future.succeededFuture(1);

        val tuple = FutureTuple9.of(future0, future1, future2, future3, future4, future5, future6, future7, future8);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
        assertSame(future2, tuple.get_2());
        assertSame(future3, tuple.get_3());
        assertSame(future4, tuple.get_4());
        assertSame(future5, tuple.get_5());
        assertSame(future6, tuple.get_6());
        assertSame(future7, tuple.get_7());
        assertSame(future8, tuple.get_8());
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

        Future<Short> future7 = Future.succeededFuture((short) 1);
        Future<Short> failedFuture7 = Future.failedFuture("fail7");

        Future<Integer> future8 = Future.succeededFuture();
        Future<Integer> failedFuture8 = Future.failedFuture("fail8");

        val tupleA = FutureTuple9.of(
                future0, future1, future2, future3, future4, future5, future6, future7, future8
        ).mapEmpty();
        assertSucceedWith(null, tupleA.get_0());
        assertSucceedWith(null, tupleA.get_1());
        assertSucceedWith(null, tupleA.get_2());
        assertSucceedWith(null, tupleA.get_3());
        assertSucceedWith(null, tupleA.get_4());
        assertSucceedWith(null, tupleA.get_5());
        assertSucceedWith(null, tupleA.get_6());
        assertSucceedWith(null, tupleA.get_7());
        assertSucceedWith(null, tupleA.get_8());

        val tupleB = FutureTuple9.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3, failedFuture4, failedFuture5, failedFuture6,
                failedFuture7, failedFuture8
        ).mapEmpty();
        assertFailedWith("fail0", tupleB.get_0());
        assertFailedWith("fail1", tupleB.get_1());
        assertFailedWith("fail2", tupleB.get_2());
        assertFailedWith("fail3", tupleB.get_3());
        assertFailedWith("fail4", tupleB.get_4());
        assertFailedWith("fail5", tupleB.get_5());
        assertFailedWith("fail6", tupleB.get_6());
        assertFailedWith("fail7", tupleB.get_7());
        assertFailedWith("fail8", tupleB.get_8());
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

        Future<Short> future7 = Future.succeededFuture((short) 1);
        Future<Short> failedFuture7 = Future.failedFuture("fail7");

        Future<Integer> future8 = Future.succeededFuture(1);
        Future<Integer> failedFuture8 = Future.failedFuture("fail8");

        val tupleA = FutureTuple9.of(future0, future1, future2, future3, future4, future5, future6, future7, future8)
                .otherwise(0, "default", false, 0.0, '\0', (byte) 0, 0f, (short) 0, 0);
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());
        assertSucceedWith('a', tupleA.get_4());
        assertSucceedWith((byte) 1, tupleA.get_5());
        assertSucceedWith(1f, tupleA.get_6());
        assertSucceedWith((short) 1, tupleA.get_7());
        assertSucceedWith(1, tupleA.get_8());

        val tupleB = FutureTuple9.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3, failedFuture4, failedFuture5, failedFuture6,
                failedFuture7, failedFuture8
        ).otherwise(0, "default", false, 0.0, '\0', (byte) 0, 0f, (short) 0, 0);
        assertSucceedWith(0, tupleB.get_0());
        assertSucceedWith("default", tupleB.get_1());
        assertSucceedWith(false, tupleB.get_2());
        assertSucceedWith(0.0, tupleB.get_3());
        assertSucceedWith('\0', tupleB.get_4());
        assertSucceedWith((byte) 0, tupleB.get_5());
        assertSucceedWith(0f, tupleB.get_6());
        assertSucceedWith((short) 0, tupleB.get_7());
        assertSucceedWith(0, tupleB.get_8());
    }

    @Test
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

        Future<Short> future7 = Future.succeededFuture((short) 1);
        Future<Short> failedFuture7 = Future.failedFuture("fail7");

        Future<Integer> future8 = Future.succeededFuture(1);
        Future<Integer> failedFuture8 = Future.failedFuture("fail8");

        val throwablesA = new ArrayList<Throwable>();
        val tupleA = FutureTuple9.of(
                future0, future1, future2, future3, future4, future5, future6, future7, future8
        ).otherwise(throwablesA::add, 0, "default", false, 0.0, '\0', (byte) 0, 0f, (short) 0, 0);
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());
        assertSucceedWith('a', tupleA.get_4());
        assertSucceedWith((byte) 1, tupleA.get_5());
        assertSucceedWith(1f, tupleA.get_6());
        assertSucceedWith((short) 1, tupleA.get_7());
        assertSucceedWith(1, tupleA.get_8());
        assertTrue(throwablesA.isEmpty());

        val throwablesB = new ArrayList<Throwable>();
        val tupleB = FutureTuple9.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3, failedFuture4, failedFuture5, failedFuture6,
                failedFuture7, failedFuture8
        ).otherwise(throwablesB::add, 0, "default", false, 0.0, '\0', (byte) 0, 0f, (short) 0, 0);
        assertSucceedWith(0, tupleB.get_0());
        assertSucceedWith("default", tupleB.get_1());
        assertSucceedWith(false, tupleB.get_2());
        assertSucceedWith(0.0, tupleB.get_3());
        assertSucceedWith('\0', tupleB.get_4());
        assertSucceedWith((byte) 0, tupleB.get_5());
        assertSucceedWith(0f, tupleB.get_6());
        assertSucceedWith((short) 0, tupleB.get_7());
        assertSucceedWith(0, tupleB.get_8());
        assertEquals(9, throwablesB.size());
        assertEquals("fail0", throwablesB.get(0).getMessage());
        assertEquals("fail1", throwablesB.get(1).getMessage());
        assertEquals("fail2", throwablesB.get(2).getMessage());
        assertEquals("fail3", throwablesB.get(3).getMessage());
        assertEquals("fail4", throwablesB.get(4).getMessage());
        assertEquals("fail5", throwablesB.get(5).getMessage());
        assertEquals("fail6", throwablesB.get(6).getMessage());
        assertEquals("fail7", throwablesB.get(7).getMessage());
        assertEquals("fail8", throwablesB.get(8).getMessage());
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

        Future<Short> future7 = Future.succeededFuture((short) 1);
        Future<Short> failedFuture7 = Future.failedFuture("fail7");

        Future<Integer> future8 = Future.succeededFuture(1);
        Future<Integer> failedFuture8 = Future.failedFuture("fail8");

        val tupleA = FutureTuple9.of(
                future0, future1, future2, future3, future4, future5, future6, future7, future8
        ).otherwiseEmpty();
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());
        assertSucceedWith('a', tupleA.get_4());
        assertSucceedWith((byte) 1, tupleA.get_5());
        assertSucceedWith(1f, tupleA.get_6());
        assertSucceedWith((short) 1, tupleA.get_7());
        assertSucceedWith(1, tupleA.get_8());

        val tupleB = FutureTuple9.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3, failedFuture4, failedFuture5, failedFuture6,
                failedFuture7, failedFuture8
        ).otherwiseEmpty();
        assertSucceedWith(null, tupleB.get_0());
        assertSucceedWith(null, tupleB.get_1());
        assertSucceedWith(null, tupleB.get_2());
        assertSucceedWith(null, tupleB.get_3());
        assertSucceedWith(null, tupleB.get_4());
        assertSucceedWith(null, tupleB.get_5());
        assertSucceedWith(null, tupleB.get_6());
        assertSucceedWith(null, tupleB.get_7());
        assertSucceedWith(null, tupleB.get_8());
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

        Future<Short> future7 = Future.succeededFuture((short) 1);
        Future<Short> emptyFuture7 = Future.succeededFuture();

        Future<Integer> future8 = Future.succeededFuture(1);
        Future<Integer> emptyFuture8 = Future.succeededFuture();

        val tupleA = FutureTuple9.of(future0, future1, future2, future3, future4, future5, future6, future7, future8)
                .defaults(0, "default", false, 0.0, '\0', (byte) 0, 0f, (short) 0, 0);
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());
        assertSucceedWith('a', tupleA.get_4());
        assertSucceedWith((byte) 1, tupleA.get_5());
        assertSucceedWith(1f, tupleA.get_6());
        assertSucceedWith((short) 1, tupleA.get_7());
        assertSucceedWith(1, tupleA.get_8());

        val tupleB = FutureTuple9.of(
                emptyFuture0, emptyFuture1, emptyFuture2, emptyFuture3, emptyFuture4, emptyFuture5, emptyFuture6,
                emptyFuture7, emptyFuture8
        ).defaults(0, "default", false, 0.0, '\0', (byte) 0, 0f, (short) 0, 0);
        assertSucceedWith(0, tupleB.get_0());
        assertSucceedWith("default", tupleB.get_1());
        assertSucceedWith(false, tupleB.get_2());
        assertSucceedWith(0.0, tupleB.get_3());
        assertSucceedWith('\0', tupleB.get_4());
        assertSucceedWith((byte) 0, tupleB.get_5());
        assertSucceedWith(0f, tupleB.get_6());
        assertSucceedWith((short) 0, tupleB.get_7());
        assertSucceedWith(0, tupleB.get_8());
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

        Future<Short> future7 = Future.succeededFuture((short) 1);
        Future<Short> emptyFuture7 = Future.succeededFuture();

        Future<Integer> future8 = Future.succeededFuture(1);
        Future<Integer> emptyFuture8 = Future.succeededFuture();

        val nullCountA = new AtomicInteger();
        val tupleA = FutureTuple9.of(future0, future1, future2, future3, future4, future5, future6, future7, future8)
                .defaults(nullCountA::incrementAndGet, 0, "default", false, 0.0, '\0', (byte) 0, 0f, (short) 0, 0);
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());
        assertSucceedWith('a', tupleA.get_4());
        assertSucceedWith((byte) 1, tupleA.get_5());
        assertSucceedWith(1f, tupleA.get_6());
        assertSucceedWith((short) 1, tupleA.get_7());
        assertSucceedWith(1, tupleA.get_8());
        assertEquals(0, nullCountA.get());

        val nullCountB = new AtomicInteger();
        val tupleB = FutureTuple9.of(
                emptyFuture0, emptyFuture1, emptyFuture2, emptyFuture3, emptyFuture4, emptyFuture5, emptyFuture6,
                emptyFuture7, emptyFuture8
        ).defaults(nullCountB::incrementAndGet, 0, "default", false, 0.0, '\0', (byte) 0, 0f, (short) 0, 0);
        assertSucceedWith(0, tupleB.get_0());
        assertSucceedWith("default", tupleB.get_1());
        assertSucceedWith(false, tupleB.get_2());
        assertSucceedWith(0.0, tupleB.get_3());
        assertSucceedWith('\0', tupleB.get_4());
        assertSucceedWith((byte) 0, tupleB.get_5());
        assertSucceedWith(0f, tupleB.get_6());
        assertSucceedWith((short) 0, tupleB.get_7());
        assertSucceedWith(0, tupleB.get_8());
        assertEquals(9, nullCountB.get());
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

        Future<Short> future7 = Future.succeededFuture((short) 1);

        Future<Integer> future8 = Future.succeededFuture(1);

        val tupleA = FutureTuple9.of(future0, future1, future2, future3, future4, future5, future6, future7, future8)
                .fallback(0, "default", false, 0.0, '\0', (byte) 0, 0f, (short) 0, 0);
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());
        assertSucceedWith('a', tupleA.get_4());
        assertSucceedWith((byte) 1, tupleA.get_5());
        assertSucceedWith(1f, tupleA.get_6());
        assertSucceedWith((short) 1, tupleA.get_7());
        assertSucceedWith(1, tupleA.get_8());

        val tupleB = FutureTuple9.of(
                failedFuture0, emptyFuture1, future2, future3, future4, future5, future6, future7, future8
        ).fallback(0, "default", false, 0.0, '\0', (byte) 0, 0f, (short) 0, 0);
        assertSucceedWith(0, tupleB.get_0());
        assertSucceedWith("default", tupleB.get_1());
        assertSucceedWith(true, tupleB.get_2());
        assertSucceedWith(1.0, tupleB.get_3());
        assertSucceedWith('a', tupleB.get_4());
        assertSucceedWith((byte) 1, tupleB.get_5());
        assertSucceedWith(1f, tupleB.get_6());
        assertSucceedWith((short) 1, tupleB.get_7());
        assertSucceedWith(1, tupleB.get_8());
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

        Future<Short> future7 = Future.succeededFuture((short) 1);

        Future<Integer> future8 = Future.succeededFuture(1);

        val nullCountA = new AtomicInteger();
        val throwablesA = new ArrayList<Throwable>();
        val tupleA = FutureTuple9.of(
                future0, future1, future2, future3, future4, future5, future6, future7, future8
        ).fallback(
                throwablesA::add, nullCountA::incrementAndGet,
                0, "default", false, 0.0, '\0', (byte) 0, 0f, (short) 0, 0
        );
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());
        assertSucceedWith('a', tupleA.get_4());
        assertSucceedWith((byte) 1, tupleA.get_5());
        assertSucceedWith(1f, tupleA.get_6());
        assertSucceedWith((short) 1, tupleA.get_7());
        assertSucceedWith(1, tupleA.get_8());
        assertEquals(0, nullCountA.get());
        assertTrue(throwablesA.isEmpty());

        val nullCountB = new AtomicInteger();
        val throwablesB = new ArrayList<Throwable>();
        val tupleB = FutureTuple9.of(
                failedFuture0, emptyFuture1, future2, future3, future4, future5, future6, future7, future8
        ).fallback(
                throwablesB::add, nullCountB::incrementAndGet,
                0, "default", false, 0.0, '\0', (byte) 0, 0f, (short) 0, 0
        );
        assertSucceedWith(0, tupleB.get_0());
        assertSucceedWith("default", tupleB.get_1());
        assertSucceedWith(true, tupleB.get_2());
        assertSucceedWith(1.0, tupleB.get_3());
        assertSucceedWith('a', tupleB.get_4());
        assertSucceedWith((byte) 1, tupleB.get_5());
        assertSucceedWith(1f, tupleB.get_6());
        assertSucceedWith((short) 1, tupleB.get_7());
        assertSucceedWith(1, tupleB.get_8());
        assertEquals(1, nullCountB.get());
        assertEquals(1, throwablesB.size());
        assertEquals("fail0", throwablesB.get(0).getMessage());
    }
}