package io.github.alexeybond.partly_solid_bicycle.core.impl.data.visitors;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import org.jetbrains.annotations.NotNull;

public class VisitorWrapper implements DataObjectVisitor {
    @NotNull
    protected final DataObjectVisitor wrapped;

    public VisitorWrapper(@NotNull DataObjectVisitor wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void beginVisitObject() {
        wrapped.beginVisitObject();
    }

    @Override
    public void visitField(String field, InputDataObject value) {
        wrapped.visitField(field, value);
    }

    @Override
    public void endVisitObject() {
        wrapped.endVisitObject();
    }

    @Override
    public void beginVisitArray() {
        wrapped.beginVisitArray();
    }

    @Override
    public void visitItem(InputDataObject object) {
        wrapped.visitItem(object);
    }

    @Override
    public void endVisitArray() {
        wrapped.endVisitArray();
    }

    @Override
    public void visitValue(double value) {
        wrapped.visitValue(value);
    }

    @Override
    public void visitValue(long value) {
        wrapped.visitValue(value);
    }

    @Override
    public void visitValue(boolean value) {
        wrapped.visitValue(value);
    }

    @Override
    public void visitValue(String value) {
        wrapped.visitValue(value);
    }

    @Override
    public void visitNull() {
        wrapped.visitNull();
    }
}
