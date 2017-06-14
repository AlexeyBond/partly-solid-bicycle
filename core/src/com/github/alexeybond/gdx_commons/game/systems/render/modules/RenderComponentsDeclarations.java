package com.github.alexeybond.gdx_commons.game.systems.render.modules;

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
        map.put("camera", AttachedOrthographicCamera.class);
        map.put("sprite", StaticSprite.class);
        map.put("animated sprite", AnimatedSprite.class);
        map.put("background loop", BackgroundLoop.class);
    }

    @Override
    public void shutdown() {

    }
}
