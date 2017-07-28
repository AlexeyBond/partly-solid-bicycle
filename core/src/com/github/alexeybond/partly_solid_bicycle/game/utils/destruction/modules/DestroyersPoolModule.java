package com.github.alexeybond.partly_solid_bicycle.game.utils.destruction.modules;

import com.badlogic.gdx.utils.Pool;
import com.github.alexeybond.partly_solid_bicycle.game.utils.destruction.Destroyer;
import com.github.alexeybond.partly_solid_bicycle.game.utils.destruction.impl.DestroyerImpl;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;
import com.github.alexeybond.partly_solid_bicycle.ioc.strategy.Singleton;

/**
 *
 */
public class DestroyersPoolModule implements Module {
    private class DestroyersPool extends Pool<Destroyer> {
        @Override
        protected Destroyer newObject() {
            return new DestroyerImpl();
        }
    }

    @Override
    public void init() {
        IoC.register("destroyers pool", new Singleton(new DestroyersPool()));
    }

    @Override
    public void shutdown() {

    }
}
