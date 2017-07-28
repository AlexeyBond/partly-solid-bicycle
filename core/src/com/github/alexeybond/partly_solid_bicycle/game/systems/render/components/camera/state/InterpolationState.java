package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.state;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.CameraState;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.ZoomFunction;

/**
 *
 */
public class InterpolationState implements CameraState, ZoomFunction {
    private final Vector2 position = new Vector2();
    private float rotation;
    private ZoomFunction zoomFunction1, zoomFunction2;
    private final CameraState state1, state2;
    private final float speed;
    private float k = 0;

    public InterpolationState(CameraState state1, CameraState state2, float speed) {
        this.state1 = state1;
        this.state2 = state2;
        this.speed = speed;
        zoomFunction1 = state1.zoomFunction();
        zoomFunction2 = state2.zoomFunction();
    }

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
        return this;
    }

    @Override
    public float compute(float width, float height) {
        return MathUtils.lerp(
                zoomFunction1.compute(width, height),
                zoomFunction2.compute(width, height),
                k);
    }

    public boolean update(float dt) {
        k += dt * speed;

        if (k >= 1) {
            return true;
        }

        rotation = MathUtils.lerp(state1.rotation(), state2.rotation(), k);
        position.set(state1.position()).lerp(state2.position(), k);

        return false;
    }
}
