package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.*;

public interface Entity
        extends ScopeOwner<Scope<Component>>,
                ScopeAwareScopeMember<Entity, World> {
}
