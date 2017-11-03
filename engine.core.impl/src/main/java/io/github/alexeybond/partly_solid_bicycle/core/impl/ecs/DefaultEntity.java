package io.github.alexeybond.partly_solid_bicycle.core.impl.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.DefaultScope;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.ReferenceProvider;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy.LazyMemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy.LazyReferenceProvider;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Component;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Entity;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.World;
import org.jetbrains.annotations.NotNull;

public class DefaultEntity
        extends DefaultScope<Component, LazyMemberReference<Component>, Entity>
        implements Entity {
    private static final ReferenceProvider<Component, LazyMemberReference<Component>> REFERENCE_PROVIDER
            = new LazyReferenceProvider<Component>();

    private World world;

    public DefaultEntity(@NotNull Scope<Component, ?> superScope) {
        super(REFERENCE_PROVIDER, superScope);
    }

    @NotNull
    @Override
    public Scope<Entity, World> getNativeScope() throws IllegalStateException {
        try {
            return world.getScope();
        } catch (NullPointerException e) {
            throw new IllegalStateException("Not part of any world.", e);
        }
    }

    @NotNull
    @Override
    public Scope<Component, Entity> getScope() {
        return this;
    }

    @NotNull
    @Override
    public Entity getOwner() {
        return this;
    }

    @Override
    public void onJoin(@NotNull World owner, @NotNull Id<Entity> id)
            throws Exception {
        this.world = owner;
    }

    @Override
    public void onLeave(@NotNull World owner, @NotNull Id<Entity> id)
            throws Exception {
        // TODO:: Notify components
        this.world = null;
    }

    @Override
    public void onDispose(@NotNull World owner, @NotNull Id<Entity> id)
            throws Exception {
        // TODO:: Notify components
        this.world = null;
    }
}
