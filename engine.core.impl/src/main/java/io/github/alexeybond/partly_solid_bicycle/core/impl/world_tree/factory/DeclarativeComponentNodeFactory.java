package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.ComponentNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Loader;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ComponentConnector;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeclarativeComponentNodeFactory<TC> implements NodeFactory<InputDataObject> {
    @NotNull
    private final GenericFactory<TC, Void> factory;

    @NotNull
    private final CompanionResolver<TC, Loader<TC>> loaderResolver;

    @NotNull
    private final CompanionResolver<TC, ComponentConnector<TC>> connectorResolver;

    public DeclarativeComponentNodeFactory(
            @NotNull GenericFactory<TC, Void> factory,
            @NotNull CompanionResolver<TC, Loader<TC>> loaderResolver,
            @NotNull CompanionResolver<TC, ComponentConnector<TC>> connectorResolver) {
        this.factory = factory;
        this.loaderResolver = loaderResolver;
        this.connectorResolver = connectorResolver;
    }

    @NotNull
    @Override
    public LogicNode create(@Nullable InputDataObject arg) {
        TC component = factory.create(null);

        loaderResolver.resolve(component).load(component, arg);

        return new ComponentNode<TC>(component, connectorResolver.resolve(component));
    }
}