package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import org.jetbrains.annotations.NotNull;

/**
 * Interface for a component that wants to be notified when it is attached to and detached from
 * a {@link LogicNode node}.
 */
public interface NodeAttachmentListener {
    void onAttached(@NotNull LogicNode node);

    void onDetached(@NotNull LogicNode node);
}
