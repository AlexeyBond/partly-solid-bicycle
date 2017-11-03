package io.github.alexeybond.partly_solid_bicycle.core.impl.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.IdentityMemberFactory;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Component;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Engine;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Entity;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.World;
import org.jetbrains.annotations.NotNull;

public enum ECSUtil {;

    /**
     * Create a root entity and a root world.
     *
     * <p>
     *  Root world contains a root entity and at the same time root entity contains a root world as a component.
     * </p>
     *
     * <p>
     *  The universe will collapse if root entity will be removed from a root world or a root world will be
     *  removed from root entity.
     * </p>
     *
     * @param engine              the engine
     * @param entitySuperScope    scope of parent world of root world, probably a {@code NullScope}
     * @param componentSuperScope scope of parent entity of root entity, probably a {@code NullScope}
     * @return the new root world
     * @see io.github.alexeybond.partly_solid_bicycle.core.impl.scope.NullScope
     */
    public static World makeDefaultRootWorld(
            @NotNull Engine engine,
            @NotNull Scope<Entity, ?> entitySuperScope,
            @NotNull Scope<Component, ?> componentSuperScope
            ) {
        DefaultWorld world = new DefaultWorld(entitySuperScope, engine);
        DefaultEntity root = new DefaultEntity(componentSuperScope);

        world.put(world.getIdSet().get("root"), IdentityMemberFactory.<Entity>get(), root);
        root.put(root.getIdSet().get("world"), IdentityMemberFactory.<Component>get(), world);

        return world;
    }
}
