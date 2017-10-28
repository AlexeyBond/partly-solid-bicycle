package io.github.alexeybond.partly_solid_bicycle.core.impl.data.visitors;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.OutputDataObject;

/**
 * Visitor that copies contents of a {@link InputDataObject} to a {@link OutputDataObject}.
 */
public class CopyVisitor implements DataObjectVisitor {
    private OutputDataObject destination;

    @Override
    public void beginVisitObject() {

    }

    @Override
    public void visitField(String field, InputDataObject value) {
        final OutputDataObject dst = destination;
        destination = dst.addField(field);

        try {
            value.accept(this);
        } finally {
            destination = dst;
        }
    }

    @Override
    public void endVisitObject() {

    }

    @Override
    public void beginVisitArray() {

    }

    @Override
    public void visitItem(InputDataObject item) {
        final OutputDataObject dst = destination;
        destination = dst.addItem();

        try {
            item.accept(this);
        } finally {
            destination = dst;
        }
    }

    @Override
    public void endVisitArray() {

    }

    @Override
    public void visitValue(double value) {
        destination.setDouble(value);
    }

    @Override
    public void visitValue(long value) {
        destination.setLong(value);
    }

    @Override
    public void visitValue(boolean value) {
        destination.setBoolean(value);
    }

    @Override
    public void visitValue(String value) {
        destination.setString(value);
    }

    @Override
    public void visitNull() {

    }

    public <T extends OutputDataObject> T doCopy(InputDataObject in, T out) {
        this.destination = out;
        in.accept(this);
        return out;
    }
}
