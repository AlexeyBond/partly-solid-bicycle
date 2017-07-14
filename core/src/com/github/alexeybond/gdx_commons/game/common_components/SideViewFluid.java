package com.github.alexeybond.gdx_commons.game.common_components;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.CollisionData;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.PhysicsSystem;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces.FixturePhysicsComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.interfaces.UpdatablePhysicsComponent;
import com.github.alexeybond.gdx_commons.game.systems.render.RenderSystem;
import com.github.alexeybond.gdx_commons.game.systems.render.interfaces.RenderComponent;
import com.github.alexeybond.gdx_commons.util.event.helpers.Subscription;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;
import com.github.alexeybond.gdx_commons.util.event.props.ObjectProperty;
import com.github.alexeybond.gdx_commons.util.event.props.Vec2Property;

/**
 * Component that simulates a volume of fluid for games with side view (platformers and alike).
 *
 * Based on <a href="http://www.iforce2d.net/b2dtut/buoyancy">this</a> tutorial.
 */
public class SideViewFluid implements Component {
    private final String fixtureName;
    private final String collisionBeginEvent;
    private final String collisionEndEvent;
    private final String effectComponentName;
    private final String debugPassName;

    private FixturePhysicsComponent fluidFixture;
    private Polygon fluidFixturePolygon;

    private PhysicsSystem physicsSystem;
    private RenderSystem renderSystem;

    private final static Vector2 tmp = new Vector2(), tmp2 = new Vector2();

    private final Subscription<ObjectProperty<CollisionData>> collisionBeginSub
            = new Subscription<ObjectProperty<CollisionData>>() {
        @Override
        public boolean onTriggered(ObjectProperty<CollisionData> event) {
            CollisionData cd = event.get();
            Entity entity = cd.that.entity();
            Fixture fixture = cd.otherFixture();

            if (fixture.getBody().getType() != BodyDef.BodyType.DynamicBody || fixture.isSensor()) {
                return false;
            }

            Polygon entityPolygon = fixture2Polygon(fixture);

            entity.components().add(effectComponentName, new FluidEffect(entityPolygon, fixture.getBody()));

            return true;
        }
    };

    private final Subscription<ObjectProperty<CollisionData>> collisionEndSub
            = new Subscription<ObjectProperty<CollisionData>>() {
        @Override
        public boolean onTriggered(ObjectProperty<CollisionData> event) {
            event.get().that.entity().components().remove(effectComponentName);

            return true;
        }
    };

    private final Subscription<Vec2Property> positionSub
            = new Subscription<Vec2Property>() {
        @Override
        public boolean onTriggered(Vec2Property event) {
            Vector2 pos = event.ref();
            fluidFixturePolygon.setPosition(pos.x, pos.y);
            return true;
        }
    };

    private final Subscription<FloatProperty> rotationSub
            = new Subscription<FloatProperty>() {
        @Override
        public boolean onTriggered(FloatProperty event) {
            fluidFixturePolygon.setRotation(event.get());
            return true;
        }
    };

    public SideViewFluid(
            String fixtureName,
            String collisionBeginEvent, String collisionEndEvent,
            String effectComponentName,
            String debugPassName) {
        this.fixtureName = fixtureName;
        this.collisionBeginEvent = collisionBeginEvent;
        this.collisionEndEvent = collisionEndEvent;
        this.effectComponentName = effectComponentName;
        this.debugPassName = debugPassName;
    }

    @Override
    public void onConnect(Entity entity) {
        fluidFixture = entity.components().get(fixtureName);

        fluidFixturePolygon = fixture2Polygon(fluidFixture.fixture());

        collisionBeginSub.set(entity.events()
                .event(collisionBeginEvent, ObjectProperty.<CollisionData>make()));
        collisionEndSub.set(entity.events()
                .event(collisionEndEvent, ObjectProperty.<CollisionData>make()));
        positionSub.set(entity.events().event("position", Vec2Property.make()));
        rotationSub.set(entity.events().event("rotation", FloatProperty.make()));

        physicsSystem = entity.game().systems().get("physics");

        renderSystem = entity.game().systems().get("render");
    }

    @Override
    public void onDisconnect(Entity entity) {
        collisionBeginSub.clear();
        collisionEndSub.clear();
        positionSub.clear();
        rotationSub.clear();
    }

    private static Polygon fixture2Polygon(Fixture fixture) {
        Shape shape = fixture.getShape();

        switch (shape.getType()) {
            case Polygon:
                PolygonShape polygonShape = (PolygonShape) shape;
                int vc = polygonShape.getVertexCount();
                float[] vertices = new float[vc * 2];
                for (int i = vc - 1, j = 0; i >= 0; --i) {
                    polygonShape.getVertex(i, tmp);
                    vertices[j++] = tmp.x;
                    vertices[j++] = tmp.y;
                }
                return new Polygon(vertices);

            default:
                throw new IllegalStateException("Unknown shape type: " + shape.getType().name());
        }
    }

