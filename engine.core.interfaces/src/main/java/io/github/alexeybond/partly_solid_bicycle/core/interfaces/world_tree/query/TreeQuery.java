package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.query;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import org.jetbrains.annotations.NotNull;

/**
 * Query that matches some (or none) nodes relative to some given node.
 * <p>
 * Query may represent a path in node tree with wildcards e.g.:
 * <pre>
 * /world/enemies/*
 * </pre>
 * that for tree
 * <pre>
 * /world
 * /world/enemies
 * /world/enemies/player <- you are your own worst enemy. got it?
 * /world/enemies/unfriendly_mushroom
 * </pre>
 * will match paths
 * <pre>
 * /world/enemies/player
 * /world/enemies/unfriendly_mushroom
 * </pre>
 * <p>
 * Query interface assumes queries like the example above but does not limit them to just paths with wildcards.
 */
public interface TreeQuery {
    /**
     * Perform the query.
     *
     * @param node     node to start from
     * @param listener the listener
     * @param <S>      type of listener state
     * @param <R>      type of result
     * @return the result provided by listener
     */
    <S, R> R query(@NotNull LogicNode node, @NotNull TreeQueryListener<S, R> listener);
}
