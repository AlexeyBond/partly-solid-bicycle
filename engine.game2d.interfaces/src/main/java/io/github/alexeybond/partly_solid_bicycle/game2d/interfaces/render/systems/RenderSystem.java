package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.systems;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.System;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene.Scene;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link System game system} that wraps a scene.
 */
public interface RenderSystem extends System {
    @NotNull
    Scene getScene();
}
