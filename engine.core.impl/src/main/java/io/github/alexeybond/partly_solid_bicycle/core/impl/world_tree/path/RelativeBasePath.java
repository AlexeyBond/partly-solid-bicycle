package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.path;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public enum RelativeBasePath implements LogicNodePath {
    INSTANCE;

    @NotNull
    @Override
    public LogicNode lookup(@NotNull LogicNode start) throws NoSuchElementException {
        return start;
    }

    @NotNull
    @Override
    public <A> LogicNode lookupOrAdd(@NotNull LogicNode start, @NotNull NodeFactory<A> factory, A argument) {
        return lookup(start);
    }
}
