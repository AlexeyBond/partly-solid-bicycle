package io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.factory.GenericFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A factory that produces {@link LogicNode}'s.
 *
 * @param <A>
 */
public interface NodeFactory<A> extends GenericFactory<LogicNode, A> {
    @NotNull
    LogicNode create(@Nullable A arg);
}
