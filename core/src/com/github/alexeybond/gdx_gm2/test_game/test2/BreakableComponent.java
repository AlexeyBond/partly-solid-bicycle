package com.github.alexeybond.gdx_gm2.test_game.test2;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.FixturePhysicsComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.BaseBodyComponent;
import com.github.alexeybond.gdx_commons.game.systems.box2d_physics.components.FixtureDefFixtureComponent;
import com.github.alexeybond.gdx_commons.game.utils.destruction.Destroyer;
import com.github.alexeybond.gdx_commons.game.utils.destruction.impl.DestroyerImpl;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class BreakableComponent
        implements Component, EventListener<Component, Event<Component>> {
    private static final String[] FIXTURE_COMPONENT_NAMES = {
            "break-fixture-0",
            "break-fixture-1",
            "break-fixture-2",
            "break-fixture-3",
            "break-fixture-4",
            "break-fixture-5",
            "break-fixture-6",
            "break-fixture-7",
    };

    private final GameDeclaration gameDeclaration;
    private final String fixtureComponentName = "box fixture";
    private final String bodyComponentName = "body";
    private final String peaceClassName = "box-peace";

    private Event<Component> breakEvent;
    private int breakSubIdx = -1;
    private BaseBodyComponent bodyComponent;
    private FixturePhysicsComponent fixtureComponent;
    private Entity entity;

    private Destroyer destroyer = new DestroyerImpl();

    public BreakableComponent(GameDeclaration gameDeclaration) {
        this.gameDeclaration = gameDeclaration;
    }

    @Override
    public void onConnect(Entity entity) {
        breakEvent = entity.events().event("break", Event.<Component>make());
        breakSubIdx = breakEvent.subscribe(this);
        this.entity = entity;
        bodyComponent = entity.components().get(bodyComponentName);
        fixtureComponent = entity.components().get(fixtureComponentName);
    }

    @Override
    public void onDisconnect(Entity entity) {
        breakSubIdx = breakEvent.unsubscribe(breakSubIdx);
    }

    @Override
    public boolean onTriggered(Component component, Event<Component> event) {
        int vc = ((PolygonShape) fixtureComponent.fixture().getShape()).getVertexCount();
        List<Vector2> verts = new ArrayList<Vector2>(vc);

        for (int i = 0; i < vc; i++) {
            Vector2 v = new Vector2();
            ((PolygonShape) fixtureComponent.fixture().getShape()).getVertex(i, v);
            verts.add(v);
        }

        destroyer.config().crackLengthMin = 40;
        destroyer.config().crackLengthMax = 40;
        destroyer.config().minTriArea = 1;
        destroyer.config().forkRaysMin = 2;
        destroyer.config().forkRaysMax = 2;
        destroyer.config().forkAngleRange = 80;
        destroyer.config().forkAngleRestrictRangeFraction = 0.1f;

        destroyer.prepare(verts);
        destroyer.startCenter();
        List<ArrayList<Vector2>> broken = destroyer.compute();

        PolygonShape polygonShape = new PolygonShape();

        for (List<Vector2> part : broken) {
            Entity partE = new Entity(entity.game());

            gameDeclaration.getEntityClass(peaceClassName).apply(partE, gameDeclaration);

            int I = 2, N = 0, idx;
            Vector2[] verts1 = new Vector2[8];

            while (I < part.size()) {
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = polygonShape;
                fixtureDef.density = 0.01f;
                fixtureDef.restitution = 0.1f;

                idx = 0;
                verts1[idx++] = part.get(0);

                --I;

                for (; idx < verts1.length && I < part.size(); ++idx, ++I) {
                    verts1[idx] = part.get(I);
                }

                if (idx == verts1.length) {
                    polygonShape.set(verts1);
                } else {
                    polygonShape.set(Arrays.copyOf(verts1, idx));
                }

                partE.components().add(
                        FIXTURE_COMPONENT_NAMES[N],
                        new FixtureDefFixtureComponent(fixtureDef));
                ++N;
            }
        }

        polygonShape.dispose();

        destroyer.reset();

        entity.destroy();
        return true;
    }

    public static class Decl implements ComponentDeclaration {
        @Override
        public Component create(GameDeclaration gameDeclaration) {
            return new BreakableComponent(gameDeclaration);
        }
    }
}
