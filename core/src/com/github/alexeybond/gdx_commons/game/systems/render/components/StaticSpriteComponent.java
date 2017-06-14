package com.github.alexeybond.gdx_commons.game.systems.render.components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.alexeybond.gdx_commons.drawing.sprite.DefaultSpriteTemplate;
import com.github.alexeybond.gdx_commons.drawing.sprite.SpriteInstance;
import com.github.alexeybond.gdx_commons.drawing.sprite.SpriteTemplate;

/**
 *
 */
public class StaticSpriteComponent extends SpriteComponent {
    private final SpriteTemplate template;

    public StaticSpriteComponent(String passName, SpriteInstance sprite, TextureRegion region, float scale) {
        super(passName, sprite, scale);
        template = new DefaultSpriteTemplate(
                region, .5f * (float)region.getRegionWidth(), .5f * (float)region.getRegionHeight(), 1, 0);
    }

    @Override
    public SpriteTemplate getTemplate() {
        return template;
    }
}
