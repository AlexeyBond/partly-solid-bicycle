package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.helpers.Subscription;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;

import static com.github.alexeybond.gdx_commons.game.systems.box2d_physics.helpers.BodyAnchorsHelper.*;

public class DistanceJointComponent extends BaseJointComponent<DistanceJoint, DistanceJointDef> {
    private final Vector2 anchorA, anchorB;
    private final boolean isLocalA, isLocalB;

    // TODO:: Add properties for damping and frequency
    private final Subscription<FloatProperty> lengthSub
            = new Subscription<FloatProperty>() {
        @Override
        public boolean onTriggered(FloatProperty event) {
            joint().setLength(event.get());
            return true;
        }
    };

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
    public void onConnect(Entity entity) {
        lengthSub.set(entity.events().event("jointLength", FloatProperty.make()), false);

        super.onConnect(entity);
    }

    @Override
    public void onDisconnect(Entity entity) {
        super.onDisconnect(entity);

        lengthSub.clear();
    }

    @Override
    public void create() {
        super.create();

        lengthSub.enable();
    }

    @Override
    protected DistanceJointDef setupJointDef(DistanceJointDef jointDef) {
        jointDef = super.setupJointDef(jointDef);

        jointDef.localAnchorA.set(localAnchor(jointDef.bodyA, anchorA, isLocalA));
        jointDef.localAnchorB.set(localAnchor(jointDef.bodyB, anchorB, isLocalB));
        jointDef.length = globalAnchor(jointDef.bodyA, anchorA, isLocalA).dst(globalAnchor(jointDef.bodyB, anchorB, isLocalB));

        return jointDef;
    }
}
