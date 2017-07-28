package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.decl;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.alexeybond.partly_solid_bicycle.drawing.sprite.SpriteInstance;
import com.github.alexeybond.partly_solid_bicycle.drawing.sprite.renderer.AccurateSpriteInstance;
import com.github.alexeybond.partly_solid_bicycle.drawing.sprite.renderer.StandardSpriteInstance;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.StaticSpriteComponent;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;

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
    public Component create(GameDeclaration gameDeclaration, Game game) {
        SpriteInstance spriteInst = resolveInstance();
        return new StaticSpriteComponent(pass, spriteInst, resolveRegion(), scale);
    }

    protected TextureRegion resolveRegion() {
        if (null == region) region = IoC.resolve("get texture region", sprite);
        return region;
    }

    protected SpriteInstance resolveInstance() {
        return accurate ? new AccurateSpriteInstance() : new StandardSpriteInstance();
    }
}
