package io.github.alexeybond.partly_solid_bicycle.game2d.impl.render.target.viewport;

import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget;
import org.jetbrains.annotations.NotNull;

public class FitViewportSizing extends ScalingViewportSizing {
    protected FitViewportSizing(float virtualWidth, float virtualHeight) {
        super(virtualWidth, virtualHeight);
    }

    @Override
    protected float computeScale(@NotNull RenderTarget target) {
        float rx = ((float) target.getWidth()) / virtualWidth;
        float ry = ((float) target.getHeight()) / virtualHeight;
        return Math.min(rx, ry);
    }
}
