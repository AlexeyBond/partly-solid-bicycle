package com.github.alexeybond.gdx_commons.game.systems.render.components.camera;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.render.interfaces.CameraState;
import com.github.alexeybond.gdx_commons.game.systems.render.components.camera.state.EntityCameraState;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;

/**
 * Component that makes an entity able to be a target for a camera.
 */
public class CameraTargetComponent implements Component {
    @Override
    public void onConnect(Entity entity) {
        ObjectProperty<CameraState, Component> stateProp
                = entity.events().event("cameraState", ObjectProperty.<CameraState, Component>make());

        stateProp.set(this, new EntityCameraState(entity));
    }

    @Override
    public void onDisconnect(Entity entity) {

    }
}
