package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.visitor.Visitor;
import org.jetbrains.annotations.NotNull;

public interface NodeVisitor extends Visitor<LogicNode> {
    void visitChild(@NotNull Id<LogicNode> id, @NotNull LogicNode child);

    void visitComponent(@NotNull Object component);
}
