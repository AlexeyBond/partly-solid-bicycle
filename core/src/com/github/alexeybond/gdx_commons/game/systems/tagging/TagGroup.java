package com.github.alexeybond.gdx_commons.game.systems.tagging;

import com.badlogic.gdx.utils.Array;
import com.github.alexeybond.gdx_commons.game.Entity;

/**
 *
 */
public class TagGroup {
    public static int DEFAULT_RESERVED_CAPACITY = 4;

    private final String tag;
    private final Array<Entity> entities = new Array<Entity>(DEFAULT_RESERVED_CAPACITY);
    private int modCount = 0;

    public TagGroup(String tag) {
        this.tag = tag;
    }

    public String tag() {
        return tag;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
        ++modCount;
    }

    public void removeEntity(Entity entity) {
        entities.removeValue(entity, true);
        ++modCount;
    }

    /**
     * @return number of times the group was modified (entities were added or removed) since it's creation
     */
    public int getModCount() {
        return modCount;
    }

    public Array<Entity> getAll() {
        return entities;
    }

    public Entity getOnly() {
        if (entities.size != 1)
            throw new IllegalStateException(
                    "Expected exactly one entity with tag \"" + tag() + "\" but found " + entities.size);
        return entities.first();
    }
}
