package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

/**
 * Relative or absolute path in tree of {@link LogicNode}'s.
 * <p>
 * {@link LogicNodePath} (or at least it's {@link #lookup(LogicNode)} method) could be implemented as
 * {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.query.TreeQuery tree query}
 * with
 * {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.query.TreeQueryListener listener}
 * returning the first matching node.
 * This isn't done because of two reasons:
 * (1) path is simple and very typical case so separate implementation may save some overhead of more generic query;
 * (2) path can be implemented faster and is required for some critical features.
 */
public interface LogicNodePath {
    /**
     * Get node located on this path from given node.
     *
     * @param start the start node
     * @return the node this path leads to
     * @throws NoSuchElementException if one of intermediate nodes is not found
     */
    @NotNull
    LogicNode lookup(@NotNull LogicNode start) throws NoSuchElementException;

    /**
     * Get node located on this path from given node or create it if it does not exist.
     * <p>
     * Note: this method does not create any intermediate nodes
     * </p>
     *
     * @param start    start node
     * @param factory  factory that will create the node if it is not exist
     * @param argument argument for {@code factory}
     * @param <A>      factory argument type
     * @return the resulting node (previously exist or just created)
     * @see LogicNode#getOrAdd(Id, NodeFactory, Object)
     */
    @NotNull
    <A> LogicNode lookupOrAdd(@NotNull LogicNode start, @NotNull NodeFactory<A> factory, A argument);

    /**
     * @return text representation of this path
     */
    @Override
    String toString();
}
