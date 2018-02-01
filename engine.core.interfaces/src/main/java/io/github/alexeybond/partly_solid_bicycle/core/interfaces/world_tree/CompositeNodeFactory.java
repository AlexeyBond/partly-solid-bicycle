package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import org.jetbrains.annotations.NotNull;

public interface CompositeNodeFactory<A, K> extends NodeFactory<A> {
    void put(@NotNull K key, @NotNull NodeFactory<A> factory);
}
