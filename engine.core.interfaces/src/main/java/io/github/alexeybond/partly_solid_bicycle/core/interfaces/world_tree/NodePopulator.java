package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import org.jetbrains.annotations.NotNull;

/**
 * Object that adds a batch of child nodes (probably having unpredictable dependencies).
 *
 * To handle unpredictable dependencies between added nodes populator works in both "push" and "pull" mode:
 * it adds children to the node when {@link #populate(LogicNode)} method is called and at the same time
 * it implements {@link NodeChildResolver} interface allowing the node to pull children required by new nodes.
 */
public interface NodePopulator extends NodeChildResolver {
    /**
     * Adds the children (using {@link LogicNode#getOrAdd(Id, NodeFactory, Object)}.
     *
     * @param node the parent node
     */
    void populate(@NotNull LogicNode node);
}
