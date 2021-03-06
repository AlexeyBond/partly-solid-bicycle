package com.github.alexeybond.partly_solid_bicycle.application.modules;

import com.github.alexeybond.partly_solid_bicycle.application.LoadProgressManager;
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
        LoadProgressManager progressManager = IoC.resolve("load progress manager");
        IoC.register("loading screen", new Singleton(new DefaultLoadingScreen(progressManager)));
    }

    @Override
    public void shutdown() {

    }
}
