package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import generated.io.github.alexeybond.partly_solid_bicycle.test.annotation_processing.*;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.dynamic.DynamicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

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

    @Test
    public void doTestNestedLists() {
        DynamicNode dataObject = new DynamicNode();
        DynamicNode lst1 = dataObject.addField("multilist");
        DynamicNode lst2 = lst1.addItem();
        lst2.addItem().setString("foo");
        lst2.addItem().setString("bar");
        lst2 = lst1.addItem();
        lst2.addItem().setString("baz");

        Component2 component = new Component2();

        Component2_loader.RESOLVER.resolve(component).load(component, dataObject);

        assertEquals(
                Arrays.asList(
                        Arrays.asList("foo", "bar"),
                        Arrays.asList("baz")
                ),
                component.multilist
        );
    }

    @Test
    public void doTestArrays() {
        /*{floats: [42,21,10.5], ints:[[[10,20],[100,200]]]}*/
        DynamicNode dataObject = new DynamicNode();
        DynamicNode lst = dataObject.addField("floats");
        lst.addItem().setDouble(42);
        lst.addItem().setDouble(21);
        lst.addItem().setDouble(10.5);
        lst = dataObject.addField("ints");
        DynamicNode lst1 = lst.addItem();
        DynamicNode lst2 = lst1.addItem();
        lst2.addItem().setLong(10);
        lst2.addItem().setLong(20);
        lst2 = lst1.addItem();
        lst2.addItem().setLong(100);
        lst2.addItem().setLong(200);

        Component3 component = new Component3();

        Component3_loader.RESOLVER.resolve(component).load(component, dataObject);

        assertArrayEquals(new float[]{42, 21, 10.5f}, component.floats, Float.MIN_VALUE);
        assertArrayEquals(new int[][][]{{{10, 20}, {100, 200}}}, component.ints);
    }

    @Test
    public void doTestSuperclassFields() {
        DynamicNode dataObject = new DynamicNode();
        dataObject.addField("s1").setString("foo");
        dataObject.addField("s2").setString("bar");
        dataObject.addField("s3").setString("baz");

        Component5 component = new Component5();

        Component5_loader.RESOLVER.resolve(component).load(component, dataObject);

        assertEquals("foo", component.s1);
        assertEquals("bar", component.s2);
        assertEquals("baz", component.s3);
    }

    @Test
    public void doTestAccessorMethods() {
        DynamicNode dataObject = new DynamicNode();
        dataObject.addField("content").setString("1234");

        Component6 component = new Component6();

        String fieldInit = component.content;

        Component6_loader.RESOLVER.resolve(component).load(component, dataObject);

        assertEquals("1", component.getA());
        assertEquals("234", component.getB());
        assertSame(fieldInit, component.content); // loader MUST NOT change field value if a setter is present!
    }

    @Test
    public void doTestKotlinComponent() {
        DynamicNode dataObject = new DynamicNode();
        dataObject.addField("prop1").setString("hello, kotlin");
        dataObject.addField("prop2").addItem().setString("hello");

        Component7 component = new Component7();

        Component7_loader.RESOLVER.resolve(component).load(component, dataObject);

        assertEquals("hello, kotlin", component.getProp1());
        assertEquals(Collections.singletonList("hello"), component.getProp2());
    }
}
