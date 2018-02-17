package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class ComponentNode<T> extends NodeBase {
    @NotNull
    private final T component;

    @NotNull
    private final ComponentConnector<T> connector;

    public ComponentNode(
            @NotNull T component,
            @NotNull ComponentConnector<T> connector) {
        this.component = component;
        this.connector = connector;
    }

    @NotNull
    @Override
    public LogicNode get(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        throw new NoSuchElementException("No such child node: " + id);
    }

    @NotNull
    @Override
    public <A> LogicNode getOrAdd(
            @NotNull Id<LogicNode> id,
            @NotNull NodeFactory<A> factory,
            @Nullable A arg) throws RuntimeException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void populate(@NotNull NodePopulator populator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(@NotNull Id<LogicNode> id)
            throws UnsupportedOperationException, IllegalStateException {
        // nothing to do
    }

    @Override
    public void remove(@NotNull LogicNode child)
            throws UnsupportedOperationException, IllegalStateException, IllegalArgumentException {
        throw new IllegalArgumentException();
    }

    @NotNull
    @Override
    public <TT> TT getComponent() throws NoSuchElementException {
        return (TT) component;
    }

    @Override
    protected void onConnected0(@NotNull LogicNode parent, Id<LogicNode> id) {
        connector.onConnected(component, this, id);
    }

    @Override
    protected void onDisconnected0(@NotNull LogicNode parent) {
        connector.onDisconnected(component, this);
    }

    @Override
    public void accept(@NotNull NodeVisitor visitor) {
        visitor.visitComponent(component);
    }
}
