package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ChildLogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class ConstantArgumentChildResolver<A> implements NodeChildResolver {
    @NotNull
    private final NodeFactory<A> factory;

    @Nullable
    private final A argument;

    public ConstantArgumentChildResolver(@NotNull NodeFactory<A> factory, @Nullable A argument) {
        this.factory = factory;
        this.argument = argument;
    }

    @NotNull
    @Override
    public ChildLogicNode resolve(@NotNull Id<LogicNode> id)
            throws NoSuchElementException {
        return factory.create(argument);
    }
}
