package com.github.alexeybond.gdx_gm2;

import com.github.alexeybond.gdx_commons.application.impl.app.GameApplication;
import com.github.alexeybond.gdx_commons.game.utils.destruction.modules.DestroyersPoolModule;
import com.github.alexeybond.gdx_commons.ioc.modules.Modules;
import com.github.alexeybond.gdx_gm2.test_game.InitialScreenModule;

public class MyGdxGame extends GameApplication {
    @Override
    protected Modules setupModules(Modules modules) {
        modules = super.setupModules(modules);

        modules.add(new DestroyersPoolModule());

        modules.add(new InitialScreenModule());

        return modules;
    }
}
