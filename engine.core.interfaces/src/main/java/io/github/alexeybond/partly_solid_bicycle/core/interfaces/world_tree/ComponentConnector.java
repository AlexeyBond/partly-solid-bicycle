package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.companions.Companion;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import org.jetbrains.annotations.NotNull;

public interface ComponentConnector<T> extends Companion<T> {
    void onConnected(@NotNull T component, @NotNull LogicNode node, @NotNull Id<LogicNode> id);

    void onDisconnected(@NotNull T component, @NotNull LogicNode node);
}
