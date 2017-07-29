package com.github.alexeybond.partly_solid_bicycle.game.systems.tagging;

import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.GameSystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Tagging system provides a simple and efficient way to add named labels (tags) to some entities and search
 * for entities with some label.
 *
 * I'm not sure if this system is necessary as the whole ECS works alike to tagging system in our old framework.
 */
public class TaggingSystem implements GameSystem {
    private final Map<String, TagGroup> groups = new HashMap<String, TagGroup>();

    @Override
    public void onConnect(Game game) {

    }

    @Override
    public void onDisconnect(Game game) {

    }

    @Override
    public void update(float deltaTime) {

    }

    public TagGroup group(String name) {
        TagGroup group = groups.get(name);

        if (null == group) {
            group = new TagGroup(name);
            groups.put(name, group);
        }

        return group;
    }
}
