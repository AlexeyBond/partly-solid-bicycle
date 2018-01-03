package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene;

import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.DrawingState;

/**
 * Object responsible for rendering the whole {@link Scene}.
 * <p>
 * <p>
 * {@link SceneRenderer} determines order in which scene layers are drawn and rendering context states
 * for each layer (including a
 * {@link io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget render target})
 * the layer is rendered to.
 * </p>
 */
public interface SceneRenderer {
    /**
     * Setup this object to render the given scene.
     * <p>
     * <p>
     * This method must create all necessary layers using {@link Scene#createLayer(String)}.
     * It's also hardly recommended to store created layers in instance fields instead of
     * requiring them every time using {@link Scene#getLayer(String)}.
     * </p>
     */
    void init(Scene scene);

    /**
     * Render scene.
     */
    void render(DrawingState state);
}
