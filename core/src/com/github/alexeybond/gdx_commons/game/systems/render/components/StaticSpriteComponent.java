package com.github.alexeybond.gdx_commons.game.systems.render.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;

/**
 *
 */
// TODO:: Sprite uses SIN/COS tables with low accuracy what causes artifacts on some objects. Add (optional) mode that uses more accurate SIN/COS computation.
public class StaticSpriteComponent extends BaseRenderComponent {
    private final Sprite sprite;

    public StaticSpriteComponent(String passName, TextureRegion region, float scale) {
        super(passName);

        sprite = new Sprite(region);
        sprite.setScale(scale);
        sprite.setOriginCenter();
    }

    @Override
    public void draw(DrawingContext context) {
        Vector2 pos = position.ref();
        float rot = rotation.get();

        Batch batch = context.state().beginBatch();

        if (rot != sprite.getRotation()) sprite.setRotation(rot);
        sprite.setCenter(pos.x, pos.y);

        sprite.draw(batch);
    }
}
