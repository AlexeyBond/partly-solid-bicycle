package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components.decl;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components.GenericTrigger;

/**
 *
 */
public class GenericTriggerDecl implements ComponentDeclaration {
    public String ownerEvent;
    public String targetEvent;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new GenericTrigger(ownerEvent, targetEvent);
    }
}
