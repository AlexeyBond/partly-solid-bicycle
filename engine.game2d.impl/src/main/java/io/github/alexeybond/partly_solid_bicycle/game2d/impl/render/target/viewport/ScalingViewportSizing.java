package io.github.alexeybond.partly_solid_bicycle.game2d.impl.render.target.viewport;

import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.Viewport;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.ViewportSizingStrategy;
import org.jetbrains.annotations.NotNull;

public abstract class ScalingViewportSizing implements ViewportSizingStrategy {
    protected final float virtualWidth, virtualHeight, ratio;

    protected ScalingViewportSizing(float virtualWidth, float virtualHeight) {
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;

        ratio = virtualWidth / virtualHeight;
    }

    @Override
    public void setup(@NotNull Viewport viewport, @NotNull RenderTarget target) {
        float scale = computeScale(target);

        int tw = target.getWidth(), th = target.getHeight();
        int width = (int) (scale * virtualWidth), height = (int) (scale * virtualHeight);
        int x = (tw - width) / 2, y = (th - height) / 2;
        viewport.setViewport(x, y, width, height);

        viewport.setVirtualSize(virtualWidth, virtualHeight);
    }

    protected abstract float computeScale(@NotNull RenderTarget target);
}
