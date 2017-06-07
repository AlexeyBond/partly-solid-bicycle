package com.github.alexeybond.gdx_gm2.test_game.game.components;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;

/**
 *
 */
public class SpaceshipFuelTank
        implements Component, EventListener<Component, FloatProperty<Component>> {
    private FloatProperty<Component> capacityProp;
    private FloatProperty<Component> fuelProp;
    private FloatProperty<Component> fuelPickEvent;
    private int fuelPickSubIdx = -1;

    @Override
    public void onConnect(Entity entity) {
        capacityProp = entity.events()
                .event("fuelCapacity", FloatProperty.<Component>make());
        fuelProp = entity.events()
                .event("fuel", FloatProperty.<Component>make());
        fuelPickEvent = entity.events()
                .event("fuelPicked", FloatProperty.<Component>make());
        fuelPickSubIdx = fuelPickEvent.subscribe(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        fuelPickSubIdx = fuelPickEvent.unsubscribe(fuelPickSubIdx);
    }

    @Override
    public boolean onTriggered(Component component, FloatProperty<Component> event) {
        fuelProp.set(this, Math.min(capacityProp.get(), fuelProp.get() + event.get()));

        return true;
    }
}
