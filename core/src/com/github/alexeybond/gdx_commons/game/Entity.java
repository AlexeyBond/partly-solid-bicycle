package com.github.alexeybond.gdx_commons.game;

import com.github.alexeybond.gdx_commons.util.event.Events;
import com.github.alexeybond.gdx_commons.util.event.EventsOwner;
import com.github.alexeybond.gdx_commons.util.parts.AParts;
import com.github.alexeybond.gdx_commons.util.parts.Parts;

/**
 *
 */
public class Entity implements EventsOwner<Component> {
    private final Game game;

    private final Events<Component> events = new Events<Component>();
    private final Parts<Entity, Component> components = new Parts<Entity, Component>(this);

    public Entity(Game game) {
        this.game = game;
    }

    public Game game() {
        return game;
    }

    @Override
    public Events<Component> events() {
        return events;
    }

    public AParts<Entity, Component> components() {
        return components;
    }

    public void destroy() {
        components.removeAll();
    }
}
