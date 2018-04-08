package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.connector;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ComponentConnector;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import org.jetbrains.annotations.NotNull;

public enum NullConnector implements ComponentConnector {
    INSTANCE;

    @SuppressWarnings({"unchecked"})
    public static <T> ComponentConnector<T> get() {
        return (ComponentConnector<T>) INSTANCE;
    }

    @Override
    public void onConnected(@NotNull Object component, @NotNull LogicNode node, @NotNull Id id) {
    }

    @Override
    public void onDisconnected(@NotNull Object component, @NotNull LogicNode node) {
    }
}
