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
        implements Component, EventListener<ObjectProperty<Entity>> {
    private final float defaultAmount;
    private int hitSubIdx = -1;
    private Entity entity;
    private ObjectProperty<Entity> hitEvent;
    private FloatProperty amountProp;

    public FuelItem(float defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        hitEvent = entity.events()
                .event("playerCollect", ObjectProperty.<Entity>make());
        amountProp = entity.events()
                .event("amount", FloatProperty.make(defaultAmount));
        hitSubIdx = hitEvent.subscribe(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        hitSubIdx = hitEvent.unsubscribe(hitSubIdx);
    }

    @Override
    public boolean onTriggered(ObjectProperty<Entity> event) {
        FloatProperty pickEvent = event.get().events().event("fuelPicked");

        pickEvent.setSilently(amountProp.get());
        pickEvent.trigger();
        amountProp.set(0);

        entity.destroy();

        return true;
    }
}
