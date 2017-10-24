package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.MutableScope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeAwareScopeMember;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeWithRemove;

public interface Entity
        extends ScopeWithRemove<Component>,
        MutableScope<Component>,
                ScopeAwareScopeMember<Entity, World> {
}
