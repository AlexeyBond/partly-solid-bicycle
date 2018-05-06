package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public class SuperRootNode implements LogicNode, TreeContext {
    @NotNull
    private final IdSet<LogicNode> idSet;

    @NotNull
    private final ChildLogicNode rootNode;

    @NotNull
    private final Id<LogicNode> rootId;

    public SuperRootNode(
            @NotNull IdSet<LogicNode> idSet,
            @NotNull ChildLogicNode rootNode) {
        this.idSet = idSet;
        this.rootNode = rootNode;

        rootId = idSet.unnamed();

        rootNode.onConnected(this, rootId);
    }

    @NotNull
    @Override
    public LogicNode get(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        if (id == rootId) {
            return rootNode;
        }

        throw new NoSuchElementException();
    }

    @NotNull
    @Override
    public <A> LogicNode getOrAdd(
            @NotNull Id<LogicNode> id,
            @NotNull NodeFactory<A> factory,
            @Nullable A arg)
            throws RuntimeException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void populate(@NotNull NodePopulator populator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(@NotNull Id<LogicNode> id)
            throws UnsupportedOperationException, IllegalStateException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(@NotNull LogicNode child)
            throws UnsupportedOperationException, IllegalStateException, IllegalArgumentException {
        throw new UnsupportedOperationException();
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
        return this;
    }

    @Override
    public void accept(@NotNull NodeVisitor visitor) {
        visitor.visitChild(rootId, rootNode);
    }

    @NotNull
    @Override
    public IdSet<LogicNode> getIdSet() {
        return idSet;
    }

    @NotNull
    @Override
    public LogicNode getRoot() {
        return rootNode;
    }

    public void dispose() {
        rootNode.onDisconnected(this);
    }
}
