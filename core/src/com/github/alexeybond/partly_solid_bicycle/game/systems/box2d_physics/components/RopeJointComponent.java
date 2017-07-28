package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

import static com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.helpers.BodyAnchorsHelper.*;

public class RopeJointComponent extends BaseJointComponent<RopeJoint, RopeJointDef> {
    private final Vector2 anchorA, anchorB;
    private final boolean isLocalA, isLocalB;
    private final float length, relLength;

    public RopeJointComponent(
            String entityATag, String entityBTag,
            RopeJointDef def,
            Vector2 anchorA, Vector2 anchorB,
            boolean isLocalA, boolean isLocalB,
            float length, float relLength) {
        super(entityATag, entityBTag, def);
        this.anchorA = anchorA;
        this.anchorB = anchorB;
        this.isLocalA = isLocalA;
        this.isLocalB = isLocalB;
        this.length = length;
        this.relLength = relLength;
    }

    @Override
    protected RopeJointDef setupJointDef(RopeJointDef jointDef) {
        jointDef = super.setupJointDef(jointDef);

        jointDef.localAnchorA.set(localAnchor(jointDef.bodyA, anchorA, isLocalA));
        jointDef.localAnchorB.set(localAnchor(jointDef.bodyB, anchorB, isLocalB));
        jointDef.maxLength =
                relLength * globalAnchor(jointDef.bodyA, anchorA, isLocalA)
                        .dst(globalAnchor(jointDef.bodyB, anchorB, isLocalB)) +
                        length;

        return jointDef;
    }
}
