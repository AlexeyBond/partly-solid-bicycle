package com.github.alexeybond.gdx_commons.game.systems.render.components.decl;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.render.components.AccurateStaticSpriteComponent;
import com.github.alexeybond.gdx_commons.game.systems.render.components.StaticSpriteComponent;
import com.github.alexeybond.gdx_commons.ioc.IoC;

/**
 *
 */
public class StaticSprite implements ComponentDeclaration {
    private transient TextureRegion region;

    public String pass = "game-objects";

    public String sprite;

    public float scale = 1f;

    /** If true compute sprite coordinates more accurate way */
    public boolean accurate = false;

    @Override
    public Component create(GameDeclaration gameDeclaration) {
        if (null == region) region = IoC.resolve("get texture region", sprite);
        if (accurate) {
            return new AccurateStaticSpriteComponent(pass, region, scale);
        } else {
            return new StaticSpriteComponent(pass, region, scale);
        }
    }
}
