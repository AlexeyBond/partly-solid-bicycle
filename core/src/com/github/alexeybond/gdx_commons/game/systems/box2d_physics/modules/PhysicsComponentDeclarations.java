package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.modules;

import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.MouseJointComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.PhysicsDebuggerComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.decl.*;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.modules.Module;

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
        map.put("physics debugger", PhysicsDebuggerComponent.Decl.class);
        map.put("mouse joint", MouseJointComponent.Decl.class);
        map.put("distance joint", DistanceJointDecl.class);
        map.put("rope joint", RopeJointDecl.class);
        map.put("revolute joint", RevoluteJointDecl.class);
        map.put("rope", RopeDecl.class);
    }

    @Override
    public void shutdown() {

    }
}
