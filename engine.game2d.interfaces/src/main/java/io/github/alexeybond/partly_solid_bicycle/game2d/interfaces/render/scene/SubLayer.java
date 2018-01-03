package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene;

import com.badlogic.gdx.utils.Disposable;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.Drawable;

/**
 * A sub-layer.
 *
 * @see Scene
 */
public interface SubLayer extends Drawable, Disposable {
    /**
     * Disposes all resources of this sub-layer.
     */
    @Override
    void dispose();
}
