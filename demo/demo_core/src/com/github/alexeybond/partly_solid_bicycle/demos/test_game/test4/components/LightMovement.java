package com.github.alexeybond.partly_solid_bicycle.demos.test_game.test4.components;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.timing.TimingSystem;
import com.github.alexeybond.partly_solid_bicycle.util.event.helpers.Subscription;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.FloatProperty;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.Vec2Property;

/**
 *
 */
public class LightMovement implements Component {
    private final float min, max, v;

    private final Subscription<FloatProperty> deltaTimeSub
            = new Subscription<FloatProperty>() {
        @Override
        public boolean onTriggered(FloatProperty event) {
            float y = position.ref().y;
            if (y < min) y = max;
            if (y > max) y = min;

            y += event.get() * v;

            position.set(position.ref().x, y);

            return true;
        }
    };

    private Vec2Property position;

    public LightMovement(float min, float max, float v) {
        this.min = min;
        this.max = max;
        this.v = v;
    }

    @Override
    public void onConnect(Entity entity) {
        position = entity.events().event("position");
        deltaTimeSub.set(entity.game().systems().<TimingSystem>get("timing").events()
                .<FloatProperty>event("deltaTime"));
    }

    @Override
    public void onDisconnect(Entity entity) {
        deltaTimeSub.clear();
    }

    public static class Decl implements ComponentDeclaration {
        public float min = -600, max = 600, v = 1000;

        @Override
        public Component create(GameDeclaration gameDeclaration, Game game) {
            return new LightMovement(min, max, v);
        }
    }
}
