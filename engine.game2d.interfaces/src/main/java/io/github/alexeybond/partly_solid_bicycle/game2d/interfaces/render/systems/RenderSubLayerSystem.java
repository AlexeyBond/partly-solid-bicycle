package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.systems;

import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene.SubLayer;
import org.jetbrains.annotations.NotNull;

/**
 * A game system that wraps a scene sub-layer.
 *
 * @param <T> sub-layer type
 */
public interface RenderSubLayerSystem<T extends SubLayer> {
    @NotNull
    T getSubLayer();
}
