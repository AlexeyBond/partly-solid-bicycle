package io.github.alexeybond.partly_solid_bicycle.game2d.render.target;

import com.badlogic.gdx.utils.Disposable;
import org.jetbrains.annotations.NotNull;

/**
 * Render target is a axis-aligned rectangle of a screen or a framebuffer (or of some other object
 * that may be used for the same purposes).
 */
public interface RenderTarget extends Disposable {
    /**
     * Returns a {@link RenderTarget} that represents the whole area of a surface region of which
     * this {@link RenderTarget} represents. May return {@code this} if this {@link RenderTarget}
     * represents the whole area.
     * <p>
     * <p>
     * {@link #getAbsOffsetX()} and {@link #getAbsOffsetY()} of returned {@link RenderTarget}
     * always return {@code 0}.
     * </p>
     */
    @NotNull
    WholeRenderTarget getUnderlyingTarget();

    /**
     * @return width of this target in pixels
     */
    int getWidth();

    /**
     * @return height of this target in pixels
     */
    int getHeight();

    /**
     * @return offset of the left edge of rectangle represented by this target from the left edge
     * of {@link RenderTarget target} returned by {@link #getUnderlyingTarget()}
     */
    int getAbsOffsetX();


    /**
     * @return offset of the bottom edge of rectangle represented by this target from the bottom edge
     * of {@link RenderTarget target} returned by {@link #getUnderlyingTarget()}
     */
    int getAbsOffsetY();

    /**
     * @return virtual width of this target
     */
    float getVirtualWidth();

    /**
     * @return virtual height of this target
     */
    float getVirtualHeight();

    /**
     * Recompute values returned by {@link #getWidth()}, {@link #getHeight()}, {@link #getAbsOffsetX()},
     * {@link #getAbsOffsetY()}.
     */
    void updateSizes();

    /**
     * Begin drawing to this target.
     */
    void begin();

    /**
     * End drawing to this target.
     */
    void end();
}
