package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.Component1$_impl;
import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.Component1_saver;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.dynamic.DynamicNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.helpers.matching.DataMatcher;
import io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.test_utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

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

        Component1_saver
                .RESOLVER
                .resolve(cmp)
                .save(cmp, data);

        DataMatcher.assertMatch(
                data,
                TestUtils.parseJSON("{x: 32, y: 48, z: 13}")
        );
    }
}
