package com.github.alexeybond.partly_solid_bicycle.game.common_components.decl;

import com.badlogic.gdx.audio.Sound;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.common_components.ContinuousEventSound;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;

/**
 *
 */
public class ContinuousSoundDecl implements ComponentDeclaration {
    public String sound;
    public String event;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        Sound soundObj = IoC.resolve("load sound", sound);
        return new ContinuousEventSound(soundObj, event);
    }
}
