package com.github.alexeybond.partly_solid_bicycle.demos;

import com.github.alexeybond.partly_solid_bicycle.application.impl.app.GameApplication;
import com.github.alexeybond.partly_solid_bicycle.game.utils.destruction.modules.DestroyersPoolModule;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Modules;
import com.github.alexeybond.partly_solid_bicycle.demos.test_game.InitialScreenModule;

public class MyGdxGame extends GameApplication {
    @Override
    protected Modules setupModules(Modules modules) {
        modules = super.setupModules(modules);

        modules.add(new DestroyersPoolModule());

        modules.add(new InitialScreenModule());

        return modules;
    }
}
