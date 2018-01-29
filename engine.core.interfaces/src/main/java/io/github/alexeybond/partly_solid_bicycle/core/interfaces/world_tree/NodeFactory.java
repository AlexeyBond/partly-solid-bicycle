package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import org.jetbrains.annotations.Nullable;

/**
 * A factory that produces {@link LogicNode}'s.
 *
 * @param <A>
 */
public interface NodeFactory<A> {
    LogicNode create(@Nullable A arg);
}
