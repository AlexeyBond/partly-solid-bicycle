package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.impl;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Component;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class ComponentAdaptor implements Component {
    @Override
    public void onDispose(@NotNull Entity scope, @NotNull Id<Component> id) {
        onLeave(scope, id);
    }
}
