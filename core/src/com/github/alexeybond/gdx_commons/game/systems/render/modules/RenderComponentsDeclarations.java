package com.github.alexeybond.gdx_commons.game.systems.render.modules;

import com.github.alexeybond.gdx_commons.game.systems.render.components.camera.decl.CameraControllerDecl;
import com.github.alexeybond.gdx_commons.game.systems.render.components.camera.decl.CameraTargetDecl;
import com.github.alexeybond.gdx_commons.game.systems.render.components.camera.decl.OrthographicCameraDecl;
import com.github.alexeybond.gdx_commons.game.systems.render.components.camera.decl.SetCameraTargetDecl;
import com.github.alexeybond.gdx_commons.game.systems.render.components.decl.*;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.modules.Module;

import java.util.Map;

/**
 *
 */
public class RenderComponentsDeclarations implements Module {
    @Override
    public void init() {
        Map<String, Class> map = IoC.resolve("component type aliases");
        map.put("particles", AttachedParticleEffect.class);
        map.put("sprite", StaticSprite.class);
        map.put("animated sprite", AnimatedSprite.class);
        map.put("background loop", BackgroundLoop.class);

        map.put("camera", OrthographicCameraDecl.class);
        map.put("camera controller", CameraControllerDecl.class);
        map.put("camera target", CameraTargetDecl.class);
        map.put("set camera target", SetCameraTargetDecl.class);

        map.put("rope sprite", MasterRopeSprite.class);
    }

    @Override
    public void shutdown() {

    }
}
