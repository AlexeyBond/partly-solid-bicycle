package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver.DeclarativeChildResolverMaker;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver.NullChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.CompositeNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeclarativeGroupNodeFactory implements NodeFactory<InputDataObject> {
    @NotNull
    private final NodeFactory<InputDataObject> factory;

    @NotNull
    private final IdSet<LogicNode> idSet;

    public DeclarativeGroupNodeFactory(
            @NotNull NodeFactory<InputDataObject> factory,
            @NotNull IdSet<LogicNode> idSet) {
        this.factory = factory;
        this.idSet = idSet;
    }

    @NotNull
    @Override
    public LogicNode create(@Nullable InputDataObject arg) {
        if (null == arg) throw new NullPointerException("arg");

        NodeChildResolver childResolver = DeclarativeChildResolverMaker.make(
                NullChildResolver.INSTANCE, factory, arg, idSet);

        return new CompositeNode(childResolver);
    }
}
