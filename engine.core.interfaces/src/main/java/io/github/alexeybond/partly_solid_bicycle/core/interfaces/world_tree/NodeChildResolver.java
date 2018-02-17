package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

/**
 * A object that resolves missing {@link LogicNode} children.
 */
public interface NodeChildResolver {
    /**
     * @param id child id
     * @return the child node
     * @throws NoSuchElementException if this resolver cannot resolve child with given name
     */
    @NotNull
    LogicNode resolve(@NotNull Id<LogicNode> id) throws NoSuchElementException;
}
