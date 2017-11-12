package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.Component1_loader;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.dynamic.DynamicNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoaderCompanionTest {
    @Test public void doTest() {
        DynamicNode dataObject = new DynamicNode();
        dataObject.addField("x").setDouble(1.0);
        dataObject.addField("y").setDouble(2.0);
        dataObject.addField("z").setDouble(42.);
        dataObject.addField("name").setString("Metafoobar");

        Component1 component = new Component1();

        new Component1_loader().load(component, dataObject);

        assertEquals(1.0f, component.x, Float.MIN_VALUE);
        assertEquals(2.0f, component.y, Float.MIN_VALUE);
        assertEquals(42.0f, component.z, Float.MIN_VALUE);
        assertEquals("Metafoobar", component.name);
    }
}
