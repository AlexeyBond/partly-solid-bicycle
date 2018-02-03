package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * A object that resolves missing {@link LogicNode} children.
 */
public interface NodeChildResolver {
    /**
     * @param id child id
     * @return the child node
     * @throws NoSuchElementException if this resolver cannot resolve child with given name
     */
    @NotNull
    LogicNode resolve(@NotNull Id<LogicNode> id) throws NoSuchElementException;

    /**
     * Returns the resolver that must be used for next {@link #resolve(Id)} call.
     * <p>
     * This method is added for a case when a resolver has a finite list of children it can resolve.
     * When all children from that list are resolved the resolver may delegate consequent calls to another
     * one (probably the one it delegated all failed calls before).
     * </p>
     *
     * @return the resolver that must be used for next {@link #resolve(Id)} call
     */
    @NotNull
    NodeChildResolver next();

    /**
     * @return iterable of all known identifiers of unresolved children
     */
    @NotNull
    Collection<Id<LogicNode>> getUnresolvedIds();
}
