package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CompositeDeclarativeNodeFactory extends CompositeNodeFactoryImpl<InputDataObject, String> {
    private final @NotNull
    String typeField;

    public CompositeDeclarativeNodeFactory(@NotNull String typeField) {
        this.typeField = typeField;
    }

    public CompositeDeclarativeNodeFactory() {
        this("class");
    }

    @NotNull
    @Override
    protected String getKey(@Nullable InputDataObject arg) {
        return arg.getField(typeField).getString();
    }
}
