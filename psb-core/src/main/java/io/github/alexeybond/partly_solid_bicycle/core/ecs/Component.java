package io.github.alexeybond.partly_solid_bicycle.core.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.common.scope.ForwardAwareScopeMember;
import io.github.alexeybond.partly_solid_bicycle.core.common.scope.NotifiedScopeMember;

public interface Component
        extends NotifiedScopeMember<Component, Entity>, ForwardAwareScopeMember<Component, Entity> {
}
