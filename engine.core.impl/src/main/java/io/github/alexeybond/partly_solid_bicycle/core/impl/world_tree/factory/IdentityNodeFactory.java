package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.adapter.NodeFactoryAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IdentityNodeFactory extends NodeFactoryAdapter<LogicNode> {
    public static final IdentityNodeFactory INSTANCE = new IdentityNodeFactory();

    @NotNull
    @Override
    public LogicNode create(@Nullable LogicNode arg) {
        if (null == arg) throw new IllegalArgumentException("arg == null");

        return arg;
    }
}
