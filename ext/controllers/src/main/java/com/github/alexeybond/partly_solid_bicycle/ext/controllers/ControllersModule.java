package main.java.com.github.alexeybond.partly_solid_bicycle.ext.controllers;

import com.badlogic.gdx.controllers.Controllers;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;

import java.util.Map;

public class ControllersModule implements Module {
    @Override
    public void init() {
        Map<String, Class> map = IoC.resolve("component type aliases");
        map.put("controller-button", ControllerButtonInput.Decl.class);
        map.put("controller-axis-adaptive", ControllerAxisAdaptiveInput.Decl.class);
    }

    @Override
    public void shutdown() {

    }
}
