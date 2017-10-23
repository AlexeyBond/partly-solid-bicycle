package io.github.alexeybond.partly_solid_bicycle.core.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.common.scope.ScopeAwareScopeMember;

/**
 * Component of {@link Entity entity} that contains set of another entities being a native world for them.
 */
public interface InnerWorld extends World, Component, ScopeAwareScopeMember<Component, Entity> {
}
