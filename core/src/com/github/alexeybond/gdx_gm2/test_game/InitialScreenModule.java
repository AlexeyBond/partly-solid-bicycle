package com.github.alexeybond.gdx_gm2.test_game;

import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.IoCStrategy;
import com.github.alexeybond.gdx_commons.ioc.modules.Module;

/**
 *
 */
public class InitialScreenModule implements Module {
    @Override
    public void init() {
        IoC.register("initial screen", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) {
                return new StartScreen();
            }
        });
    }

    @Override
    public void shutdown() {

    }
}
