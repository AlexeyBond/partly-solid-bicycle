package io.github.alexeybond.partly_solid_bicycle.core.impl.data.dynamic;

import io.github.alexeybond.partly_solid_bicycle.core.impl.data.visitors.CopyVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidInputDataTypeException;
import org.junit.Test;

import static org.junit.Assert.*;

public class DynamicNodeTest {
    @Test public void testGetSetCopy() {
        DynamicNode node1 = new DynamicNode();

        {
            DynamicNode m = node1.addField("a");

            m.addField("a1").setBoolean(true);
            m.addField("a2").setDouble(3.14);
            m.addField("a3").setLong(3243235123L);
            m.addField("a4").setString("hello world");
        }

        {
            DynamicNode m = node1.addField("b");

            DynamicNode m1 = m.addField("b1");
            m1.addItem().setString("foo");
            m1.addItem().setString("bar");

            m.addField("b2").addField("b2a").addField("b2a1").setString("deep");
        }

        assertEquals(true, node1.getField("a").getField("a1").getBoolean());
        assertEquals(3.14, node1.getField("a").getField("a2").getDouble(), Double.MIN_VALUE);
        assertEquals((long) 3.14, node1.getField("a").getField("a2").getLong());
        assertEquals(3243235123L, node1.getField("a").getField("a3").getLong());
        assertEquals((double) 3243235123L, node1.getField("a").getField("a3").getDouble(), Double.MIN_VALUE);
        assertEquals("hello world", node1.getField("a").getField("a4").getString());

        assertEquals(2, node1.getField("b").getField("b1").getList().size());
        assertEquals("foo", node1.getField("b").getField("b1").getList().get(0).getString());
        assertEquals("bar", node1.getField("b").getField("b1").getList().get(1).getString());

        assertEquals("deep",
                node1.getField("b").getField("b2").getField("b2a").getField("b2a1").getString());

        DynamicNode node2 = new CopyVisitor().doCopy(node1, new DynamicNode());

        assertEquals(true, node2.getField("a").getField("a1").getBoolean());
        assertEquals(3.14, node2.getField("a").getField("a2").getDouble(), Double.MIN_VALUE);
        assertEquals((long) 3.14, node2.getField("a").getField("a2").getLong());
        assertEquals(3243235123L, node2.getField("a").getField("a3").getLong());
        assertEquals((double) 3243235123L, node2.getField("a").getField("a3").getDouble(), Double.MIN_VALUE);
        assertEquals("hello world", node2.getField("a").getField("a4").getString());

        assertEquals(2, node2.getField("b").getField("b1").getList().size());
        assertEquals("foo", node2.getField("b").getField("b1").getList().get(0).getString());
        assertEquals("bar", node2.getField("b").getField("b1").getList().get(1).getString());

        assertEquals("deep",
                node2.getField("b").getField("b2").getField("b2a").getField("b2a1").getString());

        assertNotSame(node1.getField("b").getField("b2").getField("b2a").getField("b2a1"),
                      node2.getField("b").getField("b2").getField("b2a").getField("b2a1"));
        assertNotSame(node1.getField("b").getField("b1").getList().get(0),
                      node2.getField("b").getField("b1").getList().get(0));
    }

    @Test public void testThrowOnUnexpectedAccess() {
        DynamicNode node = new DynamicNode();

        try {
            node.getBoolean();
            fail();
        } catch (InvalidInputDataTypeException ignore) { }

        try {
            node.getDouble();
            fail();
        } catch (InvalidInputDataTypeException ignore) { }

        try {
            node.getField("a");
            fail();
        } catch (InvalidInputDataTypeException ignore) { }

        try {
            node.getList();
            fail();
        } catch (InvalidInputDataTypeException ignore) { }

        try {
            node.getString();
            fail();
        } catch (InvalidInputDataTypeException ignore) { }
    }
}
