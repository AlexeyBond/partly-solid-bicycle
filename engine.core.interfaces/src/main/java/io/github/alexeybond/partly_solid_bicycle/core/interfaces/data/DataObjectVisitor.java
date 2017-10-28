package io.github.alexeybond.partly_solid_bicycle.core.interfaces.data;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.visitor.Visitor;

public interface DataObjectVisitor extends Visitor<InputDataObject> {
    void beginVisitObject();

    void visitField(String field, InputDataObject value);

    void endVisitObject();

    void beginVisitArray();

    void visitItem(InputDataObject object);

    void endVisitArray();

    void visitValue(double value);
    void visitValue(long value);
    void visitValue(boolean value);
    void visitValue(String value);

    void visitNull();
}
