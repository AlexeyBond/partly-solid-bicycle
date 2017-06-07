package com.github.alexeybond.gdx_commons.game.systems.input.modules;

import com.github.alexeybond.gdx_commons.game.systems.input.components.KeyBindingsComponent;
import com.github.alexeybond.gdx_commons.game.systems.input.components.decl.KeyBindings;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.Module;

import java.util.Map;

/**
 *
 */
public class InputComponentsDeclarations implements Module {
    @Override
    public void init() {
        Map<String, Class> map = IoC.resolve("component type aliases");
        map.put("key bindings", KeyBindings.class);
    }

    @Override
    public void shutdown() {

    }
}
