package io.github.alexeybond.partly_solid_bicycle.core.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.common.scope.MutableScope;
import io.github.alexeybond.partly_solid_bicycle.core.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.common.scope.ScopeWithRemove;

import java.lang.System;

public interface World
        extends MutableScope<Entity>,
                ScopeWithRemove<Entity> {
    Scope<System> getSystemScope();
}
