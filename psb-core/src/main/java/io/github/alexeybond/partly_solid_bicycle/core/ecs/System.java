package io.github.alexeybond.partly_solid_bicycle.core.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.common.scope.NotifiedScopeMember;
import io.github.alexeybond.partly_solid_bicycle.core.common.scope.Scope;

public interface System extends NotifiedScopeMember<System, Scope<System>> {
}
