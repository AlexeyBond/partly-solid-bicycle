package io.github.alexeybond.partly_solid_bicycle.core.impl.ecs;

import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.DefaultScope;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy.LazyMemberReference;
import io.github.alexeybond.partly_solid_bicycle.core.impl.scope.lazy.LazyReferenceProvider;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Engine;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.System;
import org.jetbrains.annotations.NotNull;

public class DefaultEngine
        extends DefaultScope<System, LazyMemberReference<System>, Engine>
        implements Engine {
    private static final LazyReferenceProvider<System> REFERENCE_PROVIDER
            = new LazyReferenceProvider<System>();

    public DefaultEngine(@NotNull Scope<System, ?> superScope) {
        super(REFERENCE_PROVIDER, superScope);
    }

    @NotNull
    @Override
    public Scope<System, Engine> getScope() {
        return this;
    }

    @NotNull
    @Override
    public Engine getOwner() {
        return this;
    }
}
