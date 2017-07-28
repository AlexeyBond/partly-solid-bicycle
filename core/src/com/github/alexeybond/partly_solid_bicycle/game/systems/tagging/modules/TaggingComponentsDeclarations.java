package com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.modules;

import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.components.decl.SingleTag;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.components.decl.Tags;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;

import java.util.Map;

/**
 *
 */
public class TaggingComponentsDeclarations implements Module {
    @Override
    public void init() {
        Map<String, Class> map = IoC.resolve("component type aliases");
        map.put("tag", SingleTag.class);
        map.put("tags", Tags.class);
    }

    @Override
    public void shutdown() {

    }
}
