package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.Nullable;

public enum IdentityNodeFactory implements NodeFactory<LogicNode> {
    INSTANCE;

    @Override
    public LogicNode create(@Nullable LogicNode arg) {
        return arg;
    }
}
