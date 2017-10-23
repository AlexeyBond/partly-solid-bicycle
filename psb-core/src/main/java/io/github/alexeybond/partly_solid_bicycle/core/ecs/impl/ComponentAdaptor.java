package io.github.alexeybond.partly_solid_bicycle.core.ecs.impl;

import io.github.alexeybond.partly_solid_bicycle.core.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.ecs.Component;
import io.github.alexeybond.partly_solid_bicycle.core.ecs.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class ComponentAdaptor implements Component {
    @Override
    public void onDispose(@NotNull Entity scope, @NotNull Id<Component> id) {
        onLeave(scope, id);
    }
}
