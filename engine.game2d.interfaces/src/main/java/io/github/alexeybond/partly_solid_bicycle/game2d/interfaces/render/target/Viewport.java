package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target;

public interface Viewport {
    /**
     * Set viewport rect.
     * <p>
     * <p>
     * Virtual sizes are set to real sizes.
     * </p>
     *
     * @param x offset of the left edge of this viewport from the left edge of master target
     * @param y offset of the bottom edge of this viewport from the bottom edge of master target
     */
    void setViewport(int x, int y, int width, int height);

    /**
     * Set virtual size of viewport.
     * <p>
     * <p>
     * When called before {@link #setViewport(int, int, int, int)} the resulting viewport size is
     * undefined.
     * </p>
     */
    void setVirtualSize(float vWidth, float vHeight);
}
