package com.github.alexeybond.gdx_gm2.test_game.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.BaseBodyComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces.APhysicsSystem;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces.DisposablePhysicsComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces.UpdatablePhysicsComponent;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;
import com.github.alexeybond.gdx_commons.util.parts.exceptions.PartConnectRejectedException;

/**
 * Component added to a entity attracted by gravity of some other entity.
 */
public class GravityAttractionEffect
        implements Component, UpdatablePhysicsComponent, DisposablePhysicsComponent {
    private final Vec2Property ownerPositionProperty;
    private final BooleanProperty enableEvent;
    private final float massProduct;

    private APhysicsSystem system;
    private Body affectedBody;
    private boolean alive = false;

    private Vector2 tmp = new Vector2();

    public GravityAttractionEffect(
            Vec2Property ownerPositionProperty,
            BooleanProperty enableEvent,
            float massProduct) {
        this.ownerPositionProperty = ownerPositionProperty;
        this.enableEvent = enableEvent;
        this.massProduct = massProduct;
    }

    @Override
    public void onConnect(Entity entity) {
        try {
            affectedBody = entity.components().<BaseBodyComponent>get("body").body();
        } catch (Exception e) {
            throw new PartConnectRejectedException("Body not found.", e);
        }
        system = entity.game().systems().get("physics");
        system.registerComponent(this);
        alive = true;
    }

    @Override
    public void onDisconnect(Entity entity) {
        alive = false;
        system.disposeComponent(this);
    }

    @Override
    public void dispose() {
        // No actual physical objects created so do nothing
    }

    @Override
    public void update() {
        if (!enableEvent.get()) return;

        tmp.set(ownerPositionProperty.ref()).sub(affectedBody.getPosition());

        float absForce = massProduct / tmp.len2();

        affectedBody.applyLinearImpulse(
                tmp.setLength2(1).scl(absForce),
                affectedBody.getPosition(),
                true);
    }

    @Override
    public boolean isAlive() {
        return alive;
    }
}
