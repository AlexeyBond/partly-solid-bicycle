package com.github.alexeybond.gdx_gm2.test_game.game.components;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Transform;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.BaseBodyComponent;
import com.github.alexeybond.gdx_commons.game.systems.render.interfaces.RenderComponent;
import com.github.alexeybond.gdx_commons.game.systems.render.RenderSystem;
import com.github.alexeybond.gdx_commons.game.systems.timing.TimingSystem;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;

/**
 * Uses the following entity properties:
 *
 * <ul>
 *     <li>"rightEngineControl" - right engine key event</li>
 *     <li>"leftEngineControl" - left engine key event</li>
 *     <li>"rightEngineEnabled" - if right engine animation should be enabled</li>
 *     <li>"leftEngineEnabled" - if left engine animation should be enabled</li>
 *     <li>"fuel" - available fuel amount</li>
 *     <li>"fuelConsumption" - fuel consumption of one engine per second</li>
 *     <li>"engineImpulse" - absolute impulse created by engines</li>
 *     <li>"engineOffset" - offset of engines by X axis</li>
 * </ul>
 */
public class SpaceshipEngines
        implements Component, EventListener<FloatProperty>,
        RenderComponent {
    private BooleanProperty rightEngineControl;
    private BooleanProperty leftEngineControl;
    private BooleanProperty rightEngineEnabled;
    private BooleanProperty leftEngineEnabled;
    private FloatProperty dtProperty;
    private BaseBodyComponent bodyComponent;

    private FloatProperty engineImpulse;
    private FloatProperty engineOffset;

    private FloatProperty fuel;
    private FloatProperty fuelConsumption;

    private int dtSubId;

    private final Vector2
            pointLeft = new Vector2(),
            pointRight = new Vector2(),
            impulse = new Vector2();

    @Override
    public void onConnect(Entity entity) {
        rightEngineControl = entity.events().event("rightEngineControl", BooleanProperty.make());
        leftEngineControl = entity.events().event("leftEngineControl", BooleanProperty.make());
        rightEngineEnabled = entity.events().event("rightEngineEnabled", BooleanProperty.make());
        leftEngineEnabled = entity.events().event("leftEngineEnabled", BooleanProperty.make());

        engineImpulse = entity.events().event("engineImpulse", FloatProperty.make(1000));
        engineOffset = entity.events().event("engineOffset", FloatProperty.make(50));
        fuel = entity.events().event("fuel", FloatProperty.make());
        fuelConsumption = entity.events().event("fuelConsumption", FloatProperty.make(1));

        dtProperty = entity.game().systems().<TimingSystem>get("timing").events().event("deltaTime");
        dtSubId = dtProperty.subscribe(this);

        bodyComponent = entity.components().get("body");

        entity.game().systems().<RenderSystem>get("render").addToPass("game-debug", this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        dtSubId = dtProperty.unsubscribe(dtSubId);
    }

    @Override
    public boolean onTriggered(FloatProperty deltaTime) {
        float fuelLeft = fuel.get();

        if (fuelLeft <= 0) {
            rightEngineEnabled.set(false);
            leftEngineEnabled.set(false);

            return false;
        } else {
            rightEngineEnabled.set(rightEngineControl.get());
            leftEngineEnabled.set(leftEngineControl.get());
        }

        Body body = bodyComponent.body();
        Transform transform = body.getTransform();

        impulse
                .set(0, engineImpulse.get())
                .rotate(MathUtils.radiansToDegrees * transform.getRotation());

        if (rightEngineControl.get()) {
            transform.mul(pointRight.set(engineOffset.get(), 0));
            body.applyLinearImpulse(impulse, pointRight, true);
            fuelLeft -= fuelConsumption.get() * deltaTime.get();
        }

        if (leftEngineControl.get()) {
            transform.mul(pointLeft.set(-engineOffset.get(), 0));
            body.applyLinearImpulse(impulse, pointLeft, true);
            fuelLeft -= fuelConsumption.get() * deltaTime.get();
        }

        fuel.set(Math.max(0f, fuelLeft));

        return true;
    }

    /** Debug drawing... */
    @Override
    public void draw(DrawingContext context) {
        ShapeRenderer shapeRenderer = context.state().beginLines();

        Vector2 pt = new Vector2();

        shapeRenderer.line(pointLeft, pt.set(impulse).setLength(100).add(pointLeft));
        shapeRenderer.line(pointRight, pt.set(impulse).setLength(100).add(pointRight));
    }
}
