package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components.decl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components.FixtureDefFixtureComponent;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces.APhysicsSystem;

/**
 *
 */
abstract class FixtureDeclBase implements ComponentDeclaration {
    private transient FixtureDef fixtureDef = null;
    /* The last game this declaration was initialized for. */
    private transient Game game = null;

    public String collisionBeginEvent = "collisionBegin";
    public String collisionEndEvent = "collisionEnd";

    public float density = 0.01f;
    public float restitution = 0;
    public float friction = 0.2f;
    public boolean sensor = false;

    public float centerX = 0;
    public float centerY = 0;
    public float[] center = new float[0];

    /** Name of collision group the fixture will belong to. By default fixture belongs to default group (with index 0)
     * and always uses category/mask. */
    public String collisionGroup = null;

    /** List of category names. By default fixture belongs to category "default". */
    public String[] collisionCategories = null;

    /** List of categories the fixture will collide with. By default the fixture will collide with fixtures of all
     * categories. */
    public String[] collisionMask = null;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        initFixtureDef();
        initFilters(game);
        return new FixtureDefFixtureComponent(
                collisionBeginEvent, collisionEndEvent, fixtureDef);
    }

    private void initFixtureDef() {
        if (null != fixtureDef) return;

        fixtureDef = new FixtureDef();
        fixtureDef.shape = initShape();
        fixtureDef.density = density;
        fixtureDef.isSensor = sensor;
        fixtureDef.restitution = restitution;
        fixtureDef.friction = friction;
    }

    protected Vector2 getCenter() {
        if (center.length != 0) {
            return new Vector2(center[0], center[1]);
        } else {
            return new Vector2(centerX, centerY);
        }
    }

    protected abstract Shape initShape();

    private void initFilters(Game game) {
        if (this.game == game) return;

        APhysicsSystem physicsSystem = game.systems().get("physics");

        fixtureDef.filter.categoryBits = initFilterFlags(physicsSystem, collisionCategories, (short) 0x01);
        fixtureDef.filter.maskBits = initFilterFlags(physicsSystem, collisionMask, (short) -1);

        if (null != collisionGroup) {
            fixtureDef.filter.groupIndex = physicsSystem.collisionGroup(collisionGroup);
        }

        this.game = game;
    }

    private short initFilterFlags(APhysicsSystem physicsSystem, String[] settings, short defaultFlags) {
        if (null == settings) {
            return defaultFlags;
        }

        short res = 0;

        for (int i = 0; i < settings.length; i++) {
            res |= physicsSystem.categoryFlag(settings[i]);
        }

        return res;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        // (PROBABLY) This should be enough
        if (null != fixtureDef && null != fixtureDef.shape) {
            fixtureDef.shape.dispose();
            fixtureDef.shape = null;
        }
    }
}
