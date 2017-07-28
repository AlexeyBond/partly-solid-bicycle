package com.github.alexeybond.partly_solid_bicycle.application.modules;

import com.badlogic.gdx.assets.AssetManager;
import com.github.alexeybond.partly_solid_bicycle.application.impl.screens.DefaultLoadingScreen;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;
import com.github.alexeybond.partly_solid_bicycle.ioc.strategy.Singleton;

/**
 *
 */
public class DefaultLoadingScreenModule implements Module {
    @Override
    public void init() {
        AssetManager assetManager = IoC.resolve("asset manager");

        IoC.register("loading screen", new Singleton(new DefaultLoadingScreen(assetManager)));
    }

    @Override
    public void shutdown() {

    }
}
