package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.target;

import com.badlogic.gdx.graphics.Texture;
import org.jetbrains.annotations.NotNull;

/**
 * {@link RenderTarget} that is a framebuffer.
 */
public interface ColorBufferTarget extends RenderTarget, WholeRenderTarget {
    /**
     * @return content of this buffer represented as a texture
     */
    @NotNull
    Texture getTexture();
}
