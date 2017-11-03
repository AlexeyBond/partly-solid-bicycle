package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeOwner;
import org.jetbrains.annotations.NotNull;

public interface World
        extends ScopeOwner<Scope<Entity, World>> {
    @NotNull
    Engine getEngine();

    @NotNull
    Entity getRootEntity();
}
