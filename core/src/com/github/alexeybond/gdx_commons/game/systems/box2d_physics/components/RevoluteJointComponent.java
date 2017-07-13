package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.helpers.Subscription;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;

import static com.github.alexeybond.gdx_commons.game.systems.box2d_physics.helpers.BodyAnchorsHelper.*;

public class RevoluteJointComponent extends BaseJointComponent<RevoluteJoint, RevoluteJointDef> {
    private final Vector2 anchor;
    private final boolean isAnchorLocal;

    private Subscription<Component, FloatProperty<Component>> motorSpeedSub
            = new Subscription<Component, FloatProperty<Component>>() {
        @Override
        public boolean onTriggered(Component component, FloatProperty<Component> event) {
            joint().setMotorSpeed(event.get());

            return true;
        }
    };

    private Subscription<Component, FloatProperty<Component>> maxMotorTorqueSub
            = new Subscription<Component, FloatProperty<Component>>() {
        @Override
        public boolean onTriggered(Component component, FloatProperty<Component> event) {
            joint().setMaxMotorTorque(event.get());

            return true;
        }
    };

    private Subscription<Component, BooleanProperty<Component>> motorEnabledSub
            = new Subscription<Component, BooleanProperty<Component>>() {
        @Override
        public boolean onTriggered(Component component, BooleanProperty<Component> event) {
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
                .event("motorEnabled", BooleanProperty.<Component>make(false)),
                false);
        motorSpeedSub.set(entity.events()
                .event("motorSpeed", FloatProperty.<Component>make(0f)),
                false);
        maxMotorTorqueSub.set(entity.events()
                .event("maxMotorTorque", FloatProperty.<Component>make(0f)),
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
