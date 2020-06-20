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
import me.hltj.vertx.future.FutureTuple4;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static test.me.hltj.vertx.SharedTestUtils.assertFailedWith;
import static test.me.hltj.vertx.SharedTestUtils.assertSucceedWith;

class FutureTuple4Test {

    @Test
    void basic() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();
        Future<Boolean> future2 = Future.succeededFuture(true);
        Future<Double> future3 = Future.succeededFuture(1.0);

        val tuple = FutureTuple4.of(future0, future1, future2, future3);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
        assertSame(future2, tuple.get_2());
        assertSame(future3, tuple.get_3());
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

        val tupleA = FutureTuple4.of(future0, future1, future2, future3).mapEmpty();
        assertSucceedWith(null, tupleA.get_0());
        assertSucceedWith(null, tupleA.get_1());
        assertSucceedWith(null, tupleA.get_2());
        assertSucceedWith(null, tupleA.get_3());

        val tupleB = FutureTuple4.of(failedFuture0, failedFuture1, failedFuture2, failedFuture3).mapEmpty();
        assertFailedWith("fail0", tupleB.get_0());
        assertFailedWith("fail1", tupleB.get_1());
        assertFailedWith("fail2", tupleB.get_2());
        assertFailedWith("fail3", tupleB.get_3());
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

        val tupleA = FutureTuple4.of(future0, future1, future2, future3).otherwise(0, "default", false, 0.0);
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());

        val tupleB = FutureTuple4.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3
        ).otherwise(0, "default", false, 0.0);
        assertSucceedWith(0, tupleB.get_0());
        assertSucceedWith("default", tupleB.get_1());
        assertSucceedWith(false, tupleB.get_2());
        assertSucceedWith(0.0, tupleB.get_3());
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

        val throwablesA = new ArrayList<Throwable>();
        val tupleA = FutureTuple4.of(
                future0, future1, future2, future3
        ).otherwise(throwablesA::add, 0, "default", false, 0.0);
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());
        assertTrue(throwablesA.isEmpty());

        val throwablesB = new ArrayList<Throwable>();
        val tupleB = FutureTuple4.of(
                failedFuture0, failedFuture1, failedFuture2, failedFuture3
        ).otherwise(throwablesB::add, 0, "default", false, 0.0);
        assertSucceedWith(0, tupleB.get_0());
        assertSucceedWith("default", tupleB.get_1());
        assertSucceedWith(false, tupleB.get_2());
        assertSucceedWith(0.0, tupleB.get_3());
        assertEquals(4, throwablesB.size());
        assertEquals("fail0", throwablesB.get(0).getMessage());
        assertEquals("fail1", throwablesB.get(1).getMessage());
        assertEquals("fail2", throwablesB.get(2).getMessage());
        assertEquals("fail3", throwablesB.get(3).getMessage());
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

        val tupleA = FutureTuple4.of(future0, future1, future2, future3).otherwiseEmpty();
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());

        val tupleB = FutureTuple4.of(failedFuture0, failedFuture1, failedFuture2, failedFuture3).otherwiseEmpty();
        assertSucceedWith(null, tupleB.get_0());
        assertSucceedWith(null, tupleB.get_1());
        assertSucceedWith(null, tupleB.get_2());
        assertSucceedWith(null, tupleB.get_3());
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

        val tupleA = FutureTuple4.of(future0, future1, future2, future3).defaults(0, "default", false, 0.0);
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());

        val tupleB = FutureTuple4.of(
                emptyFuture0, emptyFuture1, emptyFuture2, emptyFuture3
        ).defaults(0, "default", false, 0.0);
        assertSucceedWith(0, tupleB.get_0());
        assertSucceedWith("default", tupleB.get_1());
        assertSucceedWith(false, tupleB.get_2());
        assertSucceedWith(0.0, tupleB.get_3());
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

        val nullCountA = new AtomicInteger();
        val tupleA = FutureTuple4.of(future0, future1, future2, future3)
                .defaults(nullCountA::incrementAndGet, 0, "default", false, 0.0);
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());
        assertEquals(0, nullCountA.get());

        val nullCountB = new AtomicInteger();
        val tupleB = FutureTuple4.of(emptyFuture0, emptyFuture1, emptyFuture2, emptyFuture3)
                .defaults(nullCountB::incrementAndGet, 0, "default", false, 0.0);
        assertSucceedWith(0, tupleB.get_0());
        assertSucceedWith("default", tupleB.get_1());
        assertSucceedWith(false, tupleB.get_2());
        assertSucceedWith(0.0, tupleB.get_3());
        assertEquals(4, nullCountB.get());
    }

    @Test
    void fallback() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> emptyFuture1 = Future.succeededFuture();

        Future<Boolean> future2 = Future.succeededFuture(true);

        Future<Double> future3 = Future.succeededFuture(1.0);

        val tupleA = FutureTuple4.of(future0, future1, future2, future3).fallback(0, "default", false, 0.0);
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());

        val tupleB = FutureTuple4.of(failedFuture0, emptyFuture1, future2, future3).fallback(0, "default", false, 0.0);
        assertSucceedWith(0, tupleB.get_0());
        assertSucceedWith("default", tupleB.get_1());
        assertSucceedWith(true, tupleB.get_2());
        assertSucceedWith(1.0, tupleB.get_3());
    }

    @Test
    void fallback_withEffect() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> emptyFuture1 = Future.succeededFuture();

        Future<Boolean> future2 = Future.succeededFuture(true);

        Future<Double> future3 = Future.succeededFuture(1.0);

        val nullCountA = new AtomicInteger();
        val throwablesA = new ArrayList<Throwable>();
        val tupleA = FutureTuple4.of(future0, future1, future2, future3)
                .fallback(throwablesA::add, nullCountA::incrementAndGet, 0, "default", false, 0.0);
        assertSucceedWith(1, tupleA.get_0());
        assertSucceedWith("hello", tupleA.get_1());
        assertSucceedWith(true, tupleA.get_2());
        assertSucceedWith(1.0, tupleA.get_3());
        assertEquals(0, nullCountA.get());
        assertTrue(throwablesA.isEmpty());

        val nullCountB = new AtomicInteger();
        val throwablesB = new ArrayList<Throwable>();
        val tupleB = FutureTuple4.of(failedFuture0, emptyFuture1, future2, future3)
                .fallback(throwablesB::add, nullCountB::incrementAndGet, 0, "default", false, 0.0);
        assertSucceedWith(0, tupleB.get_0());
        assertSucceedWith("default", tupleB.get_1());
        assertSucceedWith(true, tupleB.get_2());
        assertSucceedWith(1.0, tupleB.get_3());
        assertEquals(1, nullCountB.get());
        assertEquals(1, throwablesB.size());
        assertEquals("fail0", throwablesB.get(0).getMessage());
    }
}