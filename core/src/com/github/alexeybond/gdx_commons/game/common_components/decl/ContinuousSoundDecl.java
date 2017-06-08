package com.github.alexeybond.gdx_commons.game.common_components.decl;

import com.badlogic.gdx.audio.Sound;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.common_components.ContinuousEventSound;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.ioc.IoC;

/**
 *
 */
public class ContinuousSoundDecl implements ComponentDeclaration {
    public String sound;
    public String event;

    @Override
    public Component create(GameDeclaration gameDeclaration) {
        Sound soundObj = IoC.resolve("load sound", sound);
        return new ContinuousEventSound(soundObj, event);
    }
}
