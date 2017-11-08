package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.components;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeAwareScopeMember;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Component;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Entity;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.World;

/**
 * Component of {@link Entity entity} that also is a world containing other entities.
 */
public interface InnerWorldComponent extends
        World,
        Component,
        ScopeAwareScopeMember<Component, Scope<Component, Entity>> {
}
