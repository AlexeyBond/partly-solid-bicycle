package com.github.alexeybond.partly_solid_bicycle.drawing.sprite;

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
