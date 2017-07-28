package com.github.alexeybond.partly_solid_bicycle.demos.test_game;

import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoCStrategy;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;

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
