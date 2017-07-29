package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.state;

import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.CameraState;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.ZoomFunction;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.camera.zoom.UnitZoomFunction;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.FloatProperty;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.ObjectProperty;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.Vec2Property;

/**
 * State of a camera in which it is attached to an entity.
 */
public class EntityCameraState implements CameraState {
    private final Vec2Property positionProp;
    private final FloatProperty rotationProp;
    private final ObjectProperty<ZoomFunction> zoomFunctionProp;

    public EntityCameraState(Entity entity, ZoomFunction defaultZoomFunction) {
        positionProp = entity.events().event("position", Vec2Property.make());
        rotationProp = entity.events().event("rotation", FloatProperty.make());
        zoomFunctionProp = entity.events().event("zoomFunction",
                ObjectProperty.<ZoomFunction>make(defaultZoomFunction));
    }

    public EntityCameraState(Entity entity) {
        this(entity, UnitZoomFunction.INSTANCE);
    }

    @Override
    public Vector2 position() {
        return positionProp.ref();
    }

    @Override
    public float rotation() {
        return rotationProp.get();
    }

    @Override
    public ZoomFunction zoomFunction() {
        return zoomFunctionProp.get();
    }
}
