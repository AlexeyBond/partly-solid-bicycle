package com.github.alexeybond.gdx_gm2.test_game.test3.components;

import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.CollisionData;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.BaseBodyComponent;
import com.github.alexeybond.gdx_commons.game.systems.timing.TimingSystem;
import com.github.alexeybond.gdx_commons.util.event.helpers.Subscription;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

/**
 *
 */
public class PlatformerPlayerController implements Component {
    private float jumpVelocity = 50;
    private float walkVelocity = 50;
    private float airWalkVelocity = 20;

    private Entity entity;
    private BaseBodyComponent bodyComponent;

    private final Subscription<TimingSystem, FloatProperty<TimingSystem>> deltaTimeSub
            = new Subscription<TimingSystem, FloatProperty<TimingSystem>>() {
        @Override
        public boolean onTriggered(TimingSystem timingSystem, FloatProperty<TimingSystem> event) {
            update(event.get());
            return true;
        }
    };

    private final Subscription<Component, ObjectProperty<CollisionData, Component>> groundCollisionBeginSub
            = new Subscription<Component, ObjectProperty<CollisionData, Component>>() {
        @Override
        public boolean onTriggered(Component component, ObjectProperty<CollisionData, Component> event) {
            if (checkIgnoreGroundCollision(event.get().that.entity())) return false;
            ++sensorCollisionCount;
            return true;
        }
    };

    private final Subscription<Component, ObjectProperty<CollisionData, Component>> groundCollisionEndSub
            = new Subscription<Component, ObjectProperty<CollisionData, Component>>() {
        @Override
        public boolean onTriggered(Component component, ObjectProperty<CollisionData, Component> event) {
            if (checkIgnoreGroundCollision(event.get().that.entity())) return false;
            --sensorCollisionCount;
            return true;
        }
    };

    private BooleanProperty<Component> goRightControl, goLeftControl, jumpControl;
    private Vec2Property<Component> positionProp;

    private int sensorCollisionCount = 0;
    private boolean jumping;

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        bodyComponent = entity.components().get("body");

        deltaTimeSub.set(entity.game().systems().<TimingSystem>get("timing").events()
                .<FloatProperty<TimingSystem>>event("deltaTime"));

        goRightControl = entity.events().event("goRightControl", BooleanProperty.<Component>make());
        goLeftControl = entity.events().event("goLeftControl", BooleanProperty.<Component>make());
        jumpControl = entity.events().event("jumpControl", BooleanProperty.<Component>make());
        positionProp = entity.events().event("position", Vec2Property.<Component>make());

        groundCollisionBeginSub.set(entity.events()
                .event("groundCollisionBegin", ObjectProperty.<CollisionData, Component>make()));
        groundCollisionEndSub.set(entity.events()
                .event("groundCollisionEnd", ObjectProperty.<CollisionData, Component>make()));

        sensorCollisionCount = 0;
    }

    @Override
    public void onDisconnect(Entity entity) {
        deltaTimeSub.clear();
        groundCollisionBeginSub.clear();
        groundCollisionEndSub.clear();
    }

    private void update(float dt) {
        Vector2 velocity = bodyComponent.body().getLinearVelocity();

        final boolean grounded = isGrounded();

        if (grounded && jumpControl.get()) {
            if (!jumping && velocity.y > -jumpVelocity) {
                velocity.y = Math.max(jumpVelocity, velocity.y + jumpVelocity);
                jumping = true;
            }
        } else {
            jumping = false;
        }

        int vxi = 0;

        if (goRightControl.get()) ++vxi;
        if (goLeftControl.get()) --vxi;

        if (vxi != 0) {
            float targetAbsVelocity = grounded ? walkVelocity : airWalkVelocity;
            float targetVelocity = targetAbsVelocity * (float) vxi;

            float targetVelocityDelta = targetVelocity - velocity.x;

            if (Math.signum(targetVelocityDelta) == Math.signum(targetVelocity)) {
                velocity.x = targetVelocity;
            }
        } else {
            if (grounded && Math.abs(velocity.x) < walkVelocity) {
                velocity.x = 0;
            }
        }

        bodyComponent.body().setLinearVelocity(velocity);
    }

    private boolean checkIgnoreGroundCollision(Entity entity) {
        if (this.entity == entity) return true;

        return false;
    }

    private boolean isGrounded() {
        return sensorCollisionCount > 0;
    }

    public static class Decl implements ComponentDeclaration {

        @Override
        public Component create(GameDeclaration gameDeclaration) {
            return new PlatformerPlayerController();
        }
    }
}
