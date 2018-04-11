package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodePopulator;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public enum NullPopulator implements NodePopulator {
    INSTANCE;

    @Override
    public void populate(@NotNull LogicNode node) {
        // nothing to do
    }

    @Override
    @NotNull
    public LogicNode resolve(@NotNull Id<LogicNode> childId) {
        throw new NoSuchElementException();
    }
}
