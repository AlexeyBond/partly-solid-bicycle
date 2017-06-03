package com.github.alexeybond.gdx_commons.game.systems.tagging;

import com.badlogic.gdx.utils.Array;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;

/**
 *
 */
public class TagsComponent implements Component {
    private Entity entity;
    private TaggingSystem system;
    private final Array<TagGroup> groups = new Array<TagGroup>(false, 4);

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        this.system = entity.game().systems().get("tagging");
    }

    @Override
    public void onDisconnect(Entity entity) {

    }

    public void tag(String tagName) {
        TagGroup group = system.group(tagName);

        if (groups.contains(group, true)) {
            return;
        }

        group.addEntity(entity);
        groups.add(group);
    }

    public void untag(String tagName) {
        TagGroup group = system.group(tagName);

        if (!groups.contains(group, true)) {
            return;
        }

        group.removeEntity(entity);
        groups.removeValue(group, true);
    }
}
