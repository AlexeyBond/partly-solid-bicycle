package io.github.alexeybond.partly_solid_bicycle.core.impl.data;

import io.github.alexeybond.partly_solid_bicycle.core.impl.test_utils.TestUtils;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidInputDataTypeException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CompositeInputDataObjectTest {
    @Test
    public void testFieldAccess() {
        InputDataObject composite = new CompositeInputDataObject(
                TestUtils.parseJSON("{fieldA:1,fieldB:2}"),
                TestUtils.parseJSON("{fieldB:3,fieldC:4}")
        );

        assertEquals(1, composite.getField("fieldA").getLong());
        assertEquals(2, composite.getField("fieldB").getLong());
        assertEquals(4, composite.getField("fieldC").getLong());
        try {
            composite.getField("fieldD");
            fail();
        } catch (UndefinedFieldException e) {
            // ok
        }
    }

    @Test
    public void testVisitor() {
        DataObjectVisitor visitor = mock(DataObjectVisitor.class);

        InputDataObject composite = new CompositeInputDataObject(
                TestUtils.parseJSON("{fieldA:1,fieldB:2}"),
                TestUtils.parseJSON("{fieldB:3,fieldC:4}")
        );

        composite.accept(visitor);

        verify(visitor).beginVisitObject();

        verify(visitor).visitField(eq("fieldA"), TestUtils.matchingData("1"));
        verify(visitor).visitField(eq("fieldB"), TestUtils.matchingData("2"));
        verify(visitor).visitField(eq("fieldC"), TestUtils.matchingData("4"));

        verify(visitor).endVisitObject();

        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testInvalidTypes() {
        InputDataObject composite = new CompositeInputDataObject(
                TestUtils.parseJSON("{}"),
                TestUtils.parseJSON("{}")
        );

        try {
            composite.getBoolean();
            fail();
        } catch (InvalidInputDataTypeException e) {
            // ok
        }

        try {
            composite.getDouble();
            fail();
        } catch (InvalidInputDataTypeException e) {
            // ok
        }

        try {
            composite.getList();
            fail();
        } catch (InvalidInputDataTypeException e) {
            // ok
        }

        try {
            composite.getLong();
            fail();
        } catch (InvalidInputDataTypeException e) {
            // ok
        }

        try {
            composite.getString();
            fail();
        } catch (InvalidInputDataTypeException e) {
            // ok
        }
    }
}
