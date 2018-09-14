package io.github.alexeybond.partly_solid_bicycle.core.impl.resource_management.manager;

import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.NullChildNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.resource_management.ResourceReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeVisitor;
import org.jetbrains.annotations.NotNull;

final class ResourceReferenceNode extends NullChildNode {
    @NotNull
    private final ResourceReference resourceReference;

    ResourceReferenceNode(@NotNull ResourceReference resourceReference) {
        this.resourceReference = resourceReference;
    }

    @Override
    protected void onDisconnected0(@NotNull LogicNode parent) {
        resourceReference.dispose();
    }

    @NotNull
    @Override
    @SuppressWarnings({"unchecked"})
    public <T> T getComponent() {
        return (T) resourceReference;
    }

    @Override
    public void accept(@NotNull NodeVisitor visitor) {
        visitor.visitComponent(resourceReference);
    }
}
