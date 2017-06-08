package com.github.alexeybond.gdx_commons.game.modules;

import com.github.alexeybond.gdx_commons.game.common_components.decl.ContinuousSoundDecl;
import com.github.alexeybond.gdx_commons.game.common_components.decl.InfiniteWorldGenDecl;
import com.github.alexeybond.gdx_commons.game.common_components.decl.OneShotSoundDecl;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.Module;

import java.util.Map;

/**
 *
 */
public class CommonComponents implements Module {
    @Override
    public void init() {
        Map<String, Class> map = IoC.resolve("component type aliases");
        map.put("infinite world generator", InfiniteWorldGenDecl.class);
        map.put("one shot sound", OneShotSoundDecl.class);
        map.put("continuous sound", ContinuousSoundDecl.class);
    }

    @Override
    public void shutdown() {

    }
}
