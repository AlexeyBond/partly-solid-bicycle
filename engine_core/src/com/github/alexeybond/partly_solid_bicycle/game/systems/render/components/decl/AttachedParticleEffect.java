package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.decl;

import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.AttachedContinuousParticleEffect;

/**
 *
 */
public class AttachedParticleEffect implements ComponentDeclaration {
    /** Pass where to draw particles */
    public String pass = "game-particles";

    /** Particle effect name (file name) */
    public String effect;

    public float offsetX = 0;
    public float offsetY = 0;
    public float rotate = 0;
    public float[] offset = new float[0];

    public boolean continuous = true;

    /** Name of boolean property that enables the effect */
    public String masterProperty = "enableParticles";

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        if (continuous) {
            return new AttachedContinuousParticleEffect(
                    pass, effect, getOffset(), rotate, masterProperty);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private Vector2 getOffset() {
        if (offset.length != 0) {
            return new Vector2(offset[0], offset[1]);
        } else {
            return new Vector2(offsetX, offsetY);
        }
    }
}
