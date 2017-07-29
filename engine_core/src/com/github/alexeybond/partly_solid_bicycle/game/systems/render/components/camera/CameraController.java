package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera;

import com.badlogic.gdx.math.MathUtils;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.state.EntityCameraState;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.CameraState;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.state.InterpolationState;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.ZoomFunction;
import com.github.alexeybond.partly_solid_bicycle.game.systems.timing.TimingSystem;
import com.github.alexeybond.partly_solid_bicycle.util.event.helpers.Subscription;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.FloatProperty;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.ObjectProperty;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.Vec2Property;

/**
 *
 */
public class CameraController implements Component {
    private final Subscription<FloatProperty> deltaTimeSub
            = new Subscription<FloatProperty>() {
        @Override
        public boolean onTriggered(FloatProperty dt) {
            CameraState actualState = targetState;

            if (null != interpolationState) {
                if (interpolationState.update(dt.get())) {
                    interpolationState = null;
                } else {
                    actualState = interpolationState;
                }
            }

            positionProp.set(actualState.position());
            rotationProp.set(actualState.rotation());
            zoomFunctionProp.set(actualState.zoomFunction());

            return true;
        }
    };

    private InterpolationState interpolationState;
    private CameraState targetState;

    private Vec2Property positionProp;
    private FloatProperty rotationProp;
    private ObjectProperty<ZoomFunction> zoomFunctionProp;

    public CameraController() {
    }

    @Override
    public void onConnect(Entity entity) {
        deltaTimeSub.set(entity.game().systems().<TimingSystem>get("timing").events()
                .<FloatProperty>event("deltaTime"));

        positionProp = entity.events().event("position", Vec2Property.make());
        rotationProp = entity.events().event("rotation", FloatProperty.make());
        zoomFunctionProp = entity.events().event("zoomFunction", ObjectProperty.<ZoomFunction>make());

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
