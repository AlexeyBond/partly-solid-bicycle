package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.EntityDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.visitor.impl.ApplyEntityDeclarationVisitor;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.CollisionData;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.helpers.BodyAnchorsHelper;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces.*;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TaggingSystem;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.ObjectProperty;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.Vec2Property;

/**
 *
 */
public class RopeComponent implements Component, UpdatablePhysicsComponent {
    private final BodyDef bodyDef = new BodyDef();
    { bodyDef.type = BodyDef.BodyType.DynamicBody; }

    private final static float[] shapeVertices = new float[2 * 4];

    private final FixtureDef fixtureDef = new FixtureDef();
    private final RevoluteJointDef jointDef = new RevoluteJointDef();

    private final float segmentLength, width, segmentAngleLimit;
    private final float segmentDensity, segmentFriction, segmentRestitution;
    private final int segmentCount;
    private final String entityTag;
    private final Vector2 startAnchor;
    private final boolean startLocal;

    private final ApplyEntityDeclarationVisitor entityDeclarationVisitor = new ApplyEntityDeclarationVisitor();

    private final GameDeclaration gameDeclaration;
    private final EntityDeclaration endSpawnDeclaration;

    private final Array<RopeSegmentComponent> segments = new Array<RopeSegmentComponent>();
    private APhysicsSystem physicsSystem;
    private Entity entity;

    private boolean alive;
    private ObjectProperty<FloatArray> segmentPositionsProp;

    public RopeComponent(float segmentLength, float width, float segmentAngleLimit, float segmentDensity, float segmentFriction, float segmentRestitution, int segmentCount, String entityTag, Vector2 startAnchor, boolean startLocal, GameDeclaration gameDeclaration, EntityDeclaration endSpawnDeclaration) {
        this.segmentLength = segmentLength;
        this.width = width;
        this.segmentAngleLimit = segmentAngleLimit;
        this.segmentDensity = segmentDensity;
        this.segmentFriction = segmentFriction;
        this.segmentRestitution = segmentRestitution;
        this.segmentCount = segmentCount;
        this.entityTag = entityTag;
        this.startAnchor = startAnchor;
        this.startLocal = startLocal;
        this.gameDeclaration = gameDeclaration;
        this.endSpawnDeclaration = endSpawnDeclaration;
    }

    private float[] prepareShapeVertices() {
        float[] v = shapeVertices;
        int i = 0;
        float hw = width * .5f, h = segmentLength;
        v[i++] = hw;
        v[i++] = 0;

        v[i++] = hw;
        v[i++] = -h;

        v[i++] = -hw;
        v[i++] = -h;

        v[i++] = -hw;
        v[i++] = 0;

//        v[i++] = 0;
//        v[i++] = 0;
//
//        v[i++] = hw;
//        v[i++] = -.5f * h;
//
//        v[i++] = 0;
//        v[i++] = -h;
//
//        v[i++] = -hw;
//        v[i++] = -.5f * h;

        return v;
    }

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        physicsSystem = entity.game().systems().get("physics");

        if (null == fixtureDef.shape) {
            PolygonShape shape = new PolygonShape();
//            shape.setAsBox(0.5f * width, 0.5f * segmentLength, new Vector2(0, -0.5f * segmentLength), 0);
            shape.set(prepareShapeVertices());
            fixtureDef.shape = shape;

            fixtureDef.density = segmentDensity;
            fixtureDef.friction = segmentFriction;
            fixtureDef.restitution = segmentRestitution;
        }

        TaggingSystem taggingSystem = entity.game().systems().get("tagging");
        Entity startEntity = taggingSystem.group(entityTag).getOnly();

        Body connectBody = startEntity.components().<BodyPhysicsComponent>get("body").body();
        Vector2 connectPoint = BodyAnchorsHelper.localAnchor(connectBody, startAnchor, startLocal);
        Vector2 centerPoint = BodyAnchorsHelper.globalAnchor(connectBody, startAnchor, startLocal);

