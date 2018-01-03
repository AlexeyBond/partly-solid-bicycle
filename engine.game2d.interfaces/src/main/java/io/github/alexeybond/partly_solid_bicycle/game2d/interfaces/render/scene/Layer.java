package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene;

import com.badlogic.gdx.utils.Disposable;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.Drawable;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.DrawingState;
import org.jetbrains.annotations.NotNull;

/**
 * One layer of a scene.
 *
 * @see Scene
 */
public interface Layer extends Drawable, Disposable {
    /**
     * Get or create a {@link SubLayer sub-layer} with given priority and name.
     */
    <T extends SubLayer> T subLayer(int priority, String id, SubLayerFactory<T> factory);

    /**
     * Disposes all sub-layers.
     */
    @Override
    void dispose();

    /**
     * Draws all sub-layers in order defined by their priorities.
     */
    @Override
    void draw(@NotNull DrawingState state);
}
