package com.github.alexeybond.gdx_commons.game.common_components;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

/**
 * Component that makes entity repeat all movements of another entity.
 */
public abstract class AttachmentComponent implements Component {
    private Vec2Property masterPositionProp;

    private FloatProperty masterRotationProp;
    private Vec2Property slavePositionProp;

    private FloatProperty slaveRotationProp;

    private int positionSubIdx = -1, rotationSubIdx = -1;

    @Override
    public void onConnect(Entity entity) {
        Entity master = getMaster(entity);

        masterPositionProp = master.events().event("position", Vec2Property.make());
        masterRotationProp = master.events().event("rotation", FloatProperty.<Component>make());

        slavePositionProp = entity.events().event("position", Vec2Property.make());
        slaveRotationProp = entity.events().event("rotation", FloatProperty.<Component>make());

        positionSubIdx = masterPositionProp.subscribe(new EventListener<Vec2Property>() {
            @Override
            public boolean onTriggered(Vec2Property event) {
                return slavePositionProp.set(event.ref());
            }
        });
        rotationSubIdx = masterRotationProp.subscribe(new EventListener<FloatProperty>() {
            @Override
            public boolean onTriggered(FloatProperty event) {
                return slaveRotationProp.set(event.get());
            }
        });
    }

    @Override
    public void onDisconnect(Entity entity) {
        positionSubIdx = masterPositionProp.unsubscribe(positionSubIdx);
        rotationSubIdx = masterRotationProp.unsubscribe(rotationSubIdx);
    }

    protected abstract Entity getMaster(Entity slave);
}
