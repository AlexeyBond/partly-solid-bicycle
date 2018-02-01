package io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.adapter;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;

public class DataObjectVisitorAdapter implements DataObjectVisitor {
    @Override
    public void beginVisitObject() {
        beginVisitAnyStructure();
    }

    @Override
    public void visitField(String field, InputDataObject value) {
    }

    @Override
    public void endVisitObject() {
    }

    @Override
    public void beginVisitArray() {
        beginVisitAnyStructure();
    }

    @Override
    public void visitItem(InputDataObject object) {
    }

    @Override
    public void endVisitArray() {
    }

    @Override
    public void visitValue(double value) {
        visitAnyValue(value);
    }

    @Override
    public void visitValue(long value) {
        visitAnyValue(value);
    }

    @Override
    public void visitValue(boolean value) {
        visitAnyValue(value);
    }

    @Override
    public void visitValue(String value) {
        visitAnyValue(value);
    }

    @Override
    public void visitNull() {
        visitAnyValue(null);
    }

    protected void visitAnyValue(Object value) {
    }

    protected void beginVisitAnyStructure() {
    }
}
