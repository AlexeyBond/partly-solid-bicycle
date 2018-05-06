package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node;

import io.github.alexeybond.partly_solid_bicycle.core.impl.util.ExceptionAccumulator;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.child_resolver.CompositeChildResolver;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class GroupNode extends NodeBase {
    private Map<Id<LogicNode>, ChildLogicNode> map = new HashMap<Id<LogicNode>, ChildLogicNode>();

    @NotNull
    private NodeChildResolver childResolver;

    @NotNull
    private final NodePopulator initialPopulator;

    public GroupNode(
            @NotNull NodeChildResolver childResolver,
            @NotNull NodePopulator initialPopulator) {
        this.childResolver = childResolver;
        this.initialPopulator = initialPopulator;
    }

    @Contract("_->fail")
    private <T> T checkLegalState(RuntimeException e) {
        if (null == map) {
            throw new IllegalStateException("The node is destroyed.");
        }

        throw e;
    }

    private void put0(@NotNull Id<LogicNode> id, @NotNull ChildLogicNode node) {
        try {
            map.put(id, node);
        } catch (RuntimeException e) {
            checkLegalState(e);
        }

        try {
            node.onConnected(this, id);
        } catch (RuntimeException e) {
            map.remove(id);
            throw e;
        }
    }

    @NotNull
    @Override
    public LogicNode get(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        ChildLogicNode res;

        try {
            res = map.get(id);
        } catch (RuntimeException e) {
            return checkLegalState(e);
        }

        if (null == res) {
            res = childResolver.resolve(id);
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
        ChildLogicNode res;

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
    public void populate(@NotNull NodePopulator populator) {
        NodeChildResolver prevResolver = this.childResolver;
        this.childResolver = new CompositeChildResolver(prevResolver, populator);

        try {
            populator.populate(this);
        } finally {
            this.childResolver = prevResolver;
        }
    }

    @Override
    public void remove(@NotNull Id<LogicNode> id)
            throws UnsupportedOperationException, IllegalStateException {
        ChildLogicNode removed = map.remove(id);

        if (null != removed) {
            removed.onDisconnected(this);
        }
    }

    @Override
    public void remove(@NotNull LogicNode child)
            throws UnsupportedOperationException, IllegalStateException, IllegalArgumentException {
        Iterator<Map.Entry<Id<LogicNode>, ChildLogicNode>> entryIterator = map.entrySet().iterator();

        while (entryIterator.hasNext()) {
            Map.Entry<Id<LogicNode>, ChildLogicNode> entry = entryIterator.next();

            ChildLogicNode myChild = entry.getValue();

            if (myChild == child) {
                entryIterator.remove();
                myChild.onDisconnected(this);
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
    protected void onConnected0(@NotNull LogicNode parent, Id<LogicNode> id) {
        populate(initialPopulator);
    }

    @Override
    protected void onDisconnected0(@NotNull LogicNode parent) {
        Map<Id<LogicNode>, ChildLogicNode> map = this.map;
        this.map = null;

        Throwable acc = ExceptionAccumulator.init();

        for (Map.Entry<Id<LogicNode>, ChildLogicNode> entry : map.entrySet()) {
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
        for (Map.Entry<Id<LogicNode>, ChildLogicNode> entry : map.entrySet()) {
            visitor.visitChild(entry.getKey(), entry.getValue());
        }
    }
}
