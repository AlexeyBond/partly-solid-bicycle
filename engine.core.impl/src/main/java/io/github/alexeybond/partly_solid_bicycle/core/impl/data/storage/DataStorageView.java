package io.github.alexeybond.partly_solid_bicycle.core.impl.data.storage;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.DataObjectVisitor;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.InvalidInputDataTypeException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.exceptions.UndefinedFieldException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.ObjectVariable;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Representation of {@link StorageNode} as {@link InputDataObject}.
 */
class DataStorageView implements InputDataObject {
    @NotNull
    private final LogicNode storageRoot;

    DataStorageView(@NotNull LogicNode storageRoot) {
        this.storageRoot = storageRoot;
    }

    @Override
    public InputDataObject getField(String field)
            throws InvalidInputDataTypeException, UndefinedFieldException {
        LogicNode child;

        try {
            child = storageRoot.get(storageRoot.getTreeContext().getIdSet().get(field));
        } catch (NoSuchElementException e) {
            throw (UndefinedFieldException) new UndefinedFieldException(field).initCause(e);
        }

        ObjectVariable<InputDataObject> childVar = child.getComponent();

        return childVar.get();
    }

    @Override
    public void accept(@NotNull final DataObjectVisitor visitor) {
        visitor.beginVisitObject();
        storageRoot.accept(new NodeVisitor() {
            @Override
            public void visitChild(@NotNull Id<LogicNode> id, @NotNull LogicNode child) {
                String fieldName = String.valueOf(id.serializable());
                ObjectVariable<InputDataObject> var = child.getComponent();
                visitor.visitField(fieldName, var.get());
            }

            @Override
            public void visitComponent(@NotNull Object component) {

            }
        });
        visitor.endVisitObject();
    }

    private <T> T fail(final String dataType) {
        throw new InvalidInputDataTypeException("Cannot read storage node as " + dataType);
    }

    @Override
    public String getString() throws InvalidInputDataTypeException {
        return fail("string");
    }

    @Override
    public boolean getBoolean() throws InvalidInputDataTypeException {
        return fail("boolean");
    }

    @Override
    public double getDouble() throws InvalidInputDataTypeException {
        return fail("double");
    }

    @Override
    public long getLong() throws InvalidInputDataTypeException {
        return fail("long");
    }

    @Override
    public List<? extends InputDataObject> getList() throws InvalidInputDataTypeException {
        return fail("array");
    }
}
