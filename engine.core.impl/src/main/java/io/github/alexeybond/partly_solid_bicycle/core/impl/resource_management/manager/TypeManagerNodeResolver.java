package io.github.alexeybond.partly_solid_bicycle.core.impl.resource_management.manager;

import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.GroupNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.node.NullNode;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.NullPopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.resource_management.ResourceTypeManager;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ChildLogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeChildResolver;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

final class TypeManagerNodeResolver implements NodeChildResolver {
    @NotNull
    private LogicNode managersRoot = NullNode.INSTANCE;

    private <T> ChildLogicNode resolve0(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        ResourceTypeManager<T> manager = managersRoot.get(id).getComponent();

        return new GroupNode(
                new ResourceReferenceNodeResolver<T>(manager),
                NullPopulator.INSTANCE
        );
    }

    @NotNull
    @Override
    public ChildLogicNode resolve(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        return resolve0(id);
    }

    void setManagersRoot(@NotNull LogicNode managersRoot) {
        this.managersRoot = managersRoot;
    }
}
