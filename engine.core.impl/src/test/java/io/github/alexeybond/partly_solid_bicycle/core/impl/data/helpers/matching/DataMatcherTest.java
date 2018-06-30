package io.github.alexeybond.partly_solid_bicycle.core.impl.data.helpers.matching;

import io.github.alexeybond.partly_solid_bicycle.core.impl.test_utils.TestUtils;
import org.junit.Test;

public class DataMatcherTest {
    @Test
    public void testMatch() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("{key: value}"),
                TestUtils.parseJSON("{}")
        );
        DataMatcher.assertMatch(
                TestUtils.parseJSON("{key: value}"),
                TestUtils.parseJSON("{key: value}")
        );
        DataMatcher.assertMatch(
                TestUtils.parseJSON("123"),
                TestUtils.parseJSON("123.0")
        );
        DataMatcher.assertMatch(
                TestUtils.parseJSON("123.0"),
                TestUtils.parseJSON("123")
        );
        DataMatcher.assertMatch(
                TestUtils.parseJSON("true"),
                TestUtils.parseJSON("true")
        );
        DataMatcher.assertMatch(
                TestUtils.parseJSON("[1,2,3]"),
                TestUtils.parseJSON("[1,2,3]")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchLong() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("12"),
                TestUtils.parseJSON("42")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchDouble() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("666.0"),
                TestUtils.parseJSON("42.0")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchString() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("bye world"),
                TestUtils.parseJSON("hello world")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchBoolean() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("false"),
                TestUtils.parseJSON("true")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchArrayLength() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("[1]"),
                TestUtils.parseJSON("[1,2]")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchArrayLength2() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("[1,2]"),
                TestUtils.parseJSON("[1]")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchArray() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("[1]"),
                TestUtils.parseJSON("[2]")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchTypeArray() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("2"),
                TestUtils.parseJSON("[2]")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchTypeObject() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("2"),
                TestUtils.parseJSON("{value: 2}")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchObject() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("{}"),
                TestUtils.parseJSON("{value: 2}")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchObject2() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("{value: 1}"),
                TestUtils.parseJSON("{value: 2}")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchTypeLong() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("\"232123\""),
                TestUtils.parseJSON("232123")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchTypeDouble() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("\"232123.32\""),
                TestUtils.parseJSON("232123.32")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchTypeBoolean() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("\"true\""),
                TestUtils.parseJSON("true")
        );
    }

    @Test(expected = AssertionError.class)
    public void testMismatchTypeString() {
        DataMatcher.assertMatch(
                TestUtils.parseJSON("true"),
                TestUtils.parseJSON("\"true\"")
        );
    }
}
