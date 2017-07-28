package com.github.alexeybond.partly_solid_bicycle.drawing;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Render target representable as a texture.
 *
 * Default implementation uses FBO ({@link com.badlogic.gdx.graphics.glutils.FrameBuffer}) but
 * implementation that uses texture copying may (?) become necessary.
 */
public interface RenderTarget {
    /**
     * Start drawing to the target.
     */
    void begin();

    /**
     * End drawing to the target.
     */
    void end();

    TextureRegion asColorTexture();

    /** @return (virtual) width of the target; may be different from actual size in pixels */
    float width();

    /** @return (virtual) height of the target; may be different from actual size in pixels */
    float height();

    /** @return (real) width of the target in pixels */
    int getPixelsWidth();

    /** @return (real) height of the target in pixels */
    int getPixelsHeight();

    /**
     * Called by application when the target will no longer be used.
     *
     * Will be useful for pooling of FBO's for complex post-effects.
     */
    void unuse();
}
