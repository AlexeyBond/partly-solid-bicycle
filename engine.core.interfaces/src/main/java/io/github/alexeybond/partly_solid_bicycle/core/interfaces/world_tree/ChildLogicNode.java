package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import org.jetbrains.annotations.NotNull;

public interface ChildLogicNode extends LogicNode {

    /**
     * Called when this node becomes a child of another node.
     *
     * @param parent the parent node
     * @param id     identifier of this node within parent node
     */
    void onConnected(@NotNull LogicNode parent, @NotNull Id<LogicNode> id);

    /**
     * Called when this node is disconnected from a parent node.
     *
     * @param parent the parent node
     */
    void onDisconnected(@NotNull LogicNode parent);
}
