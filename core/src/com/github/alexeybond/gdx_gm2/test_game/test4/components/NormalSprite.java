package com.github.alexeybond.gdx_gm2.test_game.test4.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.drawing.sprite.SpriteInstance;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.render.components.StaticSpriteComponent;
import com.github.alexeybond.gdx_commons.game.systems.render.components.decl.StaticSprite;

/**
 * Works like a static sprite component but passes additional information to shader through vertex color.
 */
public class NormalSprite extends StaticSpriteComponent {
    public NormalSprite(String passName, SpriteInstance sprite, TextureRegion region, float scale) {
        super(passName, sprite, region, scale);
    }

    @Override
    public void draw(DrawingContext context) {
        Batch batch = context.state().beginBatch();
        setupColor(batch);
        super.draw(context);
        batch.setColor(Color.WHITE);
    }

    private void setupColor(Batch batch) {
        float canonAngle = canonicalAngle(rotation.get());
        float angleMajor = (float)Math.floor(canonAngle * 256f) / 256f;
        float angleMinor = (float)Math.floor((canonAngle - angleMajor) * 256f * 256f) / 256f;

        batch.setColor(angleMajor, angleMinor, 0 , 0);
    }

    private final static float DEG_FULL = 360f;
    private final static float DEG_FULL_INV = 1f / DEG_FULL;

    /**
     * Convert angle in degrees to value in range [0,1) where 0 is 0 degrees and 1 is 360 degrees.
     */
    private static float canonicalAngle(float angle) {
        if (angle >= DEG_FULL) {
            angle = angle % DEG_FULL;
        } else if (angle < 0) {
            if (angle <= -MathUtils.PI2) {
                angle = angle % DEG_FULL;
            }

            angle = angle + DEG_FULL;
        }

        return angle * DEG_FULL_INV;
    }

    public static class Decl extends StaticSprite implements ComponentDeclaration {
        @Override
        public Component create(GameDeclaration gameDeclaration, Game game) {
            return new NormalSprite(pass, resolveInstance(), resolveRegion(), scale);
        }
    }
}
