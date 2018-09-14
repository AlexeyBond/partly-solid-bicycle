package io.github.alexeybond.partly_solid_bicycle.core.impl.resource_management.manager;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.resource_management.ResourceTypeManager;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.ChildLogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeChildResolver;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

final class ResourceReferenceNodeResolver<TResource> implements NodeChildResolver {
    @NotNull
    private final ResourceTypeManager<TResource> resourceTypeManager;

    ResourceReferenceNodeResolver(
            @NotNull ResourceTypeManager<TResource> resourceTypeManager
    ) {
        this.resourceTypeManager = resourceTypeManager;
    }

    @NotNull
    @Override
    public ChildLogicNode resolve(@NotNull Id<LogicNode> id) throws NoSuchElementException {
        return new ResourceReferenceNode(
                resourceTypeManager.getResource(
                        id.serializable().toString()
                )
        );
    }
}
