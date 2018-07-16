package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.path;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class ParentPath implements LogicNodePath {
    @NotNull
    private final LogicNodePath basePath;

    public ParentPath(@NotNull LogicNodePath basePath) {
        this.basePath = basePath;
    }

    @NotNull
    @Override
    public LogicNode lookup(@NotNull LogicNode start) throws NoSuchElementException {
        return basePath.lookup(start).getParent();
    }

    @NotNull
    @Override
    public <A> LogicNode lookupOrAdd(@NotNull LogicNode start, @NotNull NodeFactory<A> factory, A argument) {
        return lookup(start);
    }

    @Override
    public String toString() {
        return basePath + "/..";
    }
}
