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
 * Please contact me (jiaywe#at#gmail.com, replace the '#at#' with 'at')
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

class FutureTuple5Test {

    @Test
    void basic() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);
        Future<Character> future4 = Future.succeededFuture('a');

        val tuple = FutureTuple5.of(future0, future1, future2, future3, future4);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
        assertSame(future2, tuple.get_2());
        assertSame(future3, tuple.get_3());
        assertSame(future4, tuple.get_4());
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

        val tupleA = FutureTuple5.of(future0, future1, future2, future3, future4).mapEmpty();
        SharedTestUtils.assertSucceedWith(null, tupleA.get_0());
        SharedTestUtils.assertSucceedWith(null, tupleA.get_1());
        SharedTestUtils.assertSucceedWith(null, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(null, tupleA.get_3());
        SharedTestUtils.assertSucceedWith(null, tupleA.get_4());

        val tupleB = FutureTuple5.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3, failedFuture4
        ).mapEmpty();
        SharedTestUtils.assertFailedWith("fail0", tupleB.get_0());
        SharedTestUtils.assertFailedWith("fail1", tupleB.get_1());
        SharedTestUtils.assertFailedWith("fail2", tupleB.get_2());
        SharedTestUtils.assertFailedWith("fail3", tupleB.get_3());
        SharedTestUtils.assertFailedWith("fail4", tupleB.get_4());
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

        val tupleA = FutureTuple5.of(future0, future1, future2, future3, future4)
                .otherwise(0, "default", false, 0.0, '\0');
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());

        val tupleB = FutureTuple5.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3, failedFuture4
        ).otherwise(0, "default", false, 0.0, '\0');
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        SharedTestUtils.assertSucceedWith(false, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(0.0, tupleB.get_3());
        SharedTestUtils.assertSucceedWith('\0', tupleB.get_4());
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

        val throwablesA = new ArrayList<Throwable>();
        val tupleA = FutureTuple5.of(
                future0, future1, future2, future3, future4
        ).otherwise(throwablesA::add, 0, "default", false, 0.0, '\0');
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());
        assertTrue(throwablesA.isEmpty());

        val throwablesB = new ArrayList<Throwable>();
        val tupleB = FutureTuple5.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3, failedFuture4
        ).otherwise(throwablesB::add, 0, "default", false, 0.0, '\0');
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        SharedTestUtils.assertSucceedWith(false, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(0.0, tupleB.get_3());
        SharedTestUtils.assertSucceedWith('\0', tupleB.get_4());
        assertEquals(5, throwablesB.size());
        assertEquals("fail0", throwablesB.get(0).getMessage());
        assertEquals("fail1", throwablesB.get(1).getMessage());
        assertEquals("fail2", throwablesB.get(2).getMessage());
        assertEquals("fail3", throwablesB.get(3).getMessage());
        assertEquals("fail4", throwablesB.get(4).getMessage());
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

        val tupleA = FutureTuple5.of(future0, future1, future2, future3, future4).otherwiseEmpty();
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());

        val tupleB = FutureTuple5.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3, failedFuture4
        ).otherwiseEmpty();
        SharedTestUtils.assertSucceedWith(null, tupleB.get_0());
        SharedTestUtils.assertSucceedWith(null, tupleB.get_1());
        SharedTestUtils.assertSucceedWith(null, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(null, tupleB.get_3());
        SharedTestUtils.assertSucceedWith(null, tupleB.get_4());
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

        val tupleA = FutureTuple5.of(future0, future1, future2, future3, future4)
                .defaults(0, "default", false, 0.0, '\0');
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());

        val tupleB = FutureTuple5.of(
                emptyFuture0, emptyFuture1, emptyFuture2, emptyFuture3, emptyFuture4
        ).defaults(0, "default", false, 0.0, '\0');
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        SharedTestUtils.assertSucceedWith(false, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(0.0, tupleB.get_3());
        SharedTestUtils.assertSucceedWith('\0', tupleB.get_4());
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

        val nullCountA = new AtomicInteger();
        val tupleA = FutureTuple5.of(future0, future1, future2, future3, future4)
                .defaults(nullCountA::incrementAndGet, 0, "default", false, 0.0, '\0');
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());
        assertEquals(0, nullCountA.get());

        val nullCountB = new AtomicInteger();
        val tupleB = FutureTuple5.of(emptyFuture0, emptyFuture1, emptyFuture2, emptyFuture3, emptyFuture4)
                .defaults(nullCountB::incrementAndGet, 0, "default", false, 0.0, '\0');
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        SharedTestUtils.assertSucceedWith(false, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(0.0, tupleB.get_3());
        SharedTestUtils.assertSucceedWith('\0', tupleB.get_4());
        assertEquals(5, nullCountB.get());
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

        val tupleA = FutureTuple5.of(future0, future1, future2, future3, future4)
                .fallback(0, "default", false, 0.0, '\0');
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());

        val tupleB = FutureTuple5.of(failedFuture0, emptyFuture1, future2, future3, future4)
                .fallback(0, "default", false, 0.0, '\0');
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleB.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleB.get_4());
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

        val nullCountA = new AtomicInteger();
        val throwablesA = new ArrayList<Throwable>();
        val tupleA = FutureTuple5.of(future0, future1, future2, future3, future4)
                .fallback(throwablesA::add, nullCountA::incrementAndGet, 0, "default", false, 0.0, '\0');
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleA.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleA.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleA.get_4());
        assertEquals(0, nullCountA.get());
        assertTrue(throwablesA.isEmpty());

        val nullCountB = new AtomicInteger();
        val throwablesB = new ArrayList<Throwable>();
        val tupleB = FutureTuple5.of(failedFuture0, emptyFuture1, future2, future3, future4)
                .fallback(throwablesB::add, nullCountB::incrementAndGet, 0, "default", false, 0.0, '\0');
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        SharedTestUtils.assertSucceedWith(true, tupleB.get_2());
        SharedTestUtils.assertSucceedWith(1.0, tupleB.get_3());
        SharedTestUtils.assertSucceedWith('a', tupleB.get_4());
        assertEquals(1, nullCountB.get());
        assertEquals(1, throwablesB.size());
        assertEquals("fail0", throwablesB.get(0).getMessage());
    }
}