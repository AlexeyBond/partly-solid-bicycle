package com.github.alexeybond.partly_solid_bicycle.ext.spriter_animation.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.brashmonkey.spriter.SCMLReader;
import com.github.alexeybond.partly_solid_bicycle.ext.spriter_animation.components.SlaveSpriterAnimationComponent;
import com.github.alexeybond.partly_solid_bicycle.ext.spriter_animation.components.SpriterAnimationComponent;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoCStrategy;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;

import java.util.Map;

public class SpriterAnimationModule implements Module {
    @Override
    public void init() {
        IoC.register("load spriter animation", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) {
                // TODO:: Optimize, use asset manager to load animations
                String path = (String) args[0];

                FileHandle handle = Gdx.files.internal(path);

                return new SCMLReader(handle.read()).getData();
            }
        });

        // Register component type
        Map<String, Class> componentTypeMap = IoC.resolve("component type aliases");
        componentTypeMap.put("spriter animation", SpriterAnimationComponent.Decl.class);
        componentTypeMap.put("slave spriter animation", SlaveSpriterAnimationComponent.Decl.class);
    }

    @Override
    public void shutdown() {

    }
}
