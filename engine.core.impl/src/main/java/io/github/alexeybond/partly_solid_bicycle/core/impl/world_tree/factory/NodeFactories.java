package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver.NullChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.GroupNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.NullNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.NullPopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum NodeFactories implements NodeFactory<Object> {
    NULL {
        @NotNull
        @Override
        public LogicNode create(@Nullable Object arg) {
            return new NullNode();
        }
    },

    EMPTY_GROUP {
        @NotNull
        @Override
        public LogicNode create(@Nullable Object arg) {
            return new GroupNode(NullChildResolver.INSTANCE, NullPopulator.INSTANCE);
        }
    }
}
