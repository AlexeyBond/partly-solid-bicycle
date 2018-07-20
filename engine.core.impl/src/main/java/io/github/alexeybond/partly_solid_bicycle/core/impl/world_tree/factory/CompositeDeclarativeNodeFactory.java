package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompositeDeclarativeNodeFactory extends CompositeNodeFactoryImpl<InputDataObject, String> {
    private final @NotNull
    String typeField;

    public CompositeDeclarativeNodeFactory(@NotNull String typeField, @NotNull String kind) {
        super(kind);
        this.typeField = typeField;
    }

    public CompositeDeclarativeNodeFactory(@NotNull String kind) {
        this("class", kind);
    }

    @NotNull
    @Override
    protected String getKey(@Nullable InputDataObject arg) {
        return arg.getField(typeField).getString();
    }
}
