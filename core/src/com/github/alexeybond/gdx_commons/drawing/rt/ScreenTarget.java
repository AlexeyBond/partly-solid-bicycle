package com.github.alexeybond.gdx_commons.drawing.rt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.alexeybond.gdx_commons.drawing.RenderTarget;

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
    public int width() {
        return Gdx.graphics.getWidth();
    }

    @Override
    public int height() {
        return Gdx.graphics.getHeight();
    }

    @Override
    public void unuse() {

    }
}
