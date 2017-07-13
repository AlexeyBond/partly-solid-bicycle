package com.github.alexeybond.gdx_commons.game.systems.render.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;

/**
 *
 */
public class GlobalCameraBackgroundLoop extends BackgroundLoopComponent {
    private final String cameraName;
    private ObjectProperty<Camera> cameraProperty;

    public GlobalCameraBackgroundLoop(String pass, Texture texture, String cameraName) {
        super(pass, texture);
        this.cameraName = cameraName;
    }

    @Override
    protected Camera getCamera() {
        return cameraProperty.get();
    }

    @Override
    public void onConnect(Entity entity) {
        super.onConnect(entity);

        cameraProperty = entity.game().events()
                .event(cameraName, ObjectProperty.<Camera>make());
    }
}
