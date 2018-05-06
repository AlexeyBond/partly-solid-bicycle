package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ChildLogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.adapter.NodeFactoryAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IdentityNodeFactory extends NodeFactoryAdapter<ChildLogicNode> {
    public static final IdentityNodeFactory INSTANCE = new IdentityNodeFactory();

    @NotNull
    @Override
    public ChildLogicNode create(@Nullable ChildLogicNode arg) {
        if (null == arg) throw new IllegalArgumentException("arg == null");

        return arg;
    }
}
