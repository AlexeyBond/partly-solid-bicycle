package com.github.alexeybond.gdx_commons.game.systems.render.components.decl;

import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.render.components.AttachedContinuousParticleEffect;

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

    /** Name of boolean property that enables the effect */
    public String masterProperty = "enableParticles";

    @Override
    public Component create() {
        return new AttachedContinuousParticleEffect(
                pass, effect, new Vector2(offsetX, offsetY), rotate, masterProperty);
    }
}
