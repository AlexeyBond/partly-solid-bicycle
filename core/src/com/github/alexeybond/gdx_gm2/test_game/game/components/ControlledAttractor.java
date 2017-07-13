package com.github.alexeybond.gdx_gm2.test_game.game.components;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.timing.TimingSystem;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;

/**
 *
 */
public class ControlledAttractor implements Component {
    private FloatProperty deltaTime;
    private BooleanProperty attractorControl;
    private BooleanProperty attractorEnabled;
    private FloatProperty fuelProperty;
    private FloatProperty fuelConsumptionProperty;

    private int controlSubIdx, timeSubIdx;

    private final GravityTrigger gravityTrigger
            = new GravityTrigger(
                    "attractorHitBegin",
                    "attractorHitEnd",
                    "toPlayerAttractor",
                    "attractorEnabled");

    @Override
    public void onConnect(final Entity entity) {
        attractorControl = entity.events().event("attractorControl", BooleanProperty.make());
        attractorEnabled = entity.events().event("attractorEnabled", BooleanProperty.make(false));

        entity.events().event("attractorEnabled", BooleanProperty.make(false));

        fuelProperty = entity.events().event("fuel", FloatProperty.<Component>make());
        fuelConsumptionProperty = entity.events().event("attractorFuelConsumption", FloatProperty.<Component>make(2));

        deltaTime = entity.game().systems().<TimingSystem>get("timing").events().event("deltaTime");

        entity.components().add("gravitator", gravityTrigger);

        controlSubIdx = attractorControl.subscribe(new EventListener<Event>() {
            @Override
            public boolean onTriggered(Event event) {
                return attractorEnabled.set(
                        attractorControl.get() && fuelProperty.get() > 0);
            }
        });

        timeSubIdx = deltaTime.subscribe(new EventListener<Event>() {
            @Override
            public boolean onTriggered(Event event) {
                if (attractorEnabled.get()) {
                    float fuelRemains = Math.max(0, fuelProperty.get() - fuelConsumptionProperty.get() * deltaTime.get());
                    fuelProperty.set(fuelRemains);

                    if (fuelRemains <= 0) {
                        attractorEnabled.set(false);
                    }
                } else if (attractorControl.get() && fuelProperty.get() > 0) {
                    attractorEnabled.set(true);
                }
                return true;
            }
        });
    }

    @Override
    public void onDisconnect(Entity entity) {
        attractorEnabled.set(false);
        controlSubIdx = attractorControl.unsubscribe(controlSubIdx);
        timeSubIdx = deltaTime.unsubscribe(timeSubIdx);
    }
}
