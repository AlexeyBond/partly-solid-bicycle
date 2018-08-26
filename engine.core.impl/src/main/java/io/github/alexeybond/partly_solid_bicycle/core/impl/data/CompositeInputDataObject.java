package io.github.alexeybond.partly_solid_bicycle.core.impl.data;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.adapter.DataObjectVisitorAdapter;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidInputDataTypeException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompositeInputDataObject implements InputDataObject {
    @NotNull
    private final InputDataObject[] objects;

    public CompositeInputDataObject(@NotNull InputDataObject... objects) {
        this.objects = objects;
    }

    @Override
    public InputDataObject getField(String field)
            throws InvalidInputDataTypeException, UndefinedFieldException {
        for (InputDataObject object : objects) {
            try {
                return object.getField(field);
            } catch (UndefinedFieldException e) {
                // ok
            }
        }

        throw new UndefinedFieldException(field);
    }

    @Override
    public String getString() throws InvalidInputDataTypeException {
        return failRead("string");
    }

    @Override
    public boolean getBoolean() throws InvalidInputDataTypeException {
        return failRead("boolean");
    }

    @Override
    public double getDouble() throws InvalidInputDataTypeException {
        return failRead("number");
    }

    @Override
    public long getLong() throws InvalidInputDataTypeException {
        return failRead("number");
    }

    @Override
    public List<? extends InputDataObject> getList() throws InvalidInputDataTypeException {
        return failRead("array");
    }

    private <T> T failRead(@NotNull final String asType) {
        throw new InvalidInputDataTypeException(
                "Composite object cannot be read as " + asType
        );
    }

    @Override
    public void accept(@NotNull final DataObjectVisitor visitor) {
        final Set<String> visitedFields = new HashSet<String>();

        DataObjectVisitor innerVisitor = new DataObjectVisitorAdapter() {
            @Override
            public void visitField(String field, InputDataObject value) {
                if (visitedFields.contains(field)) return;

                visitor.visitField(field, value);

                visitedFields.add(field);
            }
        };

        visitor.beginVisitObject();

        for (InputDataObject object : objects) {
            object.accept(innerVisitor);
        }

        visitor.endVisitObject();
    }
}
