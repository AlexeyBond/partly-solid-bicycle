package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeChildResolver;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public class CompositeChildResolver implements NodeChildResolver {

    @NotNull
    private final NodeChildResolver second;

    @NotNull
    private final NodeChildResolver first;

    public CompositeChildResolver(
            @NotNull NodeChildResolver second,
            @NotNull NodeChildResolver first) {
        this.second = second;
        this.first = first;
    }

    @NotNull
    @Override
    public LogicNode resolve(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        try {
            return first.resolve(id);
        } catch (NoSuchElementException e) {
            return second.resolve(id);
        }
    }
}
