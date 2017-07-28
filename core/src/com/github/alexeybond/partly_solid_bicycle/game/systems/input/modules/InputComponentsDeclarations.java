package com.github.alexeybond.partly_solid_bicycle.game.systems.input.modules;

import com.github.alexeybond.partly_solid_bicycle.game.systems.input.components.decl.KeyBindings;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;

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
