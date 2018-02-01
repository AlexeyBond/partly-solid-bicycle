package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.adapter;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeVisitor;
import org.jetbrains.annotations.NotNull;

public class NodeVisitorAdapter implements NodeVisitor {
    protected NodeVisitorAdapter() {
    }

    @Override
    public void visitChild(@NotNull Id<LogicNode> id, @NotNull LogicNode child) {
    }

    @Override
    public void visitComponent(@NotNull Object component) {
    }
}
