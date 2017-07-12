package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.util.DeclUtils;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.DistanceJointComponent;

public class DistanceJointDecl implements ComponentDeclaration {
    public String entity1, entity2;

    public float[] anchor1 = null, anchor2 = null;

    public float x1 = 0, y1 = 0, x2 = 0, y2 = 0;

    public boolean local1 = true, local2 = true;

    public boolean collideConnected = true;

    private transient Vector2 anchorV1, anchorV2;
    private transient DistanceJointDef jointDef;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        Vector2 a1 = (anchorV1 = DeclUtils.readVector(anchorV1, anchor1, x1, y1));
        Vector2 a2 = (anchorV2 = DeclUtils.readVector(anchorV2, anchor2, x2, y2));
        return new DistanceJointComponent(entity1, entity2, createDef(jointDef), a1, a2, local1, local2);
    }

    private DistanceJointDef createDef(DistanceJointDef present) {
        if (null != present) return present;

        DistanceJointDef jointDef = new DistanceJointDef();

        jointDef.collideConnected = collideConnected;

        return this.jointDef = jointDef;
    }
}
