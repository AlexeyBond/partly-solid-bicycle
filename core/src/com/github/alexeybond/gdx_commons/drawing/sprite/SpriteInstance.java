package com.github.alexeybond.gdx_commons.drawing.sprite;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public interface SpriteInstance {
    void draw(Batch batch, SpriteTemplate template, Vector2 position, float scale, float rotation);
}
