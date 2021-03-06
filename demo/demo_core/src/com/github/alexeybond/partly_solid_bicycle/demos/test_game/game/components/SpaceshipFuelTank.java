package com.github.alexeybond.partly_solid_bicycle.demos.test_game.game.components;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventListener;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.FloatProperty;

/**
 *
 */
public class SpaceshipFuelTank
        implements Component, EventListener<FloatProperty> {
    private FloatProperty capacityProp;
    private FloatProperty fuelProp;
    private FloatProperty fuelPickEvent;
    private int fuelPickSubIdx = -1;

    @Override
    public void onConnect(Entity entity) {
        capacityProp = entity.events()
                .event("fuelCapacity", FloatProperty.make());
        fuelProp = entity.events()
                .event("fuel", FloatProperty.make());
        fuelPickEvent = entity.events()
                .event("fuelPicked", FloatProperty.make());
        fuelPickSubIdx = fuelPickEvent.subscribe(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        fuelPickSubIdx = fuelPickEvent.unsubscribe(fuelPickSubIdx);
    }

    @Override
    public boolean onTriggered(FloatProperty event) {
        fuelProp.set(Math.min(capacityProp.get(), fuelProp.get() + event.get()));

        return true;
    }
}
