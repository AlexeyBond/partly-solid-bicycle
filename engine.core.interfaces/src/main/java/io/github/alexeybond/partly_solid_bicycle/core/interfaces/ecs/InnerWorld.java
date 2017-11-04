package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeAwareScopeMember;

/**
 * Component of {@link Entity entity} that also is a world containing other entities.
 */
public interface InnerWorld extends
        World,
        Component,
        ScopeAwareScopeMember<Component, Scope<Component, Entity>> {
}
