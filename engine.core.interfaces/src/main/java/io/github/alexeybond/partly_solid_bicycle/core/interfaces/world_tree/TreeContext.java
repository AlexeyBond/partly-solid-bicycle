package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import org.jetbrains.annotations.NotNull;

public interface TreeContext {
    @NotNull
    IdSet<LogicNode> getIdSet();

    @NotNull
    LogicNode getRoot();
}
