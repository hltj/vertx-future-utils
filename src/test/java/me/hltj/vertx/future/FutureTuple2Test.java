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

class FutureTuple2Test {

    @Test
    void basic() {
        Future<String> future0 = Future.succeededFuture();
        Future<Integer> future1 = Promise.<Integer>promise().future();

        val tuple = FutureTuple2.of(future0, future1);
        assertSame(future0, tuple.get_0());
        assertSame(future1, tuple.get_1());
    }

    @Test
    void mapEmpty() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> failedFuture1 = Future.failedFuture("fail1");

        val tupleA = FutureTuple2.of(future0, future1).mapEmpty();
        SharedTestUtils.assertSucceedWith(null, tupleA.get_0());
        SharedTestUtils.assertSucceedWith(null, tupleA.get_1());

        val tupleB = FutureTuple2.of(failedFuture0, failedFuture1).mapEmpty();
        SharedTestUtils.assertFailedWith("fail0", tupleB.get_0());
        SharedTestUtils.assertFailedWith("fail1", tupleB.get_1());
    }

    @Test
    void otherwise() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> failedFuture1 = Future.failedFuture("fail1");

        val tupleA = FutureTuple2.of(future0, future1).otherwise(0, "default");
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());

        val tupleB = FutureTuple2.of(failedFuture0, failedFuture1).otherwise(0, "default");
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
    }

    @Test
    void otherwise_withEffect() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> failedFuture1 = Future.failedFuture("fail1");

        val throwablesA = new ArrayList<Throwable>();
        val tupleA = FutureTuple2.of(future0, future1).otherwise(throwablesA::add, 0, "default");
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        assertTrue(throwablesA.isEmpty());

        val throwablesB = new ArrayList<Throwable>();
        val tupleB = FutureTuple2.of(failedFuture0, failedFuture1).otherwise(throwablesB::add, 0, "default");
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        assertEquals(2, throwablesB.size());
        assertEquals("fail0", throwablesB.get(0).getMessage());
        assertEquals("fail1", throwablesB.get(1).getMessage());
    }

    @Test
    void otherwiseEmpty() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> failedFuture1 = Future.failedFuture("fail1");

        val tupleA = FutureTuple2.of(future0, future1).otherwiseEmpty();
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());

        val tupleB = FutureTuple2.of(failedFuture0, failedFuture1).otherwiseEmpty();
        SharedTestUtils.assertSucceedWith(null, tupleB.get_0());
        SharedTestUtils.assertSucceedWith(null, tupleB.get_1());
    }

    @Test
    void defaults() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> emptyFuture0 = Future.succeededFuture();

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> emptyFuture1 = Future.succeededFuture();

        val tupleA = FutureTuple2.of(future0, future1).defaults(0, "default");
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());

        val tupleB = FutureTuple2.of(emptyFuture0, emptyFuture1).defaults(0, "default");
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
    }

    @Test
    void defaults_withEffect() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> emptyFuture0 = Future.succeededFuture();

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> emptyFuture1 = Future.succeededFuture();

        val nullCountA = new AtomicInteger();
        val tupleA = FutureTuple2.of(future0, future1).defaults(nullCountA::incrementAndGet, 0, "default");
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        assertEquals(0, nullCountA.get());

        val nullCountB = new AtomicInteger();
        val tupleB = FutureTuple2.of(emptyFuture0, emptyFuture1).defaults(nullCountB::incrementAndGet, 0, "default");
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        assertEquals(2, nullCountB.get());
    }

    @Test
    void fallback() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> emptyFuture1 = Future.succeededFuture();

        val tupleA = FutureTuple2.of(future0, future1).fallback(0, "default");
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());

        val tupleB = FutureTuple2.of(failedFuture0, emptyFuture1).fallback(0, "default");
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
    }

    @Test
    void fallback_withEffect() {
        Future<Integer> future0 = Future.succeededFuture(1);
        Future<Integer> failedFuture0 = Future.failedFuture("fail0");

        Future<String> future1 = Future.succeededFuture("hello");
        Future<String> emptyFuture1 = Future.succeededFuture();

        val nullCountA = new AtomicInteger();
        val throwablesA = new ArrayList<Throwable>();
        val tupleA = FutureTuple2.of(future0, future1)
                .fallback(throwablesA::add, nullCountA::incrementAndGet, 0, "default");
        SharedTestUtils.assertSucceedWith(1, tupleA.get_0());
        SharedTestUtils.assertSucceedWith("hello", tupleA.get_1());
        assertEquals(0, nullCountA.get());
        assertTrue(throwablesA.isEmpty());

        val nullCountB = new AtomicInteger();
        val throwablesB = new ArrayList<Throwable>();
        val tupleB = FutureTuple2.of(failedFuture0, emptyFuture1)
                .fallback(throwablesB::add, nullCountB::incrementAndGet, 0, "default");
        SharedTestUtils.assertSucceedWith(0, tupleB.get_0());
        SharedTestUtils.assertSucceedWith("default", tupleB.get_1());
        assertEquals(1, nullCountB.get());
        assertEquals(1, throwablesB.size());
        assertEquals("fail0", throwablesB.get(0).getMessage());
    }
}