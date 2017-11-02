package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeAwareScopeMember;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeOwner;

public interface Entity
        extends ScopeOwner<Scope<Component, Entity>>,
                ScopeAwareScopeMember<Entity, Scope<Entity, World>> {
}
