package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

/**
 * Warning: Some methods of this class violate {@link LogicNode} contract
 * use with caution
 */
public enum NullNode implements LogicNode {
    INSTANCE;

    @NotNull
    @Override
    public LogicNode get(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @NotNull
    @Override
    public <A> LogicNode getOrAdd(
            @NotNull Id<LogicNode> id,
            @NotNull NodeFactory<A> factory,
            @Nullable A arg
    ) throws RuntimeException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void populate(@NotNull NodePopulator populator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(@NotNull Id<LogicNode> id) throws IllegalStateException {

    }

    @Override
    public void remove(@NotNull LogicNode child) throws IllegalStateException, IllegalArgumentException {
        throw new IllegalArgumentException();
    }

    @NotNull
    @Override
    public LogicNode getParent() {
        return this;
    }

    @NotNull
    @Override
    public <T> T getComponent() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @NotNull
    @Override
    public TreeContext getTreeContext() {
        return null;
    }

    @Override
    public void accept(@NotNull NodeVisitor visitor) {
    }
}