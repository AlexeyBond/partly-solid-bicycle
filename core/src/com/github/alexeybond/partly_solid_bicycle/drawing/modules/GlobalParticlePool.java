package com.github.alexeybond.partly_solid_bicycle.drawing.modules;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoCStrategy;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;
import com.github.alexeybond.partly_solid_bicycle.resource_management.PreloadListCallback;
import com.github.alexeybond.partly_solid_bicycle.resource_management.PreloadList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class GlobalParticlePool implements Module {
    private Map<String, ParticleEffectPool> particlePoolMap = new HashMap<String, ParticleEffectPool>();

    @Override
    public void init() {
        IoC.register("get particle pool", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) {
                String name = (String) args[0];

                ParticleEffectPool pool = particlePoolMap.get(name);

                if (null == pool) {
                    ParticleEffect template = IoC.resolve("load particles", name);
                    pool = new ParticleEffectPool(template, 1, 16);
                    particlePoolMap.put(name, pool);
                }

                return pool;
            }
        });

        IoC.<List<PreloadListCallback>>resolve("list unload callbacks")
                .add(new PreloadListCallback() {
            @Override
            public void execute(PreloadList list, AssetManager assetManager) {
                // TODO:: Remove pools only for unloaded effects
                // TODO:: Dispose pooled effects (implement own ParticleEffectPool? standard one doesn't provide method to dispose all effects)
                particlePoolMap.clear();
            }
        });
    }

    @Override
    public void shutdown() {

    }
}
