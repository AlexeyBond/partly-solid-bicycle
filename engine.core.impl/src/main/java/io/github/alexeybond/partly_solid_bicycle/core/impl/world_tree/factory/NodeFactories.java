package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver.NullChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.connector.NullConnector;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.ComponentNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.GroupNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.NullChildNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.NullPopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ChildLogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum NodeFactories implements NodeFactory<Object> {
    NULL {
        @NotNull
        @Override
        public ChildLogicNode create(@Nullable Object arg) {
            return new NullChildNode();
        }
    },

    EMPTY_GROUP {
        @NotNull
        @Override
        public ChildLogicNode create(@Nullable Object arg) {
            return new GroupNode(NullChildResolver.INSTANCE, NullPopulator.INSTANCE);
        }
    },

    SIMPLE_COMPONENT {
        @NotNull
        @Override
        public ChildLogicNode create(@Nullable Object component) {
            if (null == component) throw new NullPointerException("component");

            return new ComponentNode<Object>(component, NullConnector.get());
        }
    }
}
