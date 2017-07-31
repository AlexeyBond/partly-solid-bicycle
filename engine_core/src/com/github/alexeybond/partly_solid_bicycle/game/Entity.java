package com.github.alexeybond.partly_solid_bicycle.game;

import com.github.alexeybond.partly_solid_bicycle.util.event.Events;
import com.github.alexeybond.partly_solid_bicycle.util.event.DefaultEvents;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventsOwner;
import com.github.alexeybond.partly_solid_bicycle.util.parts.AParts;
import com.github.alexeybond.partly_solid_bicycle.util.parts.Parts;

/**
 *
 */
public class Entity implements EventsOwner {
    private final Game game;

    private boolean isAlive = true;

    private final Events events = new DefaultEvents();
    private final Parts<Entity, Component> components = new Parts<Entity, Component>(this);

    public Entity(Game game) {
        this.game = game;
    }

    public Game game() {
        return game;
    }

    @Override
    public Events events() {
        return events;
    }

    public AParts<Entity, Component> components() {
        return components;
    }

    public void destroy() {
        try {
            components.removeAll();
        } finally {
            isAlive = false;
        }
    }

    public boolean alive() {
        return isAlive;
    }
}
