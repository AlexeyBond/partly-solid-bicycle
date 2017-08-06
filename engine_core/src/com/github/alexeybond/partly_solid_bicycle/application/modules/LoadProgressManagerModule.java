package com.github.alexeybond.partly_solid_bicycle.application.modules;

import com.github.alexeybond.partly_solid_bicycle.application.LoadProgressManager;
import com.github.alexeybond.partly_solid_bicycle.application.impl.load.DefaultLoadProgressManager;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;
import com.github.alexeybond.partly_solid_bicycle.ioc.strategy.Singleton;

public class LoadProgressManagerModule implements Module {
    @Override
    public void init() {
        LoadProgressManager manager = new DefaultLoadProgressManager();
        IoC.register("load progress manager", new Singleton(manager));
    }

    @Override
    public void shutdown() {

    }
}
