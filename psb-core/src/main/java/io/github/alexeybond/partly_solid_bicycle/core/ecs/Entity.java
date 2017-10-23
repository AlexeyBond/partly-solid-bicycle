package io.github.alexeybond.partly_solid_bicycle.core.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.common.scope.ScopeAwareScopeMember;
import io.github.alexeybond.partly_solid_bicycle.core.common.scope.ScopeWithRemove;

public interface Entity extends ScopeWithRemove<Component>, ScopeAwareScopeMember<Entity, World> {
}
