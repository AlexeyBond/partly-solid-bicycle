package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PredefinedChildListResolver<A> implements NodeChildResolver {
    @NotNull
    private NodeChildResolver next;

    @NotNull
    private final Map<Id<LogicNode>, A> childArgs;

    @NotNull
    private final NodeFactory<A> factory;

    public PredefinedChildListResolver(
            @NotNull NodeChildResolver next,
            @NotNull Map<Id<LogicNode>, A> childArgs,
            @NotNull NodeFactory<A> factory) {
        this.next = next;
        this.childArgs = childArgs;
        this.factory = factory;
    }

    @NotNull
    @Override
    public LogicNode resolve(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        A arg = childArgs.get(id);

        if (null != arg) {
            LogicNode res = factory.create(arg);
            childArgs.remove(id);
            return res;
        }

        return next.resolve(id);
    }

    @NotNull
    @Override
    public NodeChildResolver next() {
        next = next.next();

        if (0 == childArgs.size()) {
            return next;
        }

        return this;
    }

    @NotNull
    @Override
    public Collection<Id<LogicNode>> getUnresolvedIds() {
        Collection<Id<LogicNode>> nextUnresolved = next.getUnresolvedIds();
        ArrayList<Id<LogicNode>> list
                = new ArrayList<Id<LogicNode>>(nextUnresolved.size() + childArgs.size());
        list.addAll(nextUnresolved);
        list.addAll(childArgs.keySet());
        return Collections.unmodifiableList(list);
    }
}
