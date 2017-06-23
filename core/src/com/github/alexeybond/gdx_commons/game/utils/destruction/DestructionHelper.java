package com.github.alexeybond.gdx_commons.game.utils.destruction;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 */
public final class DestructionHelper implements Disposable {
    /** Box2D doesn't allow more than 8 vertices per shape. */
    public static final int MAX_PHYSICAL_SHAPE_VERTICES = 8;

    private static final String[] FIXTURE_NAMES = {
            "break-fixture-0",
            "break-fixture-1",
            "break-fixture-2",
            "break-fixture-3",
            "break-fixture-4",
            "break-fixture-5",
            "break-fixture-6",
            "break-fixture-7",
    };

    private final float[] shapeVertices = new float[MAX_PHYSICAL_SHAPE_VERTICES * 2];
    private final PolygonShape polygonShape = new PolygonShape();
    private final FixtureDef fixtureDef = new FixtureDef();

    {
        fixtureDef.shape = polygonShape;
    }

    @Override
    public void dispose() {
        polygonShape.dispose();
    }

    private ArrayList<Vector2> part;
    private final Vector2 center = new Vector2();

    private int fixtureIndex, vertex2Index, currentVertexIndex;

    public void beginPart(ArrayList<Vector2> part) {
        this.part = part;

        center.set(0,0);
        for (int i = 0; i < part.size(); i++) {
            center.add(part.get(i));
        }
        center.scl(1f / (float) part.size());

        fixtureIndex = 0;
        vertex2Index = 2;
    }

    private void addVertex(Vector2 vertex) {
        shapeVertices[currentVertexIndex++] = vertex.x - center.x;
        shapeVertices[currentVertexIndex++] = vertex.y - center.y;
    }

    public FixtureDef nextFixture() {
        if (vertex2Index >= part.size()) return null;

        currentVertexIndex = 0;

        addVertex(part.get(0));

        --vertex2Index;

        for (;
             currentVertexIndex < shapeVertices.length && vertex2Index < part.size();
             ++vertex2Index) {
            addVertex(part.get(vertex2Index));
        }

        if (currentVertexIndex == shapeVertices.length) {
            polygonShape.set(shapeVertices);
        } else {
            polygonShape.set(Arrays.copyOf(shapeVertices, currentVertexIndex));
        }

        return fixtureDef;
    }

    public Vector2 center() {
        return center;
    }

    public String fixtureName() {
        return FIXTURE_NAMES[fixtureIndex];
    }
}
