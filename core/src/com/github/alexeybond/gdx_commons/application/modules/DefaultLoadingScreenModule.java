package com.github.alexeybond.gdx_commons.application.modules;

import com.badlogic.gdx.assets.AssetManager;
import com.github.alexeybond.gdx_commons.application.impl.screens.DefaultLoadingScreen;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.modules.Module;
import com.github.alexeybond.gdx_commons.ioc.strategy.Singleton;

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
