package io.github.alexeybond.partly_solid_bicycle.core.impl.data.gdxjson;

import com.badlogic.gdx.utils.JsonWriter;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;

import java.io.IOException;

public class GdxJsonWriter implements DataObjectVisitor {
    private JsonWriter writer;

    @Override
    public void beginVisitObject() {
        try {
            writer.object();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitField(String field, InputDataObject value) {
        try {
            writer.name(field);
            value.accept(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endVisitObject() {
        try {
            writer.pop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beginVisitArray() {
        try {
            writer.array();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitItem(InputDataObject object) {
        object.accept(this);
    }

    @Override
    public void endVisitArray() {
        try {
            writer.pop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitValue(double value) {
        try {
            writer.value(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitValue(long value) {
        try {
            writer.value(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitValue(boolean value) {
        try {
            writer.value(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitValue(String value) {
        try {
            writer.value(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void visitNull() {
        try {
            writer.value(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void doWrite(JsonWriter writer, InputDataObject object) {
        this.writer = writer;
        try {
            object.accept(this);
        } finally {
            this.writer = null;
        }
    }
}
