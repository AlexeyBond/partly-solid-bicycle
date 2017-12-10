package io.github.alexeybond.partly_solid_bicycle.game2d.impl.render.target;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.Viewport;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.ViewportSizingStrategy;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.WholeRenderTarget;
import org.jetbrains.annotations.NotNull;

public class ViewportTarget implements RenderTarget {
    private final RenderTarget underlying;
    private final ViewportSizingStrategy sizingStrategy;
    private int vpX, vpY, vpWidth, vpHeight;
    private int pubX, pubY, pubWidth, pubHeight;
    private boolean scissors;
    private float vWidth, vHeight;

    private final Viewport viewport = new Viewport() {
        @Override
        public void setViewport(int x, int y, int width, int height) {
            pubX = vpX = x + underlying.getAbsOffsetX();
            pubY = vpY = y + underlying.getAbsOffsetY();
            vWidth = pubWidth = vpWidth = width;
            vHeight = pubHeight = vpHeight = height;

            scissors = false;

            if (x < 0) {
                pubX = underlying.getAbsOffsetX();
                scissors = true;
            }

            if (y < 0) {
                pubY = underlying.getAbsOffsetY();
                scissors = true;
            }

            if (x + width > underlying.getWidth()) {
                pubWidth = (underlying.getWidth() + underlying.getAbsOffsetX()) - pubX;
                scissors = true;
            }

            if (y + height > underlying.getHeight()) {
                pubHeight = (underlying.getHeight() + underlying.getAbsOffsetY()) - pubY;
                scissors = true;
            }
        }

        @Override
        public void setVirtualSize(float vWidth, float vHeight) {
            ViewportTarget.this.vWidth = vWidth;
            ViewportTarget.this.vHeight = vHeight;
        }
    };

    public ViewportTarget(RenderTarget underlying, ViewportSizingStrategy sizingStrategy) {
        this.underlying = underlying;
        this.sizingStrategy = sizingStrategy;

        updateSizes();
    }

    private void setupScissors() {
        GL20 gl = Gdx.gl;
        gl.glEnable(GL20.GL_SCISSOR_TEST);
        gl.glScissor(pubX, pubY, pubWidth, pubHeight);
    }

    private void clearScissors() {
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }

    @NotNull
    @Override
    public WholeRenderTarget getUnderlyingTarget() {
        return underlying.getUnderlyingTarget();
    }

    @Override
    public int getWidth() {
        return pubWidth;
    }

    @Override
    public int getHeight() {
        return pubHeight;
    }

    @Override
    public int getAbsOffsetX() {
        return pubX;
    }

    @Override
    public int getAbsOffsetY() {
        return pubY;
    }

    @Override
    public float getVirtualWidth() {
        return vWidth;
    }

    @Override
    public float getVirtualHeight() {
        return vHeight;
    }

    @Override
    public void updateSizes() {
        underlying.updateSizes();
        sizingStrategy.setup(viewport, underlying);
    }

    @Override
    public void begin() {
        updateSizes();
        underlying.getUnderlyingTarget().begin0();
        Gdx.gl.glViewport(vpX, vpY, vpWidth, vpHeight);

        if (scissors) setupScissors();
    }

    @Override
    public void end() {
        if (scissors) clearScissors();

        underlying.getUnderlyingTarget().end();
    }

    @Override
    public void dispose() {
        underlying.dispose();
    }
}
