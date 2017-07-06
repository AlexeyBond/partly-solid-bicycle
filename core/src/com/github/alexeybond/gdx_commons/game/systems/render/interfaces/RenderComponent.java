package com.github.alexeybond.gdx_commons.game.systems.render.interfaces;

import com.github.alexeybond.gdx_commons.drawing.Drawable;
import com.github.alexeybond.gdx_commons.game.Component;

/**
 * A render component is a single {@link Drawable}, added (probably) only to one pass.
 *
 * Entity may have multiple render components.
 *
 * Drawable may also setup projection (camera position, zoom and rotation).
 */
public interface RenderComponent extends Drawable, Component {
}
