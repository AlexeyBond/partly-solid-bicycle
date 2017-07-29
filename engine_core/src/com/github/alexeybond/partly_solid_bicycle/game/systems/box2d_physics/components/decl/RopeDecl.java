package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components.decl;

import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.EntityDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.util.DeclUtils;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components.RopeComponent;

public class RopeDecl implements ComponentDeclaration {
    public float segmentLength = 10f;

    public float width = 10f;

    public float segmentAngleLimit = 45f;

    public float density = 0.01f;

    public float friction = 1f;

    public float restitution = 0.1f;

    public int segmentCount = 2;

    public String startEntity;
    public float startX = 0, startY = 0;
    public float[] startAnchor = null;
    public boolean startLocal = true;

    public String endSpawnClass = null;

    private transient Vector2 startAnchorV;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        startAnchorV = DeclUtils.readVector(startAnchorV, startAnchor, startX, startY);

        EntityDeclaration endSpawnDeclaration = null;
        if (null != endSpawnClass) endSpawnDeclaration = gameDeclaration.getEntityClass(endSpawnClass);

        return new RopeComponent(
                segmentLength,
                width,
                segmentAngleLimit,
                density,
                friction,
                restitution,
                segmentCount,
                startEntity, startAnchorV, startLocal,
                gameDeclaration,
                endSpawnDeclaration);
    }
}
