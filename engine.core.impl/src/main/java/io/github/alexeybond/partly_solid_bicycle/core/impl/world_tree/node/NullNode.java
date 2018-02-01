package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class NullNode extends NodeBase {
    @Override
    protected void onConnected0(@NotNull LogicNode parent) {

    }

    @Override
    protected void onDisconnected0(@NotNull LogicNode parent) {

    }

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
            @Nullable A arg) throws RuntimeException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(@NotNull Id<LogicNode> id)
            throws UnsupportedOperationException, IllegalStateException {

    }

    @Override
    public void remove(@NotNull LogicNode child)
            throws UnsupportedOperationException, IllegalStateException, IllegalArgumentException {

    }

    @NotNull
    @Override
    public <T> T getComponent() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public void accept(@NotNull NodeVisitor visitor) {

    }
}
