package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.github.alexeybond.partly_solid_bicycle.drawing.DrawingContext;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces.APhysicsSystem;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.RenderSystem;
import com.github.alexeybond.partly_solid_bicycle.game.systems.render.interfaces.RenderComponent;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.ObjectProperty;

/**
 *
 */
public class PhysicsDebuggerComponent implements Component, RenderComponent {
    private final String passName, cameraName;

    private Box2DDebugRenderer debugRenderer;
    private RenderSystem renderSystem;
    private APhysicsSystem physicsSystem;
    private ObjectProperty<Camera> cameraProperty;

    private final Matrix4 tmp = new Matrix4();

    public PhysicsDebuggerComponent(String passName, String cameraName) {
        this.passName = passName;
        this.cameraName = cameraName;
    }

    @Override
    public void onConnect(Entity entity) {
        renderSystem = entity.game().systems().get("render");
        physicsSystem = entity.game().systems().get("physics");

        cameraProperty = entity.game().events()
                .event(cameraName, ObjectProperty.<Camera>make());

        debugRenderer = new Box2DDebugRenderer();

        renderSystem.addToPass(passName, this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        renderSystem.removeFromPass(passName, this);
    }

    @Override
    public void draw(DrawingContext context) {
        context.state().end();

        if (null == debugRenderer) {
            debugRenderer = new Box2DDebugRenderer();
        }

        debugRenderer.render(physicsSystem.world(), cameraProperty.get().combined);
    }

    public static class Decl implements ComponentDeclaration {
        public String pass = "game-debug";

        public String camera;

        @Override
        public Component create(GameDeclaration gameDeclaration, Game game) {
            return new PhysicsDebuggerComponent(pass, camera);
        }
    }
}
