package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodePopulator;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class PopulatorChildResolver implements NodeChildResolver {
    @NotNull
    private final LogicNode node;

    @NotNull
    private final NodeChildResolver next;

    @NotNull
    private final NodePopulator populator;

    public PopulatorChildResolver(
            @NotNull LogicNode node,
            @NotNull NodeChildResolver next,
            @NotNull NodePopulator populator) {
        this.node = node;
        this.next = next;
        this.populator = populator;
    }

    @NotNull
    @Override
    public LogicNode resolve(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        LogicNode child = populator.resolve(node, id);

        if (null == child) return next.resolve(id);

        return child;
    }
}
