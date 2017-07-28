package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.CameraState;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.components.camera.state.EntityCameraState;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.ObjectProperty;

/**
 * Component that makes an entity able to be a target for a camera.
 */
public class CameraTargetComponent implements Component {
    @Override
    public void onConnect(Entity entity) {
        ObjectProperty<CameraState> stateProp
                = entity.events().event("cameraState", ObjectProperty.<CameraState>make());

        stateProp.set(new EntityCameraState(entity));
    }

    @Override
    public void onDisconnect(Entity entity) {

    }
}
