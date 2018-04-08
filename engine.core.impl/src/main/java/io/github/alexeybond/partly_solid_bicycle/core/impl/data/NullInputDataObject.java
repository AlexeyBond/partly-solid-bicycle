package io.github.alexeybond.partly_solid_bicycle.core.impl.data;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidInputDataTypeException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum NullInputDataObject implements InputDataObject {
    INSTANCE;

    @Override
    public InputDataObject getField(String field)
            throws InvalidInputDataTypeException, UndefinedFieldException {
        return fail();
    }

    @Override
    public String getString() throws InvalidInputDataTypeException {
        return fail();
    }

    @Override
    public boolean getBoolean() throws InvalidInputDataTypeException {
        return fail();
    }

    @Override
    public double getDouble() throws InvalidInputDataTypeException {
        return fail();
    }

    @Override
    public long getLong() throws InvalidInputDataTypeException {
        return fail();
    }

    @Override
    public List<? extends InputDataObject> getList() throws InvalidInputDataTypeException {
        return fail();
    }

    @Override
    public void accept(@NotNull DataObjectVisitor visitor) {
        visitor.visitNull();
    }

    private <T> T fail() {
        throw new InvalidInputDataTypeException("Attempt to read a null-object.");
    }
}
