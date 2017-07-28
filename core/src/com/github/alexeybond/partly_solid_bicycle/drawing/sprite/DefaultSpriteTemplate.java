package com.github.alexeybond.partly_solid_bicycle.drawing.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 */
public class DefaultSpriteTemplate implements SpriteTemplate {
    private final TextureRegion region;
    private final float originX, originY, scale, rotation;

    public DefaultSpriteTemplate(
            TextureRegion region,
            float originX, float originY,
            float scale,
            float rotation) {
        this.region = region;
        this.originX = originX;
        this.originY = originY;
        this.scale = scale;
        this.rotation = rotation;
    }

    @Override
    public TextureRegion region() {
        return region;
    }

    @Override
    public float originX() {
        return originX;
    }

    @Override
    public float originY() {
        return originY;
    }

    @Override
    public float scale() {
        return scale;
    }

    @Override
    public float rotation() {
        return rotation;
    }
}
