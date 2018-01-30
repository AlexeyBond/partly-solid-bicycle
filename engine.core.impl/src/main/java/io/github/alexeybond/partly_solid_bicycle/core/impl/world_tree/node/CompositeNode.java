package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node;

import io.github.alexeybond.partly_solid_bicycle.core.impl.util.ExceptionAccumulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class CompositeNode extends NodeBase {
    private Map<Id<LogicNode>, LogicNode> map = new HashMap<Id<LogicNode>, LogicNode>();

    @NotNull
    private NodeChildResolver childResolver;

    public CompositeNode(@NotNull NodeChildResolver childResolver) {
        this.childResolver = childResolver;
    }

    @Contract("_->fail")
    private <T> T checkLegalState(RuntimeException e) {
        if (null == map) {
            throw new IllegalStateException("The node is destroyed.");
        }

        throw e;
    }

    private void put0(@NotNull Id<LogicNode> id, @NotNull LogicNode node) {
        try {
            map.put(id, node);
        } catch (RuntimeException e) {
            checkLegalState(e);
        }

        try {
            node.onConnected(this);
        } catch (RuntimeException e) {
            map.remove(id);
            throw e;
        }
    }

    @NotNull
    @Override
    public LogicNode get(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        LogicNode res;

        try {
            res = map.get(id);
        } catch (RuntimeException e) {
            return checkLegalState(e);
        }

        if (null == res) {
            res = childResolver.resolve(id);
            childResolver = childResolver.next();
            put0(id, res);
        }

        return res;
    }

    @NotNull
    @Override
    public <A> LogicNode getOrAdd(
            @NotNull Id<LogicNode> id,
            @NotNull NodeFactory<A> factory,
            @Nullable A arg)
            throws RuntimeException, UnsupportedOperationException {
        LogicNode res;

        try {
            res = map.get(id);
        } catch (RuntimeException e) {
            return checkLegalState(e);
        }

        if (null == res) {
            res = factory.create(arg);
            put0(id, res);
        }

        return res;
    }

    @Override
    public void remove(@NotNull Id<LogicNode> id)
            throws UnsupportedOperationException, IllegalStateException {
        LogicNode removed = map.remove(id);

        if (null != removed) {
            removed.onDisconnected(this);
        }
    }

    @Override
    public void remove(@NotNull LogicNode child)
            throws UnsupportedOperationException, IllegalStateException, IllegalArgumentException {
        Iterator<Map.Entry<Id<LogicNode>, LogicNode>> entryIterator = map.entrySet().iterator();

        while (entryIterator.hasNext()) {
            Map.Entry<Id<LogicNode>, LogicNode> entry = entryIterator.next();

            if (entry.getValue() == child) {
                entryIterator.remove();
                child.onDisconnected(this);
                return;
            }
        }

        throw new IllegalArgumentException();
    }

    @NotNull
    @Override
    public <T> T getComponent() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    protected void onConnected0(@NotNull LogicNode parent) {
        for (Id<LogicNode> childId : childResolver.getUnresolvedIds()) {
            get(childId);
        }
    }

    @Override
    protected void onDisconnected0(@NotNull LogicNode parent) {
        Map<Id<LogicNode>, LogicNode> map = this.map;
        this.map = null;

        Throwable acc = ExceptionAccumulator.init();

        for (Map.Entry<Id<LogicNode>, LogicNode> entry : map.entrySet()) {
            try {
                entry.getValue().onDisconnected(this);
            } catch (Exception e) {
                acc = ExceptionAccumulator.add(acc, e);
            }
        }

        ExceptionAccumulator.<RuntimeException>flush(acc);
    }

    @Override
    public void accept(@NotNull NodeVisitor visitor) {
        for (Map.Entry<Id<LogicNode>, LogicNode> entry : map.entrySet()) {
            visitor.visitChild(entry.getKey(), entry.getValue());
        }
    }
}
