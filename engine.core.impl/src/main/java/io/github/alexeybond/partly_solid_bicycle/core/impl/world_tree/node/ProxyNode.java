package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class ProxyNode extends NodeBase {
    @NotNull
    private final LogicNode real;

    public ProxyNode(@NotNull LogicNode real) {
        this.real = real;
    }

    @Override
    protected void onConnected0(@NotNull LogicNode parent) {

    }

    @Override
    protected void onDisconnected0(@NotNull LogicNode parent) {

    }

    @NotNull
    @Override
    public LogicNode get(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        return real.get(id);
    }

    @NotNull
    @Override
    public <A> LogicNode getOrAdd(
            @NotNull Id<LogicNode> id,
            @NotNull NodeFactory<A> factory,
            @Nullable A arg)
            throws RuntimeException, UnsupportedOperationException {
        return real.getOrAdd(id, factory, arg);
    }

    @Override
    public void remove(@NotNull Id<LogicNode> id)
            throws UnsupportedOperationException, IllegalStateException {
        real.remove(id);
    }

    @Override
    public void remove(@NotNull LogicNode child)
            throws UnsupportedOperationException, IllegalStateException, IllegalArgumentException {
        real.remove(child);
    }

    @NotNull
    @Override
    public <T> T getComponent() throws NoSuchElementException {
        return real.getComponent();
    }

    @Override
    public void accept(@NotNull NodeVisitor visitor) {
        real.accept(visitor);
    }
}
