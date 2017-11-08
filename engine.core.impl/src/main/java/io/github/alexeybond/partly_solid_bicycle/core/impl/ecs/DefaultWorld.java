package io.github.alexeybond.partly_solid_bicycle.core.impl.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.DefaultScope;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.ReferenceProvider;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy.LazyMemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy.LazyReferenceProvider;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Component;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Engine;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Entity;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.World;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.components.InnerWorldComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Default implementation of {@link World}.
 *
 * <p>
 *  This class also implements {@link InnerWorldComponent} so it always is a component of some entity.
 * </p>
 */
public class DefaultWorld
            extends DefaultScope<Entity, LazyMemberReference<Entity>, World>
            implements InnerWorldComponent {
    private static final ReferenceProvider<Entity, LazyMemberReference<Entity>> REFERENCE_PROVIDER
            = new LazyReferenceProvider<Entity>();

    private final Engine engine;

    private Entity root;

    public DefaultWorld(
            @NotNull Scope<Entity, ?> superScope,
            @NotNull Engine engine) {
        super(REFERENCE_PROVIDER, superScope);
        this.engine = engine;
    }

    @NotNull
    @Override
    public Engine getEngine() {
        return engine;
    }

    @NotNull
    @Override
    public Entity getRootEntity() {
        return root;
    }

    @NotNull
    @Override
    public Scope<Entity, World> getScope() {
        return this;
    }

    @NotNull
    @Override
    public World getOwner() {
        return this;
    }

    @Nullable
    @Override
    public Component forward(
            @NotNull Entity from,
            @NotNull Entity to) {
        return this;
    }

    @Override
    public void onJoin(@NotNull Entity owner, @NotNull Id<Component> id)
            throws Exception {
        this.root = owner;
    }

    @Override
    public void onLeave(@NotNull Entity owner, @NotNull Id<Component> id)
            throws Exception {
        // TODO:: Notify entities
        this.root = null;
    }

    @Override
    public void onDispose(@NotNull Entity owner, @NotNull Id<Component> id)
            throws Exception {
        // TODO:: Notify entities
        this.root = null;
    }

    @NotNull
    @Override
    public Scope<Component, Entity> getNativeScope() throws IllegalStateException {
        return getRootEntity().getScope();
    }
}
