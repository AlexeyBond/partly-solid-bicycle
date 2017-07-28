package com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.components;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TagGroup;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TaggingSystem;

/**
 *
 */
public class SingleTagComponent implements Component {
    private final String tagName;
    private TagGroup group;

    public SingleTagComponent(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public void onConnect(Entity entity) {
        group = entity.game().systems().<TaggingSystem>get("tagging").group(tagName);
        group.addEntity(entity);
    }

    @Override
    public void onDisconnect(Entity entity) {
        group.removeEntity(entity);
    }
}
