package io.github.alexeybond.partly_solid_bicycle.core.impl.data.gdxjson;

import com.badlogic.gdx.utils.JsonWriter;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.dynamic.DynamicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class GdxJsonWriterTest {
    @Test public void testSerialization() throws IOException {
        DynamicNode node = new DynamicNode(), node1;

        node1 = node.addField("a");
        node1.addItem().setLong(42);
        node1.addItem().setBoolean(false);
        node1.addItem().addField("b").setDouble(1.0);
        node1.addItem().addField("c").setString("bazz");

        StringWriter sw = new StringWriter();
        JsonWriter jw = new JsonWriter(sw);
        new GdxJsonWriter().doWrite(jw, node);
        String res = sw.toString();

        InputDataObject parsed = new GdxJsonDataReader().parseData(res);

        assertEquals(4, parsed.getField("a").getList().size());
        assertEquals(42L, parsed.getField("a").getList().get(0).getLong());
        assertEquals(false, parsed.getField("a").getList().get(1).getBoolean());
        assertEquals(1.0, parsed.getField("a").getList().get(2).getField("b").getDouble(), Double.MIN_VALUE);
        assertEquals("bazz", parsed.getField("a").getList().get(3).getField("c").getString());
    }
}
