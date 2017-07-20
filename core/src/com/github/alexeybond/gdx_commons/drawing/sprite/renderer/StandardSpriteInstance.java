package com.github.alexeybond.gdx_commons.drawing.sprite.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.drawing.sprite.SpriteInstance;
import com.github.alexeybond.gdx_commons.drawing.sprite.SpriteTemplate;

/**
 *
 */
public class StandardSpriteInstance implements SpriteInstance {
    private final Sprite sprite = new Sprite();
    private SpriteTemplate lastTemplate;

    @Override
    public void draw(Batch batch, SpriteTemplate template, Vector2 position, float scale, float rotation) {
        if (lastTemplate != template) {
            lastTemplate = template;

            TextureRegion region = template.region();
            sprite.setRegion(template.region());
            sprite.setSize(region.getRegionWidth(), region.getRegionHeight());
            sprite.setOrigin(template.originX(), template.originY());
        }

        rotation += template.rotation();
        scale *= template.scale();

        if (scale != sprite.getScaleX()) sprite.setScale(scale);
        if (rotation != sprite.getRotation()) sprite.setRotation(rotation);
        sprite.setPosition(position.x - sprite.getOriginX(), position.y - sprite.getOriginY());

        sprite.setColor(batch.getPackedColor());
        sprite.draw(batch);
    }
}
