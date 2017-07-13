package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.helpers.Subscription;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;

import static com.github.alexeybond.gdx_commons.game.systems.box2d_physics.helpers.BodyAnchorsHelper.*;

public class RevoluteJointComponent extends BaseJointComponent<RevoluteJoint, RevoluteJointDef> {
    private final Vector2 anchor;
    private final boolean isAnchorLocal;

    private Subscription<FloatProperty> motorSpeedSub
            = new Subscription<FloatProperty>() {
        @Override
        public boolean onTriggered(FloatProperty event) {
            joint().setMotorSpeed(event.get());

            return true;
        }
    };

    private Subscription<FloatProperty> maxMotorTorqueSub
            = new Subscription<FloatProperty>() {
        @Override
        public boolean onTriggered(FloatProperty event) {
            joint().setMaxMotorTorque(event.get());

            return true;
        }
    };

    private Subscription<BooleanProperty> motorEnabledSub
            = new Subscription<BooleanProperty>() {
        @Override
        public boolean onTriggered(BooleanProperty event) {
            joint().enableMotor(event.get());

            return true;
        }
    };

    public RevoluteJointComponent(
            String entityATag, String entityBTag,
            RevoluteJointDef def,
            Vector2 anchor,
            boolean isAnchorLocal) {
        super(entityATag, entityBTag, def);
        this.anchor = anchor;
        this.isAnchorLocal = isAnchorLocal;
    }

    @Override
    public void onConnect(Entity entity) {
        motorEnabledSub.set(entity.events()
                .event("motorEnabled", BooleanProperty.make(false)),
                false);
        motorSpeedSub.set(entity.events()
                .event("motorSpeed", FloatProperty.make(0f)),
                false);
        maxMotorTorqueSub.set(entity.events()
                .event("maxMotorTorque", FloatProperty.make(0f)),
                false);

        super.onConnect(entity);
    }

    @Override
    public void create() {
        super.create();

        motorEnabledSub.enable();
        motorSpeedSub.enable();
        maxMotorTorqueSub.enable();
    }

    @Override
    public void onDisconnect(Entity entity) {
        motorEnabledSub.clear();
        motorSpeedSub.clear();
        maxMotorTorqueSub.clear();

        super.onDisconnect(entity);
    }

    @Override
    protected RevoluteJointDef setupJointDef(RevoluteJointDef jointDef) {
        jointDef = super.setupJointDef(jointDef);

        jointDef.localAnchorA.set(localAnchor(jointDef.bodyA, anchor, isAnchorLocal));
        jointDef.localAnchorB.set(localAnchor(
                jointDef.bodyB,
                globalAnchor(jointDef.bodyA, anchor, isAnchorLocal),
                false));

        jointDef.referenceAngle = jointDef.bodyA.getAngle() - jointDef.bodyB.getAngle();

        return jointDef;
    }
}
