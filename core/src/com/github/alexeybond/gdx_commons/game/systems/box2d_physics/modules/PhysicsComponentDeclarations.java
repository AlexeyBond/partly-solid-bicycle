package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.modules;

import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl.*;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.Module;

import java.util.Map;

/**
 *
 */
public class PhysicsComponentDeclarations implements Module {
    @Override
    public void init() {
        Map<String, Class> map = IoC.resolve("component type aliases");
        map.put("body", BodyDecl.class);
        map.put("box fixture", BoxFixtureDecl.class);
        map.put("polygon fixture", PolygonFixtureDecl.class);
        map.put("circle fixture", CircleFixtureDecl.class);
        map.put("generic trigger", GenericTriggerDecl.class);
    }

    @Override
    public void shutdown() {

    }
}
