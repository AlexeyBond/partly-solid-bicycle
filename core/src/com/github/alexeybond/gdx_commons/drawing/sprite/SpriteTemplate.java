package com.github.alexeybond.gdx_commons.drawing.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 */
public interface SpriteTemplate {
    TextureRegion region();
    float originX();
    float originY();
    float scale();
    float rotation();
}
