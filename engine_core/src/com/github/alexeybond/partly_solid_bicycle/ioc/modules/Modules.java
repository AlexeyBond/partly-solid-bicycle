package com.github.alexeybond.partly_solid_bicycle.ioc.modules;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/**
 * Group of modules loaded by an application or some part of it.
 */
public class Modules implements Disposable {
    private Array<Module> modules = new Array<Module>(true, 8);

    public void add(Module module) {
        module.init();
        modules.add(module);
    }

    @Override
    public void dispose() {
        for (int i = modules.size - 1; i >= 0; i--) {
            modules.get(i).shutdown();
        }

        modules.clear();
    }
}
