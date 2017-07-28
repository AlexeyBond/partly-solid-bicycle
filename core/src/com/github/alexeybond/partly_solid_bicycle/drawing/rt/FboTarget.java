package com.github.alexeybond.partly_solid_bicycle.drawing.rt;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.github.alexeybond.partly_solid_bicycle.drawing.RenderTarget;

/**
 *
 */
public class FboTarget implements RenderTarget {
    private final FrameBuffer fbo;
    private final TextureRegion region = new TextureRegion();

    public FboTarget(FrameBuffer fbo) {
        this.fbo = fbo;
        this.region.setTexture(fbo.getColorBufferTexture());
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
    public float width() {
        return getPixelsWidth();
    }

    @Override
    public float height() {
        return getPixelsHeight();
    }

    @Override
    public int getPixelsWidth() {
        return fbo.getWidth();
    }

    @Override
    public int getPixelsHeight() {
        return fbo.getHeight();
    }

    @Override
    public void unuse() {
    }
}
