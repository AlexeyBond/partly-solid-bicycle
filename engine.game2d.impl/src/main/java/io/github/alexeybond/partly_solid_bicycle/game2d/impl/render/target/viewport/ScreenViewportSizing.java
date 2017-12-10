package io.github.alexeybond.partly_solid_bicycle.game2d.impl.render.target.viewport;

import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.RenderTarget;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.Viewport;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target.ViewportSizingStrategy;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ViewportSizingStrategy} implementing the same behaviour as
 * {@link com.badlogic.gdx.utils.viewport.ScreenViewport LibGDX's ScreenViewport}
 * but is actually useless as any {@link RenderTarget} implementation should setup a viewport
 * to it's own size on call of {@link RenderTarget#begin()}.
 */
public class ScreenViewportSizing implements ViewportSizingStrategy {
    @Override
    public void setup(@NotNull Viewport viewport, @NotNull RenderTarget target) {
        viewport.setViewport(0, 0, target.getWidth(), target.getHeight());
    }
}
