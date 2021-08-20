package me.hltj.vertx.function;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FunctionsTest {

    @Test
    void consumer3() {
        val results = new ArrayList<String>();
        Consumer3<String, Boolean, Integer> consumer3 = (s, x, i) ->
                results.add(String.format("(%s, %s, %d)", s, x, i));

        consumer3.andThen(consumer3).accept("hello", true, 1);
        assertEquals(2, results.size());
        assertEquals("(hello, true, 1)", results.get(0));
        assertEquals("(hello, true, 1)", results.get(1));
    }

    @Test
    void consumer4() {
        val results = new ArrayList<String>();
        Consumer4<String, Boolean, Integer, Double> consumer4 = (s, x, i, d) ->
                results.add(String.format("(%s, %s, %d, %1.1f)", s, x, i, d));

        consumer4.andThen(consumer4).accept("hello", true, 1, 2.0);
        assertEquals(2, results.size());
        assertEquals("(hello, true, 1, 2.0)", results.get(0));
        assertEquals("(hello, true, 1, 2.0)", results.get(1));
    }

    @Test
    void consumer5() {
        val results = new ArrayList<String>();
        Consumer5<String, Boolean, Integer, Double, Byte> consumer5 = (s, x, i, d, b) ->
                results.add(String.format("(%s, %s, %d, %1.1f, %d)", s, x, i, d, b));

        consumer5.andThen(consumer5).accept("hello", true, 1, 2.0, (byte) 3);
        assertEquals(2, results.size());
        assertEquals("(hello, true, 1, 2.0, 3)", results.get(0));
        assertEquals("(hello, true, 1, 2.0, 3)", results.get(1));
    }

    @Test
    void consumer6() {
        val results = new ArrayList<String>();
        Consumer6<String, Boolean, Integer, Double, Byte, Float> consumer6 = (s, x, i, d, b, f) ->
                results.add(String.format("(%s, %s, %d, %1.1f, %d, %1.1f)", s, x, i, d, b, f));

        consumer6.andThen(consumer6).accept("hello", true, 1, 2.0, (byte) 3, 4f);
        assertEquals(2, results.size());
        assertEquals("(hello, true, 1, 2.0, 3, 4.0)", results.get(0));
        assertEquals("(hello, true, 1, 2.0, 3, 4.0)", results.get(1));
    }

    @Test
    void consumer7() {
        val results = new ArrayList<String>();
        Consumer7<String, Boolean, Integer, Double, Byte, Float, Long> consumer7 = (s, x, i, d, b, f, l) ->
                results.add(String.format("(%s, %s, %d, %1.1f, %d, %1.1f, %d)", s, x, i, d, b, f, l));

        consumer7.andThen(consumer7).accept("hello", true, 1, 2.0, (byte) 3, 4f, 5L);
        assertEquals(2, results.size());
        assertEquals("(hello, true, 1, 2.0, 3, 4.0, 5)", results.get(0));
        assertEquals("(hello, true, 1, 2.0, 3, 4.0, 5)", results.get(1));
    }

    @Test
    void consumer8() {
        val results = new ArrayList<String>();
        Consumer8<String, Boolean, Integer, Double, Byte, Float, Long, Short> consumer8 = (s, x, i, d, b, f, l, x1) ->
                results.add(String.format("(%s, %s, %d, %1.1f, %d, %1.1f, %d, %d)", s, x, i, d, b, f, l, x1));

        consumer8.andThen(consumer8).accept("hello", true, 1, 2.0, (byte) 3, 4f, 5L, (short) 6);
        assertEquals(2, results.size());
        assertEquals("(hello, true, 1, 2.0, 3, 4.0, 5, 6)", results.get(0));
        assertEquals("(hello, true, 1, 2.0, 3, 4.0, 5, 6)", results.get(1));
    }

    @Test
    void consumer9() {
        val results = new ArrayList<String>();
        Consumer9<String, Boolean, Integer, Double, Byte, Float, Long, Short, Character> consumer9 =
                (s, x, i, d, b, f, l, x1, c) ->
                        results.add(String.format("(%s, %s, %d, %1.1f, %d, %1.1f, %d, %d, %c)", s, x, i, d, b, f, l, x1, c));

        consumer9.andThen(consumer9).accept("hello", true, 1, 2.0, (byte) 3, 4f, 5L, (short) 6, 'a');
        assertEquals(2, results.size());
        assertEquals("(hello, true, 1, 2.0, 3, 4.0, 5, 6, a)", results.get(0));
        assertEquals("(hello, true, 1, 2.0, 3, 4.0, 5, 6, a)", results.get(1));
    }

    @Test
    void consumer10() {
        val results = new ArrayList<String>();
        Consumer10<String, Boolean, Integer, Double, Byte, Float, Long, Short, Character, Object> consumer10 =
                (s, x, i, d, b, f, l, x1, c, o) -> results.add(
                        String.format("(%s, %s, %d, %1.1f, %d, %1.1f, %d, %d, %c, %s)", s, x, i, d, b, f, l, x1, c, o)
                );

        consumer10.andThen(consumer10).accept("hello", true, 1, 2.0, (byte) 3, 4f, 5L, (short) 6, 'a', null);
        assertEquals(2, results.size());
        assertEquals("(hello, true, 1, 2.0, 3, 4.0, 5, 6, a, null)", results.get(0));
        assertEquals("(hello, true, 1, 2.0, 3, 4.0, 5, 6, a, null)", results.get(1));
    }

    @Test
    void function10() {
        Function10<String, Boolean, Integer, Double, Byte, Float, Long, Short, Character, Object, String> function10 =
                (s, x, i, d, b, f, l, x1, c, o) -> String.format(
                        "(%s, %s, %d, %1.1f, %d, %1.1f, %d, %d, %c, %s)", s, x, i, d, b, f, l, x1, c, o
                );

        val length = function10.andThen(String::length)
                .apply("hello", true, 1, 2.0, (byte) 3, 4f, 5L, (short) 6, 'a', null);
        assertNotNull(length);
        assertEquals(44, length.intValue());
    }
}