package io.github.alexeybond.partly_solid_bicycle.game2d.impl.render.target;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.WholeRenderTarget;
import org.jetbrains.annotations.NotNull;

public class ScreenTarget implements WholeRenderTarget {
    private int width, height;

    public ScreenTarget() {
        updateSizes();
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
        return width;
    }

    @Override
    public float getVirtualHeight() {
        return height;
    }

    @Override
    public void updateSizes() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    @Override
    public void begin() {
        begin0();
        updateSizes();
        Gdx.gl.glViewport(0, 0, width, height);
    }

    @Override
    public void end() {

    }

    @NotNull
    @Override
    public WholeRenderTarget getUnderlyingTarget() {
        return this;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void begin0() {
        GLFrameBuffer.unbind();
    }

    @Override
    public void dispose() {

    }
}
