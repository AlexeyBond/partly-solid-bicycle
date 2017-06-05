package com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Fixture component that uses a {@link FixtureDef} instance to construct fixture.
 */
public class FixtureDefFixtureComponent extends BaseFixtureComponent {
    private final FixtureDef fixtureDef;

    protected FixtureDefFixtureComponent(String collisionBeginEventName, String collisionEndEventName, FixtureDef fixtureDef) {
        super(collisionBeginEventName, collisionEndEventName);
        this.fixtureDef = fixtureDef;
    }

    public FixtureDefFixtureComponent(FixtureDef fixtureDef) {
        super();
        this.fixtureDef = fixtureDef;
    }

    @Override
    protected Fixture createFixture() {
        return parent().body().createFixture(fixtureDef);
    }
}
