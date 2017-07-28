package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.decl;

import com.github.alexeybond.partly_solid_bicycle.drawing.animation.decl.AnimationDecl;
import com.github.alexeybond.partly_solid_bicycle.drawing.animation.impl.Animation;
import com.github.alexeybond.partly_solid_bicycle.drawing.sprite.SpriteInstance;
import com.github.alexeybond.partly_solid_bicycle.drawing.sprite.renderer.AccurateSpriteInstance;
import com.github.alexeybond.partly_solid_bicycle.drawing.sprite.renderer.StandardSpriteInstance;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.AnimatedSpriteComponent;

/**
 *
 */
public class AnimatedSprite implements ComponentDeclaration {
    public AnimationDecl animation;

    public String pass = "game-objects";

    public float scale = 1f;

    /** If true compute sprite coordinates more accurate way */
    public boolean accurate = false;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        Animation animation = this.animation.getAnimation();
        SpriteInstance spriteInst = accurate ? new AccurateSpriteInstance() : new StandardSpriteInstance();
        return new AnimatedSpriteComponent(pass, spriteInst, scale, animation);
    }
}
