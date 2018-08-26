package io.github.alexeybond.partly_solid_bicycle.core.impl.data.template;

import io.github.alexeybond.partly_solid_bicycle.core.impl.data.helpers.matching.DataMatcher;
import io.github.alexeybond.partly_solid_bicycle.core.impl.test_utils.TestUtils;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SimpleTemplateDataObjectTest {
    private static final InputDataObject template = TestUtils.parseJSON("{" +
            "    a: b," +
            "    c: { $set: arg_c }," +
            "    d: {" +
            "        d1: { $set: arg_d1 }" +
            "    }," +
            "    e: [{ $set: e1}]" +
            "}");
    private static final InputDataObject arguments = TestUtils.parseJSON("{" +
            "    arg_c: foo," +
            "    arg_d1: [bar, 2]," +
            "    e1: { baz: 3.14 }" +
            "}");
    private static final InputDataObject expected = TestUtils.parseJSON("{" +
            "    a: b," +
            "    c: foo," +
            "    d: {" +
            "        d1: [bar, 2]" +
            "    }," +
            "    e: [{baz: 3.14}]" +
            "}");

    @Test
    public void testRenderedFields() {
        InputDataObject rendered = new SimpleTemplateDataObject(template, arguments);

        DataMatcher.assertMatch(rendered, expected);
    }

    @Test
    public void testVisitRendered() {
        InputDataObject rendered = new SimpleTemplateDataObject(template, arguments);
        DataObjectVisitor visitor = mock(DataObjectVisitor.class);

        rendered.accept(visitor);

        verify(visitor, times(1)).beginVisitObject();

        verify(visitor).visitField(eq("a"), TestUtils.matchingData("\"b\""));
        verify(visitor).visitField(eq("c"), TestUtils.matchingData("\"foo\""));
        verify(visitor).visitField(eq("d"), TestUtils.matchingData("{d1:[bar,2]}"));
        verify(visitor).visitField(eq("e"), TestUtils.matchingData("[{baz: 3.14}]"));

        verify(visitor, times(1)).endVisitObject();

        verifyNoMoreInteractions(visitor);

        reset(visitor);

        rendered.getField("e").accept(visitor);

        verify(visitor).beginVisitArray();
        verify(visitor).visitItem(TestUtils.matchingData("{baz: 3.14}"));
        verify(visitor).endVisitArray();

        verifyNoMoreInteractions(visitor);
    }
}
