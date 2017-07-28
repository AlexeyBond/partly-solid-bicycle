package com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.components;

import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces.APhysicsSystem;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces.CreatablePhysicsComponent;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces.DisposablePhysicsComponent;
import com.github.alexeybond.partly_solid_bicycle.game.systems.box2d_physics.interfaces.JointPhysicsComponent;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TaggingSystem;

/**
 * Base class for components representing a physical joint between two physical bodies.
 *
 * <p>
 *     One of bodies may belong to the same entity as the component but don't forget that
 *     entity position and rotation are applied after connection of components thus such
 *     approach may cause bugs with bodies having non-zero position/rotation.
 * </p>
 */
public abstract class BaseJointComponent <TJoint extends Joint, TDef extends JointDef>
        implements Component, DisposablePhysicsComponent, CreatablePhysicsComponent, JointPhysicsComponent {
    private final String entityATag, entityBTag;
    /* May be shared across multiple instances created by the same declaration */
    private final TDef def;

    private Entity entity;
    private BaseBodyComponent bodyComponentA, bodyComponentB;
    private TJoint joint;
    private APhysicsSystem physicsSystem;

    protected BaseJointComponent(String entityATag, String entityBTag, TDef def) {
        this.entityATag = entityATag;
        this.entityBTag = entityBTag;
        this.def = def;
    }

    protected Entity entity() {
        return entity;
    }

    @Override
    public TJoint joint() {
        return joint;
    }

    @Override
    public void onConnect(Entity entity) {
        this.entity = entity;
        physicsSystem = entity.game().systems().get("physics");
        physicsSystem.createComponent(this);
    }

    @Override
    public void onDisconnect(Entity entity) {
        physicsSystem.disposeComponent(this);
    }

    @Override
    public void dispose() {
        // If at least one of body components is already destroyed then it's unsafe to
        // call `destroyJoint`. If the component will be destroyed then the joint will
        // be destroyed automatically so it's safe to keep it.
        if (bodyComponentA.isAlive() && bodyComponentB.isAlive()) {
            physicsSystem.world().destroyJoint(joint);
        }

        joint = null;
    }

    @Override
    public void create() {
        TDef def = setupJointDef(this.def);
        joint = (TJoint) physicsSystem.world().createJoint(def);
        joint.setUserData(this);
    }

    protected TDef setupJointDef(TDef jointDef) {
        TaggingSystem taggingSystem = entity.game().systems().get("tagging");

        Entity entityA = entity, entityB = entity;

        if (null != entityATag) entityA = taggingSystem.group(entityATag).getOnly();
        if (null != entityBTag) entityB = taggingSystem.group(entityBTag).getOnly();

        if (entityA == entityB) throw new IllegalStateException(
                "Unable to create joint with only one entity; tags - '"
                        + entityATag + "' and '" + entityBTag + "'.");

        bodyComponentA = entityA.components().get("body");
        bodyComponentB = entityB.components().get("body");

        jointDef.bodyA = bodyComponentA.body();
        jointDef.bodyB = bodyComponentB.body();

        return jointDef;
    }
}
