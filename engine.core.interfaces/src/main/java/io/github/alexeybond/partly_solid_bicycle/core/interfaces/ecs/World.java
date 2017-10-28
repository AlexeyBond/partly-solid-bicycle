package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;

public interface World
        extends Scope<Entity> {
    Engine getEngine();

    Entity getRootEntity();
}