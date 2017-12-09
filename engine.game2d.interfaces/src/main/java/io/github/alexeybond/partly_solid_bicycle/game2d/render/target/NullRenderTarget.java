package io.github.alexeybond.partly_solid_bicycle.game2d.render.target;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link RenderTarget} that has size 0x0 pixels and is impossible to render to.
 */
public enum NullRenderTarget implements RenderTarget, WholeRenderTarget {
    INSTANCE;

    @NotNull
    @Override
    public WholeRenderTarget getUnderlyingTarget() {
        return this;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getAbsOffsetX() {
        return 0;
    }

    @Override
    public int getAbsOffsetY() {
        return 0;
    }

    @Override
    public float getVirtualWidth() {
        return 0;
    }

    @Override
    public float getVirtualHeight() {
        return 0;
    }

    @Override
    public void updateSizes() {

    }

    @Override
    public void begin() {
        throw new IllegalStateException("Cannot draw to NULL-target.");
    }

    @Override
    public void begin0() {
        begin();
    }

    @Override
    public void end() {
        throw new IllegalStateException("Cannot draw to NULL-target.");
    }

    @Override
    public void dispose() {

    }
}
