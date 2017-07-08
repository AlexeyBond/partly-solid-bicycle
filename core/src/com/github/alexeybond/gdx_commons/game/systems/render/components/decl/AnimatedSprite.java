package com.github.alexeybond.gdx_commons.game.systems.render.components.decl;

import com.github.alexeybond.gdx_commons.drawing.animation.decl.AnimationDecl;
import com.github.alexeybond.gdx_commons.drawing.animation.impl.Animation;
import com.github.alexeybond.gdx_commons.drawing.sprite.SpriteInstance;
import com.github.alexeybond.gdx_commons.drawing.sprite.renderer.AccurateSpriteInstance;
import com.github.alexeybond.gdx_commons.drawing.sprite.renderer.StandardSpriteInstance;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.render.components.AnimatedSpriteComponent;

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
