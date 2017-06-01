package com.github.alexeybond.gdx_commons.drawing.rt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.github.alexeybond.gdx_commons.drawing.RenderTarget;

/**
 *
 */
public class FboTarget implements RenderTarget {
    private final FrameBuffer fbo;
    private final TextureRegion region = new TextureRegion();

    public FboTarget(FrameBuffer fbo) {
        this.fbo = fbo;
        this.region.setRegion(0,0,fbo.getWidth(),fbo.getHeight());
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
    public TextureRegion asColorTexture() {
        // Texture may change on context invalidation
        region.setTexture(fbo.getColorBufferTexture());
        return region;
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
