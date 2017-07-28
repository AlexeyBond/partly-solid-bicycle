package com.github.alexeybond.partly_solid_bicycle.game.modules;

import com.github.alexeybond.partly_solid_bicycle.game.common_components.SideViewFluid;
import com.github.alexeybond.partly_solid_bicycle.game.common_components.decl.*;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;

import java.util.Map;

/**
 *
 */
public class CommonComponents implements Module {
    @Override
    public void init() {
        Map<String, Class> map = IoC.resolve("component type aliases");
        map.put("infinite world generator", InfiniteWorldGenDecl.class);
        map.put("one shot sound", OneShotSoundDecl.class);
        map.put("continuous sound", ContinuousSoundDecl.class);
        map.put("attach to tagged", AttachToTaggedDecl.class);
        map.put("destroy on event", DestroyOnEventDecl.class);
        map.put("spawn on event", SpawnOnEventDecl.class);
        map.put("send event to game", SendEventToGameDecl.class);
        map.put("collision event filter", CollisionEventFilterDecl.class);
        map.put("side view fluid", SideViewFluid.Decl.class);
    }

    @Override
    public void shutdown() {

    }
}
