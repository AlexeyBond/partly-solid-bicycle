package com.github.alexeybond.gdx_gm2.test_game.game.components;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;

/**
 *
 */
public class FuelItem
        implements Component, EventListener<Component, ObjectProperty<Entity, Component>> {
    private final float amount;
    private int hitSubIdx = -1;
    private Entity entity;

    public FuelItem(float amount) {
        this.amount = amount;
    }

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        hitSubIdx = entity
                .events()
                .event("playerCollect", ObjectProperty.<Entity, Component>make())
                .subscribe(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        hitSubIdx = entity
                .events()
                .event("playerCollect", ObjectProperty.<Entity, Component>make())
                .unsubscribe(hitSubIdx);
    }

    @Override
    public boolean onTriggered(Component component, ObjectProperty<Entity, Component> event) {
        FloatProperty<Component> fuel = event.get().events().event("fuel");
        FloatProperty<Component> fuelCapacity = event.get().events().event("fuelCapacity");

        fuel.set(this, Math.min(fuel.get() + amount, fuelCapacity.get()));

        entity.components().removeAll();

        return true;
    }
}
