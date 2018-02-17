package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeChildResolver;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public enum NullChildResolver implements NodeChildResolver {
    INSTANCE;

    @NotNull
    @Override
    public LogicNode resolve(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        throw new NoSuchElementException("No such node: " + id.toString());
    }
}
