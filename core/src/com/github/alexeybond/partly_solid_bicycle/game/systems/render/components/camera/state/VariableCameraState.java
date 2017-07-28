package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.state;

import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.CameraState;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.ZoomFunction;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.camera.zoom.UnitZoomFunction;

/**
 *
 */
public class VariableCameraState implements CameraState {
    private final Vector2 position = new Vector2();
    private float rotation = 0;
    private ZoomFunction zoomFunction = UnitZoomFunction.INSTANCE;

    @Override
    public Vector2 position() {
        return position;
    }

    @Override
    public float rotation() {
        return rotation;
    }

    @Override
    public ZoomFunction zoomFunction() {
        return zoomFunction;
    }

    public void set(CameraState state) {
        position.set(state.position());
        rotation = state.rotation();
        zoomFunction = state.zoomFunction();
    }
}
