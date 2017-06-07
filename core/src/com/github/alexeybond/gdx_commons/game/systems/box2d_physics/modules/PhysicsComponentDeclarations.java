package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.modules;

import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl.BodyDecl;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl.BoxFixtureDecl;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl.PolygonFixtureDecl;
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
        map.put("poly fixture", PolygonFixtureDecl.class);
    }

    @Override
    public void shutdown() {

    }
}
