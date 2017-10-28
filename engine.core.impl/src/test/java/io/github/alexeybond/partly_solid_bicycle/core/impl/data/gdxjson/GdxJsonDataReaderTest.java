package io.github.alexeybond.partly_solid_bicycle.core.impl.data.gdxjson;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GdxJsonDataReaderTest {
    @Test public void testParse() {
        InputDataObject dataObject = new GdxJsonDataReader().parseData(
                "" +
                    "{" +
                        "a : [{b: foo, c : 3.14}, {d : true, e: 2}]" +
                        "}"
        );

        assertEquals("foo", dataObject.getField("a").getList().get(0).getField("b").getString());
        assertEquals(true, dataObject.getField("a").getList().get(1).getField("d").getBoolean());
        assertEquals(3.14, dataObject.getField("a").getList().get(0).getField("c").getDouble(), Double.MIN_VALUE);
        assertEquals(3L, dataObject.getField("a").getList().get(0).getField("c").getLong());
        assertEquals(2L, dataObject.getField("a").getList().get(1).getField("e").getLong());
        assertEquals(2.0, dataObject.getField("a").getList().get(1).getField("e").getDouble(), Double.MIN_VALUE);
    }

    @Test(expected = RuntimeException.class)
    public void testMultiRootException() {
        new GdxJsonDataReader().parseData("{}{}");
    }
}
