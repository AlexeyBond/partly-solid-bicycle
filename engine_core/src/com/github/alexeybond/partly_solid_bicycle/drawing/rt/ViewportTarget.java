package com.github.alexeybond.partly_solid_bicycle.drawing.rt;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.alexeybond.partly_solid_bicycle.drawing.RenderTarget;

/**
 *
 */
public class ViewportTarget implements RenderTarget {
    private RenderTarget superTarget;
    private Viewport viewport;
    private TextureRegion region = new TextureRegion();

    public ViewportTarget(RenderTarget superTarget, Viewport viewport) {
        this.superTarget = superTarget;
        this.viewport = viewport;
    }

    @Override
    public void begin() {
        superTarget.begin();
        viewport.apply(true);
    }

    @Override
    public void end() {
        superTarget.end();
    }

    @Override
    public TextureRegion asColorTexture() {
        TextureRegion superRegion = superTarget.asColorTexture();
        region.setTexture(superRegion.getTexture());
        region.setRegion(
                viewport.getScreenX(), viewport.getScreenY(),
                viewport.getScreenWidth(), viewport.getScreenHeight()
                );
        return region;
    }

    @Override
    public float width() {
        return viewport.getWorldWidth();
    }

    @Override
    public float height() {
        return viewport.getWorldHeight();
    }

    @Override
    public int getPixelsWidth() {
        return viewport.getScreenWidth();
    }

    @Override
    public int getPixelsHeight() {
        return viewport.getScreenHeight();
    }

    @Override
    public void unuse() {
//        superTarget.unuse();
    }
}
