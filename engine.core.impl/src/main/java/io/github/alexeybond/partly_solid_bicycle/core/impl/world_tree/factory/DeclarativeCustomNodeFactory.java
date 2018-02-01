package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Loader;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeclarativeCustomNodeFactory<TN extends LogicNode>
        implements NodeFactory<InputDataObject> {
    @NotNull
    private final GenericFactory<TN, Void> factory;

    @NotNull
    private final CompanionResolver<TN, Loader<TN>> loaderResolver;

    public DeclarativeCustomNodeFactory(
            @NotNull GenericFactory<TN, Void> factory,
            @NotNull CompanionResolver<TN, Loader<TN>> loaderResolver) {
        this.factory = factory;
        this.loaderResolver = loaderResolver;
    }

    @NotNull
    @Override
    public LogicNode create(@Nullable InputDataObject arg) {
        TN node = factory.create(null);

        loaderResolver.resolve(node).load(node, arg);

        return node;
    }
}
