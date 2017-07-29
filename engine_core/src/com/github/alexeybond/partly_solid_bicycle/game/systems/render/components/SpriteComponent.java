package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components;

import com.github.alexeybond.partly_solid_bicycle.drawing.DrawingContext;
import com.github.alexeybond.partly_solid_bicycle.drawing.sprite.SpriteInstance;
import com.github.alexeybond.partly_solid_bicycle.drawing.sprite.SpriteTemplate;

/**
 *
 */
public abstract class SpriteComponent extends BaseRenderComponent {
    private final SpriteInstance sprite;
    private final float scale;

    protected SpriteComponent(String passName, SpriteInstance sprite, float scale) {
        super(passName);
        this.sprite = sprite;
        this.scale = scale;
    }

    @Override
    public void draw(DrawingContext context) {
        sprite.draw(
                context.state().beginBatch(),
                getTemplate(),
                position.ref(),
                scale,
                rotation.get());
    }

    protected abstract SpriteTemplate getTemplate();
}
