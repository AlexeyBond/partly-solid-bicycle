package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.CompositeNodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class CompositeNodeFactoryImpl<A, K> implements CompositeNodeFactory<A, K> {
    private final Map<K, NodeFactory<A>> map = new HashMap<K, NodeFactory<A>>();

    protected abstract @NotNull
    K getKey(@Nullable A arg);

    @NotNull
    @Override
    public LogicNode create(@Nullable A arg) {
        K key = getKey(arg);
        NodeFactory<A> factory = map.get(key);

        try {
            return factory.create(arg);
        } catch (NullPointerException e) {
            if (null == factory) {
                throw new IllegalArgumentException("Illegal key: " + key);
            }
            throw e;
        }
    }

    @Override
    public void put(@NotNull K key, @NotNull NodeFactory<A> factory) {
        map.put(key, factory);
    }
}
