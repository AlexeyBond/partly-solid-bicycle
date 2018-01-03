package io.github.alexeybond.partly_solid_bicycle.game2d.impl.render.scene;

import com.badlogic.gdx.utils.ObjectMap;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.DrawingState;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene.Layer;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene.Scene;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene.SceneRenderer;
import org.jetbrains.annotations.NotNull;

public class DefaultSceneImpl implements Scene {
    @NotNull
    private final SceneRenderer sceneRenderer;

    private ObjectMap<String, DefaultLayerImpl> layers = new ObjectMap<String, DefaultLayerImpl>();

    public DefaultSceneImpl(@NotNull SceneRenderer sceneRenderer) {
        this.sceneRenderer = sceneRenderer;

        sceneRenderer.init(this);
    }

    @NotNull
    @Override
    public Layer getLayer(@NotNull String id) throws IllegalArgumentException {
        DefaultLayerImpl layer = layers.get(id);
        if (null == layer) throw new IllegalArgumentException("No layer named '" + id + "' found.");
        return layer;
    }

    @NotNull
    @Override
    public Layer createLayer(@NotNull String id) throws IllegalStateException {
        DefaultLayerImpl layer = layers.get(id);
        if (null != layer) return layer;
        layer = new DefaultLayerImpl();
        layers.put(id, layer);
        return layer;
    }

    @Override
    public void dispose() {
        for (DefaultLayerImpl layer : layers.values()) {
            layer.dispose();
        }

        layers.clear();
    }

    @Override
    public void draw(@NotNull DrawingState state) {
        sceneRenderer.render(this, state);
    }
}
