package com.github.alexeybond.partly_solid_bicycle.drawing.rt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.alexeybond.partly_solid_bicycle.drawing.RenderTarget;

/**
 *
 */
public enum ScreenTarget implements RenderTarget {
    INSTANCE;

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    @Override
    public TextureRegion asColorTexture() {
        throw new IllegalStateException("Cannot access screen target as texture.");
    }

    @Override
    public float width() {
        return getPixelsWidth();
    }

    @Override
    public float height() {
        return getPixelsWidth();
    }

    @Override
    public int getPixelsWidth() {
        return Gdx.graphics.getWidth();
    }

    @Override
    public int getPixelsHeight() {
        return Gdx.graphics.getHeight();
    }

    @Override
    public void unuse() {

    }
}
