package com.github.alexeybond.gdx_commons.drawing.rt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.github.alexeybond.gdx_commons.drawing.RenderTarget;

/**
 *
 */
public class FboTarget implements RenderTarget {
    private final FrameBuffer fbo;

    public FboTarget(FrameBuffer fbo) {
        this.fbo = fbo;
    }

    @Override
    public void begin() {
        fbo.begin();
    }

    @Override
    public void end() {
        fbo.end();
    }

    @Override
    public Texture asColorTexture() {
        return fbo.getColorBufferTexture();
    }

    @Override
    public int width() {
        return fbo.getWidth();
    }

    @Override
    public int height() {
        return fbo.getHeight();
    }

    @Override
    public void unuse() {
    }
}
