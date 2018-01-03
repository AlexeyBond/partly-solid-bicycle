package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.Component1_loader;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.dynamic.DynamicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class LoaderCompanionTest {
    @Test public void doTest() {
        DynamicNode dataObject = new DynamicNode();
        dataObject.addField("x").setDouble(1.0);
        dataObject.addField("y").setDouble(2.0);
        dataObject.addField("z").setDouble(42.);
        dataObject.addField("name").setString("Metafoobar");
        dataObject.addField("data");

        Component1 component = new Component1();

        Component1_loader.RESOLVER.resolve(component).load(component, dataObject);

        assertEquals(1.0f, component.x, Float.MIN_VALUE);
        assertEquals(2.0f, component.y, Float.MIN_VALUE);
        assertEquals(42.0f, component.z, Float.MIN_VALUE);
        assertEquals("Metafoobar", component.name);
        assertSame(dataObject.getField("data"), component.data);
    }

    @Test(expected = UndefinedFieldException.class)
    public void doTestMissingFields() {
        DynamicNode dataObject = new DynamicNode();
        dataObject.addField("name").setString("buz");

        Component1 component = new Component1();

        Component1_loader.RESOLVER.resolve(component).load(component, dataObject);
    }

    @Test
    public void doTestOptionalFields() {
        DynamicNode dataObject = new DynamicNode();
        dataObject.addField("x").setDouble(1.0);
        dataObject.addField("y").setDouble(2.0);
        dataObject.addField("z").setDouble(42.);

        Component1 component = new Component1();

        Component1_loader.RESOLVER.resolve(component).load(component, dataObject);

        assertEquals(1.0f, component.x, Float.MIN_VALUE);
        assertEquals(2.0f, component.y, Float.MIN_VALUE);
        assertEquals(42.0f, component.z, Float.MIN_VALUE);
        assertEquals("bar", component.name);
        assertSame(null, component.data);
    }
}
