package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene.sublayers;

import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene.SubLayer;
import org.jetbrains.annotations.NotNull;

/**
 * Collection sub-layer is a {@link SubLayer} containing a collection of items and knowing
 * how to draw them.
 *
 * @param <T> item type
 */
public interface CollectionSubLayer<T> extends SubLayer {
    void addItem(@NotNull T item);

    void removeItem(@NotNull T item);
}
