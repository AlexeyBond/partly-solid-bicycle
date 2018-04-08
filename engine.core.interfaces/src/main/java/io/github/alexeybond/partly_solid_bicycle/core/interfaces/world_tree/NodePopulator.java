package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Object that adds a batch of child nodes (probably having unpredictable dependencies).
 */
public interface NodePopulator {
    /**
     * Adds the children (using {@link LogicNode#getOrAdd(Id, NodeFactory, Object)}.
     *
     * @param node the parent node
     */
    void populate(@NotNull LogicNode node);

    /**
     * Resolves a child by id.
     * Will be called in case when a node being added by {@link #populate(LogicNode)}
     * has a dependency on a non-exist sibling.
     *
     * @param node    the parent node
     * @param childId child node id
     * @return child node or {@code null} if this populator does not know how to resolve child with
     * given identifier; the returned node (if not {@code null}) is not yet connected to parent
     */
    @Nullable
    LogicNode resolve(@NotNull LogicNode node, @NotNull Id<LogicNode> childId);
}
