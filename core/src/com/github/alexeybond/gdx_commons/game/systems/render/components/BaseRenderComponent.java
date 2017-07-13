package com.github.alexeybond.gdx_commons.game.systems.render.components;

import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;
import com.github.alexeybond.gdx_commons.game.systems.render.interfaces.RenderComponent;
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
    protected Vec2Property position;
    protected FloatProperty rotation;
    protected RenderSystem system;
    private final String passName;

    protected BaseRenderComponent(String passName) {
        this.passName = passName;
    }

    @Override
    public void onConnect(Entity entity) {
        system = entity.game().systems().get("render");
        system.addToPass(passName, this);

        position = entity.events().event("position", Vec2Property.make());
        rotation = entity.events().event("rotation", FloatProperty.make());
    }

    @Override
    public void onDisconnect(Entity entity) {
        system.removeFromPass(passName, this);
    }
}