        segments.clear();
        segments.ensureCapacity(segmentCount);

        for (int i = 0; i < segmentCount; i++) {
            RopeSegmentComponent segmentComponent = new RopeSegmentComponent();

            segmentComponent.onConnect(entity);

            segments.add(segmentComponent);

            Body segmentBody = segmentComponent.body();
            segmentBody.setTransform(
                    centerPoint.x, centerPoint.y,
                    0
            );

            centerPoint.y -= segmentLength;

            jointDef.bodyA = connectBody;
            jointDef.localAnchorA.set(connectPoint);
            jointDef.bodyB = segmentComponent.body();
            jointDef.localAnchorB.set(0, 0);
            jointDef.referenceAngle = 0;
            jointDef.lowerAngle = -segmentAngleLimit * MathUtils.degreesToRadians;
            jointDef.upperAngle = +segmentAngleLimit * MathUtils.degreesToRadians;
            jointDef.collideConnected = false;
            jointDef.enableLimit = true;

            physicsSystem.world().createJoint(jointDef);

            connectBody = jointDef.bodyB;
            connectPoint.set(0, -segmentLength);
        }

        if (null != endSpawnDeclaration) {
            Entity endEntity = entityDeclarationVisitor
                    .doVisit(endSpawnDeclaration, gameDeclaration, new Entity(entity.game()));

            endEntity.events().event("position", Vec2Property.make())
                    .set(centerPoint);

            BodyPhysicsComponent bodyComponent = endEntity.components().get("body");

            jointDef.bodyA = connectBody;
            jointDef.localAnchorA.set(connectPoint);
            jointDef.bodyB = bodyComponent.body();
            jointDef.localAnchorB.set(0, 0);
            jointDef.collideConnected = false;
            jointDef.enableLimit = false;

            physicsSystem.world().createJoint(jointDef);
        }

        segmentPositionsProp = entity.events()
                .event("ropeSegmentPositions", ObjectProperty.<FloatArray>make());
        segmentPositionsProp.setSilently(new FloatArray(segmentCount * 2));

        refreshSegmentPositions();

        alive = true;
        physicsSystem.registerComponent(this);
    }

    private void refreshSegmentPositions() {
        FloatArray positionsArr = segmentPositionsProp.get();
        positionsArr.setSize(segments.size * 2);
        float[] positions = positionsArr.items;
        for (int i = 0, j = 0; i < segments.size; i++) {
            Vector2 segmentCenter = segments.get(i).body().getWorldCenter();
            positions[j++] = segmentCenter.x;
            positions[j++] = segmentCenter.y;
        }
        segmentPositionsProp.trigger();
    }

    @Override
    public void onDisconnect(Entity entity) {
        for (int i = 0; i < segmentCount; i++) {
            segments.get(i).onDisconnect(entity);
        }
        segments.clear();
        alive = false;
    }

    @Override
    public void update() {
        refreshSegmentPositions();
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    private class RopeSegmentComponent implements
            Component, CollidablePhysicsComponent, BodyPhysicsComponent, FixturePhysicsComponent,
            DisposablePhysicsComponent {
        private Body body;
        private Fixture fixture;

        @Override
        public Body body() {
            return body;
        }

        @Override
        public Fixture fixture() {
            return fixture;
        }

        @Override
        public void onConnect(Entity entity) {
            body = physicsSystem.world().createBody(bodyDef);
            fixture = body.createFixture(fixtureDef);
        }

        @Override
        public void onDisconnect(Entity entity) {
            physicsSystem.disposeComponent(this);
        }

        @Override
        public Entity entity() {
            return entity;
        }

        @Override
        public void onBeginCollision(CollisionData collision) {

        }

        @Override
        public void onEndCollision(CollisionData collision) {

        }

        @Override
        public void dispose() {
            body.getWorld().destroyBody(body);
            fixture = null;
            body = null;
        }
    }
}
