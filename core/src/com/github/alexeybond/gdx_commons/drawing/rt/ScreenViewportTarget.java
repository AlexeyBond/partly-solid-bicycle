package com.github.alexeybond.gdx_commons.drawing.rt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.github.alexeybond.gdx_commons.drawing.RenderTarget;

/**
 *
 */
public class ScreenViewportTarget implements RenderTarget {
    private final Rectangle viewportRect;

    public ScreenViewportTarget(Rectangle viewportRect) {
        this.viewportRect = viewportRect;
    }

    @Override
    public void begin() {
        Gdx.gl.glViewport(
                (int) viewportRect.x,
                (int) viewportRect.y,
                (int) viewportRect.width,
                (int) viewportRect.height);
    }

    @Override
    public void end() {

    }

    @Override
    public Texture asColorTexture() {
        throw new IllegalStateException("Cannot access screen region as a texture.");
    }

    @Override
    public int width() {
        return (int) viewportRect.width;
    }

    @Override
    public int height() {
        return (int) viewportRect.height;
    }

    @Override
    public void unuse() {

    }
}
