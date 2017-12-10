package io.github.alexeybond.partly_solid_bicycle.game2d.impl.render.target;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.ColorBufferTarget;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.WholeRenderTarget;
import org.jetbrains.annotations.NotNull;

public class FrameBufferTarget implements ColorBufferTarget {
    @NotNull
    private final FrameBuffer frameBuffer;

    public FrameBufferTarget(@NotNull FrameBuffer frameBuffer) {
        this.frameBuffer = frameBuffer;
    }

    @NotNull
    @Override
    public Texture getTexture() {
        return frameBuffer.getColorBufferTexture();
    }

    @Override
    public void begin0() {
        frameBuffer.bind();
    }

    @NotNull
    @Override
    public WholeRenderTarget getUnderlyingTarget() {
        return this;
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
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
        return frameBuffer.getWidth();
    }

    @Override
    public float getVirtualHeight() {
        return frameBuffer.getHeight();
    }

    @Override
    public void updateSizes() {

    }

    @Override
    public void begin() {
        frameBuffer.begin();
    }

    @Override
    public void end() {
        frameBuffer.end();
    }

    @Override
    public void dispose() {
        frameBuffer.dispose();
    }
}
