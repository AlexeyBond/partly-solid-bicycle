package com.github.alexeybond.partly_solid_bicycle.ext.destruction.modules;

import com.github.alexeybond.partly_solid_bicycle.ext.destruction.components.decl.DestructibleDecl;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;

import java.util.Map;

public class DestructibleComponentModule implements Module {
    @Override
    public void init() {
        Map<String, Class> map = IoC.resolve("component type aliases");
        map.put("destructible", DestructibleDecl.class);
    }

    @Override
    public void shutdown() {

    }
}
