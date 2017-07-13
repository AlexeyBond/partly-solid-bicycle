package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.util.DeclUtils;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.RevoluteJointComponent;

public class RevoluteJointDecl implements ComponentDeclaration {
    public String entity1, entity2;

    public float x = 0, y = 0;

    public float[] anchor = null;

    public boolean localAnchor = true;

    public float[] limits = null;

    public boolean collideConnected = false;

    private transient RevoluteJointDef jointDef = null;
    private Vector2 anchorV = null;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new RevoluteJointComponent(
                entity1, entity2,
                createDef(jointDef),
                anchorV = DeclUtils.readVector(anchorV, anchor, x, y),
                localAnchor
        );
    }

    private RevoluteJointDef createDef(RevoluteJointDef present) {
        if (null != present) return present;

        RevoluteJointDef jointDef = new RevoluteJointDef();

        jointDef.collideConnected = collideConnected;

        if (null != limits) {
            jointDef.enableLimit = true;

            jointDef.lowerAngle = MathUtils.degreesToRadians * limits[0];
            jointDef.upperAngle = MathUtils.degreesToRadians * limits[1];
        } else {
            jointDef.enableLimit = false;
        }

        return this.jointDef = jointDef;
    }
}
