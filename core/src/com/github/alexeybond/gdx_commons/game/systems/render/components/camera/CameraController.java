package com.github.alexeybond.gdx_commons.game.systems.render.components.camera;

import com.badlogic.gdx.math.MathUtils;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.render.components.camera.state.EntityCameraState;
import com.github.alexeybond.gdx_commons.game.systems.render.interfaces.CameraState;
import com.github.alexeybond.gdx_commons.game.systems.render.components.camera.state.InterpolationState;
import com.github.alexeybond.gdx_commons.game.systems.render.components.camera.state.VariableCameraState;
import com.github.alexeybond.gdx_commons.game.systems.render.interfaces.ZoomFunction;
import com.github.alexeybond.gdx_commons.game.systems.timing.TimingSystem;
import com.github.alexeybond.gdx_commons.util.event.helpers.Subscription;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

/**
 *
 */
public class CameraController implements Component {
    private final Subscription<TimingSystem, FloatProperty<TimingSystem>> deltaTimeSub
            = new Subscription<TimingSystem, FloatProperty<TimingSystem>>() {
        @Override
        public boolean onTriggered(TimingSystem timingSystem, FloatProperty<TimingSystem> dt) {
            CameraState actualState = targetState;

            if (null != interpolationState) {
                if (interpolationState.update(dt.get())) {
                    interpolationState = null;
                } else {
                    actualState = interpolationState;
                }
            }

            positionProp.set(CameraController.this, actualState.position());
            rotationProp.set(CameraController.this, actualState.rotation());
            zoomFunctionProp.set(CameraController.this, actualState.zoomFunction());

            return true;
        }
    };

    private InterpolationState interpolationState;
    private CameraState targetState;

    private Vec2Property<Component> positionProp;
    private FloatProperty<Component> rotationProp;
    private ObjectProperty<ZoomFunction, Component> zoomFunctionProp;

    public CameraController() {
    }

    @Override
    public void onConnect(Entity entity) {
        deltaTimeSub.set(entity.game().systems().<TimingSystem>get("timing").events()
                .<FloatProperty<TimingSystem>>event("deltaTime"));

        positionProp = entity.events().event("position", Vec2Property.<Component>make());
        rotationProp = entity.events().event("rotation", FloatProperty.<Component>make());
        zoomFunctionProp = entity.events().event("zoomFunction", ObjectProperty.<ZoomFunction, Component>make());

        targetState = new EntityCameraState(entity);
    }

    @Override
    public void onDisconnect(Entity entity) {
        deltaTimeSub.clear();
    }

    public void setTargetState(CameraState state, float time) {
        if (state == targetState) return;

        if (time > MathUtils.FLOAT_ROUNDING_ERROR) {
            CameraState currentState = (null == interpolationState) ? targetState : interpolationState;
            interpolationState = new InterpolationState(currentState, state, 1f / time);
            targetState = state;
        } else {
            targetState = state;
            interpolationState = null;
        }
    }
}
