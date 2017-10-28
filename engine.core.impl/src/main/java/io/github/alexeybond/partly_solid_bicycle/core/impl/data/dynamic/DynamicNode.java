package io.github.alexeybond.partly_solid_bicycle.core.impl.data.dynamic;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.OutputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.DuplicateFieldException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidInputDataTypeException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidOutputDataTypeException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DynamicNode implements OutputDataObject, InputDataObject {
    long mLong;
    Object mObject;

    @NotNull
    NodeState state = NodeState.NULL;

    @Override
    public DynamicNode getField(String field)
            throws InvalidInputDataTypeException {
        return state.getFld(this, field);
    }

    @Override
    public String getString() throws InvalidInputDataTypeException {
        return state.getString(this);
    }

    @Override
    public boolean getBoolean() throws InvalidInputDataTypeException {
        return state.getBoolean(this);
    }

    @Override
    public double getDouble() throws InvalidInputDataTypeException {
        return state.getDouble(this);
    }

    @Override
    public long getLong() throws InvalidInputDataTypeException {
        return state.getLong(this);
    }

    @Override
    public List<? extends DynamicNode> getList() throws InvalidInputDataTypeException {
        return state.getArray(this);
    }

    @Override
    public DynamicNode addField(String field) throws InvalidOutputDataTypeException, DuplicateFieldException {
        return state.addFld(this, field);
    }

    @Override
    public DynamicNode addItem() throws InvalidOutputDataTypeException {
        return state.addItem(this);
    }

    @Override
    public void setString(String value) throws InvalidOutputDataTypeException {
        state.setString(this, value);
    }

    @Override
    public void setDouble(double value) throws InvalidOutputDataTypeException {
        state.setDouble(this, value);
    }

    @Override
    public void setLong(long value) throws InvalidOutputDataTypeException {
        state.setLong(this, value);
    }

    @Override
    public void setBoolean(boolean value) throws InvalidOutputDataTypeException {
        state.setBool(this, value);
    }

    @Override
    public void accept(@NotNull DataObjectVisitor visitor) {
        state.acceptVisitor(this, visitor);
    }
}
