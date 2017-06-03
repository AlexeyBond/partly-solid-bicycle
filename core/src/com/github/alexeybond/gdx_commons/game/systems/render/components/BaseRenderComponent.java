package com.github.alexeybond.gdx_commons.game.systems.render.components;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.game.event.props.Vec2Property;
import com.github.alexeybond.gdx_commons.game.systems.render.RenderComponent;
import com.github.alexeybond.gdx_commons.game.systems.render.RenderSystem;

/**
 * Base class for simple render components.
 *
 * Adds itself to a single pass (name given to constructor) on connection and removes on disconnect.
 *
 * Acquires most common entity properties (position, rotation, scale).
 *
 * Subclasses should only define drawing method.
 */
public abstract class BaseRenderComponent implements RenderComponent {
    protected Vec2Property<Component> position;
    protected FloatProperty<Component> rotation;
    protected FloatProperty<Component> scale;
    protected RenderSystem system;
    private final String passName;

    protected BaseRenderComponent(String passName) {
        this.passName = passName;
    }

    @Override
    public void onConnect(Entity entity) {
        system = entity.game().systems().get("render");
        system.addToPass(passName, this);

        position = entity.events().event("position", Vec2Property.<Component>make());
        rotation = entity.events().event("rotation", FloatProperty.<Component>make());
        scale = entity.events().event("scale", FloatProperty.<Component>make(1f));
    }

    @Override
    public void onDisconnect(Entity entity) {
        system.removeFromPass(passName, this);
    }
}
