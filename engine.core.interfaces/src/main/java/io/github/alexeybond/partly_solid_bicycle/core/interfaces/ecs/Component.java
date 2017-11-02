package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ForwardAwareScopeMember;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.NotifiedScopeMember;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;

public interface Component
        extends NotifiedScopeMember<Component, Entity>,
                ForwardAwareScopeMember<Component, Scope<Component, Entity>> {
}
