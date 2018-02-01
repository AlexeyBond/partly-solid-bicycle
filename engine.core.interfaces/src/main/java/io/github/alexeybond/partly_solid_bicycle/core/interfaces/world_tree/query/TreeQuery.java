package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.query;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import org.jetbrains.annotations.NotNull;

public interface TreeQuery {
    Object query(@NotNull LogicNode node);
}
