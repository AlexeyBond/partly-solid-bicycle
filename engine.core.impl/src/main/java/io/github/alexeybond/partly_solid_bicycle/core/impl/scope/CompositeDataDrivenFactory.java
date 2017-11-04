package io.github.alexeybond.partly_solid_bicycle.core.impl.scope;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.DataDrivenFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.exceptions.ScopeMemberFactoryException;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CompositeDataDrivenFactory<T> implements DataDrivenFactory<T> {
    private final Map<String, DataDrivenFactory<? extends T>> children;
    private final String typeKeyField;

    public CompositeDataDrivenFactory(
            @NotNull Map<String, DataDrivenFactory<? extends T>> children,
            @NotNull String typeKeyField) {
        this.children = children;
        this.typeKeyField = typeKeyField;
    }

    @NotNull
    @Override
    public T create(@Nullable InputDataObject arg)
            throws ScopeMemberFactoryException {
        if (null == arg) throw new NullPointerException("arg");

        String typeKey = arg.getField(typeKeyField).getString();

        DataDrivenFactory<? extends T> factory = children.get(typeKey);

        if (null == factory) throw new ScopeMemberFactoryException(
                "No factory found for '" + typeKeyField + "' = '" + typeKey + "'.");

        return factory.create(arg);
    }

    public void addChild(@NotNull String typeKey, @NotNull DataDrivenFactory<? extends T> factory) {
        children.put(typeKey, factory);
    }
}
