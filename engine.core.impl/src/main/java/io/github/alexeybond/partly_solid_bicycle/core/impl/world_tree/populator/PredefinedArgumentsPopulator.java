package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodePopulator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Populates a node with children produced by one factory with different arguments.
 * <p>
 * Factory arguments must be not null.
 *
 * @param <A>
 */
public class PredefinedArgumentsPopulator<A> implements NodePopulator {
    @NotNull
    private final NodeFactory<A> factory;

    @NotNull
    private final Map<Id<LogicNode>, A> arguments;

    public PredefinedArgumentsPopulator(
            @NotNull NodeFactory<A> factory,
            @NotNull Map<Id<LogicNode>, A> arguments) {
        this.factory = factory;
        this.arguments = arguments;
    }

    @Override
    public void populate(@NotNull LogicNode node) {
        for (HashMap.Entry<Id<LogicNode>, A> entry : arguments.entrySet()) {
            node.getOrAdd(entry.getKey(), factory, entry.getValue());
        }
    }

    @Nullable
    @Override
    public LogicNode resolve(@NotNull LogicNode node, @NotNull Id<LogicNode> childId) {
        A a = arguments.get(childId);

        if (null != a) {
            return node.getOrAdd(childId, factory, a);
        }

        return null;
    }
}