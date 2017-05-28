package com.github.alexeybond.gdx_commons.drawing;

import com.badlogic.gdx.graphics.Texture;

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

    Texture asColorTexture();

    int width();

    int height();

    /**
     * Called by application when the target will no longer be used.
     *
     * Will be useful for pooling of FBO's for complex post-effects.
     */
    void unuse();
}
