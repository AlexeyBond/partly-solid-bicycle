package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene;

public interface SubLayerFactory<T extends SubLayer> {
    T create();
}
