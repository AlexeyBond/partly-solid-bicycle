package io.github.alexeybond.partly_solid_bicycle.core.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.common.scope.ScopeAwareScopeMember;

/**
 * Component of {@link Entity entity} that contains set of another entities and is a native world for those entities.
 */
public interface InnerWorld extends World, Component, ScopeAwareScopeMember<Component, Entity> {
}
