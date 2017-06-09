package com.github.alexeybond.gdx_commons.screen.modules;

import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.IoCStrategy;
import com.github.alexeybond.gdx_commons.ioc.Module;
import com.github.alexeybond.gdx_commons.screen.screens.DefaultLoadingScreen;

/**
 *
 */
public class DefaultLoadingScreenModule implements Module {
    @Override
    public void init() {
        IoC.register("loading screen", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) {
                return new DefaultLoadingScreen();
            }
        });
    }

    @Override
    public void shutdown() {

    }
}
