package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver;

import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.ProxyNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ChildLogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeChildResolver;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

/**
 * {@link NodeChildResolver} that returns proxies to exist children of another node (called prototype).
 */
public class ProxyChildResolver implements NodeChildResolver {
    @NotNull
    private final LogicNode prototype;

    public ProxyChildResolver(@NotNull LogicNode prototype) {
        this.prototype = prototype;
    }

    @NotNull
    @Override
    public ChildLogicNode resolve(@NotNull Id<LogicNode> id)
            throws NoSuchElementException {
        return new ProxyNode(prototype.get(id));
    }
}
