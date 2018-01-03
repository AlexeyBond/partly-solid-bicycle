package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene;

import com.badlogic.gdx.utils.Disposable;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.Drawable;
import org.jetbrains.annotations.NotNull;

/**
 * Scene.
 * <p>
 * <p>
 * Scene consists of named {@link Layer layers}.
 * When (in which order, at which conditions), where (to what target), and how (in what rendering
 * context state) layers are drawn is determined by {@link SceneRenderer}. But
 * {@link SubLayer sub-layers} may change some parts of global state (and are not guaranteed to
 * rollback the changes) so sub-layers and {@link SceneRenderer} become a bit aware of implementation
 * of each other.
 * </p>
 * <p>
 * <p>
 * Each {@link Layer layer} consists of {@link SubLayer sub-layers}.
 * A {@link SubLayer sub-layer} is usually a collection of objects drawn in the same way.
 * For example a sub-layer may be a collection of sprites (all sprites are drawn using a
 * sprite batch), particle systems or debug graphics elements (which will use shape renderer).
 * Sub-layer is identified by a priority (integer) and a name (string). Sub-layers with lower priority
 * are guaranteed to be drawn before sub-layers with higher priority.
 * Drawing order for layers with equal priority is not guaranteed.
 * </p>
 * <p>
 * <p>
 * The way a sub-layer relies with drawable objects makes it alike to a system in ECS pattern
 * (but more specialized for rendering purposes).
 * And it seems to be a nice idea to implement some
 * {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.System systems}
 * wrapping a single sub-layer.
 * </p>
 */
public interface Scene extends Drawable, Disposable {
    /**
     * Get an exist layer.
     *
     * @throws IllegalArgumentException when no layer with given identifier exists
     */
    @NotNull
    Layer getLayer(String id) throws IllegalArgumentException;

    /**
     * Create new {@link Layer layer} or get exist one.
     */
    @NotNull
    Layer createLayer(String id) throws IllegalStateException;

    /**
     * Disposes all layers and other associated resources.
     */
    @Override
    void dispose();
}