    private class FluidEffect implements Component, UpdatablePhysicsComponent, RenderComponent {
        private final Polygon ownerFixturePolygon;
        private final Body ownerBody;

        private Polygon intersectionPolygon = new Polygon();

        private FloatProperty rotationProperty;
        private Vec2Property positionProperty;

        private boolean alive;

//        private FloatArray leadingEdges = new FloatArray();

        FluidEffect(Polygon ownerFixturePolygon, Body ownerBody) {
            this.ownerFixturePolygon = ownerFixturePolygon;
            this.ownerBody = ownerBody;
        }

        @Override
        public void onConnect(Entity entity) {
            rotationProperty = entity.events().event("rotation", FloatProperty.make());
            positionProperty = entity.events().event("position", Vec2Property.make());
            physicsSystem.registerComponent(FluidEffect.this);

            if (null != debugPassName) renderSystem.addToPass(debugPassName, this);

            alive = true;
        }

        @Override
        public void onDisconnect(Entity entity) {
            alive = false;

            if (null != debugPassName) renderSystem.removeFromPass(debugPassName, this);
        }

        @Override
        public void update() {
            Vector2 pos = positionProperty.ref();
            ownerFixturePolygon.setPosition(pos.x, pos.y);
            ownerFixturePolygon.setRotation(rotationProperty.get());

            if (!Intersector.intersectPolygons(ownerFixturePolygon, fluidFixturePolygon, intersectionPolygon))
                return;

            float[] pv = intersectionPolygon.getVertices();

            Vector2 centroid = GeometryUtils.polygonCentroid(pv, 0, pv.length, tmp);
            float area = Math.abs(GeometryUtils.polygonArea(pv, 0, pv.length));
            float fluidDensity = fluidFixture.fixture().getDensity();

            float displacedMass = area * fluidDensity;

            ownerBody.applyForce(
                    tmp2.set(physicsSystem.world().getGravity()).scl(-displacedMass),
                    centroid, true);

//            leadingEdges.clear();

            for (int i = 0; i < pv.length; i += 2) {
                float x1 = pv[i], y1 = pv[i+1];
                float x2, y2;

                if (i >= pv.length - 2) {
                    x2 = pv[0];
                    y2 = pv[1];
                } else {
                    x2 = pv[i+2];
                    y2 = pv[i+3];
                }

                float xm = .5f * (x1 + x2), ym = .5f * (y1 + y2);
                float ex = x2 - x1, ey = y2 - y1;

                // TODO:: Use velocity relative to fluid body if it's dynamic
                Vector2 linVel = ownerBody.getLinearVelocityFromWorldPoint(tmp2.set(xm, ym));
                float velMod = linVel.len();
                float eMod = (float) Math.sqrt(ex * ex + ey * ey);

                float d = -linVel.x * ey + linVel.y * ex;

                if (d <= MathUtils.FLOAT_ROUNDING_ERROR) continue;

                float kDrag = fluidDensity * d;
                float kLift = fluidDensity * d * (linVel.x * ex + linVel.y * ey) / (velMod * eMod);

                // TODO:: Apply force to fluid body if it's dynamic
                ownerBody.applyForce(
                        -linVel.x * kDrag + linVel.y * kLift,
                        -linVel.y * kDrag + linVel.x * kLift,
                        xm, ym, true);

//                leadingEdges.addAll(x1, y1, x2, y2);
            }
        }

        @Override
        public boolean isAlive() {
            return alive;
        }

        @Override
        public void draw(DrawingContext context) {
            if (intersectionPolygon.getVertices() == null || intersectionPolygon.getVertices().length < 6) return;

            ShapeRenderer sr = context.state().beginLines();

            sr.setColor(.2f, 3f, 1f, 1f);
            sr.polygon(intersectionPolygon.getVertices());

//            sr.setColor(1, 0,0,1);
//            for (int i = 0; i < leadingEdges.size;) {
//                sr.line(
//                        leadingEdges.get(i++),
//                        leadingEdges.get(i++),
//                        leadingEdges.get(i++),
//                        leadingEdges.get(i++)
//                );
//            }
        }
    }

    public static class Decl implements ComponentDeclaration {
        public String fluidFixture;

        public String collisionBeginEvent = "collisionBegin";

        public String collisionEndEvent = "collisionEnd";

        public String effectComponentId = "fluidEffect";

        public String debugPass = null;

        @Override
        public Component create(GameDeclaration gameDeclaration, Game game) {
            return new SideViewFluid(
                    fluidFixture, collisionBeginEvent, collisionEndEvent, effectComponentId, debugPass);
        }
    }
}
