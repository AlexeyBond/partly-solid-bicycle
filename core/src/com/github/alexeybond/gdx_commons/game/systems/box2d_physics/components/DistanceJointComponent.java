package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

public class DistanceJointComponent extends BaseJointComponent<DistanceJointDef> {
    private final DistanceJointDef jointDef = new DistanceJointDef();

    private final Vector2 anchorA, anchorB;
    private final boolean isLocalA, isLocalB;

    public DistanceJointComponent(
            String entityATag, String entityBTag,
            DistanceJointDef def,
            Vector2 anchorA, Vector2 anchorB,
            boolean isLocalA, boolean isLocalB) {
        super(entityATag, entityBTag, def);
        this.anchorA = anchorA;
        this.anchorB = anchorB;
        this.isLocalA = isLocalA;
        this.isLocalB = isLocalB;
    }

    @Override
    protected DistanceJointDef setupJointDef(DistanceJointDef jointDef) {
        jointDef = super.setupJointDef(jointDef);

        jointDef.localAnchorA.set(localAnchor(jointDef.bodyA, anchorA, isLocalA));
        jointDef.localAnchorB.set(localAnchor(jointDef.bodyB, anchorB, isLocalB));
        jointDef.length = globalAnchor(jointDef.bodyA, anchorA, isLocalA).dst(globalAnchor(jointDef.bodyB, anchorB, isLocalB));

        return jointDef;
    }

    private Vector2 localAnchor(Body body, Vector2 anchor, boolean isLocal) {
        if (isLocal) return anchor;
        return body.getLocalPoint(anchor);
    }

    private Vector2 globalAnchor(Body body, Vector2 anchor, boolean isLocal) {
        if (!isLocal) return anchor;
        return body.getWorldPoint(anchor);
    }
}
