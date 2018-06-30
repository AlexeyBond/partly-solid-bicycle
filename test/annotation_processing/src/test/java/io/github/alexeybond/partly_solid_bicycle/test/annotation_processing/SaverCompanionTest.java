package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.*;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.dynamic.DynamicNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.helpers.matching.DataMatcher;
import io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.test_utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class SaverCompanionTest {
    private DynamicNode data;

    @Before
    public void setUp() {
        data = new DynamicNode();
    }

    @Test
    public void testClassname() {
        Component1$_impl cmp = new Component1$_impl();

        Component1_saver
                .RESOLVER
                .resolve(cmp)
                .save(cmp, data);

        DataMatcher.assertMatch(
                data,
                TestUtils.parseJSON("{class: component-1}")
        );
    }

    @Test
    public void testPrimitives() {
        Component1$_impl cmp = new Component1$_impl();

        cmp.x = 32;
        cmp.y = 48;
        cmp.z = 13;

        cmp.name = "Vasya";

        cmp.data = TestUtils.parseJSON("{__holy__:cow}");

        Component1_saver
                .RESOLVER
                .resolve(cmp)
                .save(cmp, data);

        DataMatcher.assertMatch(
                data,
                TestUtils.parseJSON("{x: 32, y: 48, z: 13, name: Vasya, data: {__holy__:cow}}")
        );
    }

    @Test
    public void testLists() {
        Component2$_impl cmp = new Component2$_impl();

        cmp.multilist = Arrays.asList(
                Arrays.asList("hello", "world"),
                Arrays.asList("hail", "Satan", "!")
        );

        Component2_saver.RESOLVER.resolve(cmp).save(cmp, data);

        DataMatcher.assertMatch(
                data,
                TestUtils.parseJSON("{multilist:[[hello,world],[hail,Satan,!]]}")
        );
    }

    @Test
    public void testArrays() {
        Component3$_impl cmp = new Component3$_impl();

        cmp.floats = new float[]{3.125f, 42, 21};
        cmp.ints = new int[][][]{{{1, 2, 3}, {4}}, {{5}}, {{42}}};

        Component3_saver.RESOLVER.resolve(cmp).save(cmp, data);

        DataMatcher.assertMatch(
                data,
                TestUtils.parseJSON("{floats:[3.125,42,21],ints:[[[1,2,3],[4]],[[5]],[[42]]]}")
        );
    }
}
