package com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces;

import com.github.alexeybond.partly_solid_bicycle.drawing.Drawable;
import com.github.alexeybond.partly_solid_bicycle.game.Component;

/**
 * A render component is a single {@link Drawable}, added (probably) only to one pass.
 *
 * Entity may have multiple render components.
 *
 * Drawable may also setup projection (camera position, zoom and rotation).
 */
public interface RenderComponent extends Drawable, Component {
}
