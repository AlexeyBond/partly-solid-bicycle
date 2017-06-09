package com.github.alexeybond.gdx_commons.game.common_components.decl;

import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.common_components.SpawnOnEvent;
import com.github.alexeybond.gdx_commons.game.common_components.SpawnOnEventNearToObserver;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.EntityDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.util.DeclUtils;

/**
 *
 */
public class SpawnOnEventDecl implements ComponentDeclaration {
    public String[] classes;
    public String event;
    public float rotation = 0;
    public float offsetX = 0;
    public float offsetY = 0;
    public float[] offset = new float[0];

    public String observerTag = null;
    public float maxObserverDistance = 1000;

    private transient EntityDeclaration[] classDecls;
    private transient Vector2 offsetV;

    @Override
    public Component create(GameDeclaration gameDeclaration) {
        offsetV = DeclUtils.readVector(offsetV, offset, offsetX, offsetY);
        classDecls = DeclUtils.readClasses(classDecls, gameDeclaration, classes);

        if (null != observerTag) {
            return new SpawnOnEventNearToObserver(
                    event, offsetV, rotation, classDecls, gameDeclaration, observerTag,
                    maxObserverDistance
            );
        }

        return new SpawnOnEvent(event, offsetV, rotation, classDecls, gameDeclaration);
    }
}
