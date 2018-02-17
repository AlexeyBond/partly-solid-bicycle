package io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.TreeContext;
import org.jetbrains.annotations.NotNull;

public abstract class NodeBase implements LogicNode {
    private LogicNode parent;

    private boolean connecting;

    protected NodeBase() {
        connecting = false;
    }

    @NotNull
    @Override
    public LogicNode getParent() {
        LogicNode parent = this.parent;
        if (null == parent) throw new IllegalStateException();
        return parent;
    }

    @Override
    public final void onConnected(@NotNull LogicNode parent, @NotNull Id<LogicNode> id) {
        if (null != this.parent) {
            throw new IllegalStateException("Node is already connected.");
        }

        if (this == parent) {
            throw new IllegalArgumentException("This node cannot connect to itself.");
        }

        this.parent = parent;

        this.connecting = true;

        try {
            onConnected0(parent, id);
        } finally {
            this.connecting = false;
        }
    }

    @Override
    public final void onDisconnected(@NotNull LogicNode parent) {
        if (null == this.parent) {
            throw new IllegalStateException();
        }

        if (connecting) {
            throw new IllegalStateException();
        }

        if (this.parent != parent) {
            throw new IllegalArgumentException("Wrong parent node.");
        }

        try {
            onDisconnected0(parent);
        } finally {
            this.parent = null;
        }
    }

    @NotNull
    @Override
    public TreeContext getTreeContext() {
        return getParent().getTreeContext();
    }

    protected abstract void onConnected0(@NotNull LogicNode parent, Id<LogicNode> id);

    protected abstract void onDisconnected0(@NotNull LogicNode parent);
}
