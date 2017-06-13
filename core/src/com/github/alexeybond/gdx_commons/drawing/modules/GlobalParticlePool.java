package com.github.alexeybond.gdx_commons.drawing.modules;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.IoCStrategy;
import com.github.alexeybond.gdx_commons.ioc.modules.Module;

import java.util.HashMap;
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
    }

    @Override
    public void shutdown() {

    }
}
