package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.ComponentNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.CompanionResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Loader;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ChildLogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ComponentConnector;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.adapter.NodeFactoryAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeclarativeComponentNodeFactory<TC> extends NodeFactoryAdapter<InputDataObject> {
    @NotNull
    private final GenericFactory<TC, Void> factory;

    @NotNull
    private final CompanionResolver<TC, ? extends Loader<TC>> loaderResolver;

    @NotNull
    private final CompanionResolver<TC, ? extends ComponentConnector<TC>> connectorResolver;

    public DeclarativeComponentNodeFactory(
            @NotNull GenericFactory<TC, Void> factory,
            @NotNull CompanionResolver<TC, ? extends Loader<TC>> loaderResolver,
            @NotNull CompanionResolver<TC, ? extends ComponentConnector<TC>> connectorResolver) {
        this.factory = factory;
        this.loaderResolver = loaderResolver;
        this.connectorResolver = connectorResolver;
    }

    @NotNull
    @Override
    public ChildLogicNode create(@Nullable InputDataObject arg) {
        TC component = factory.create(null);

        loaderResolver.resolve(component).load(component, arg);

        return new ComponentNode<TC>(component, connectorResolver.resolve(component));
    }
}
