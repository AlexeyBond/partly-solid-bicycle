package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.components;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.Scope;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.scope.ScopeOwner;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Component;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventSource;

/**
 * {@link Component} that contains a scope of {@link EventSource event sources}.
 */
public interface EventScopeComponent
        extends Component,
                ScopeOwner<Scope<EventSource, EventScopeComponent>> {
}
