package com.github.alexeybond.partly_solid_bicycle.ext.destruction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Helper that helps to build physical and visual components from output of destroyer.
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

        fixtureIndex = -1;
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

        ++fixtureIndex;
        return fixtureDef;
    }

    public Vector2 center() {
        return center;
    }

    public String fixtureName() {
        return FIXTURE_NAMES[fixtureIndex];
    }

    private TextureRegion texture;
    private float ux, uy, vx, vy;
    private float u0, v0;

    public void setupTexturePlacement(TextureRegion texture, float[] placement) {
        this.texture = texture;
        float iw = 1f / (float) texture.getRegionWidth(), ih = 1f / (float) texture.getRegionHeight();
        ux = placement[0] * iw; uy = placement[1] * iw;
        vx = placement[3] * ih; vy = placement[4] * ih;
        u0 = placement[2]; v0 = placement[5];
    }

    public short[] partRenderIndices() {
        int n = part.size(), idx = 0;
        short[] indices = new short[(n-2)*3];

        for (short i = 2; i < n; i++) {
            indices[idx++] = 0;
            indices[idx++] = (short) (i - 1);
            indices[idx++] = i;
        }

        return indices;
    }

    public float[] partRenderVertices() {
        int n = part.size(), idx = 0;
        float[] vertices = new float[n * 5];
        float white = Color.WHITE.toFloatBits();

        float u1 = texture.getU(), u2 = texture.getU2();
        float v1 = texture.getV(), v2 = texture.getV2();

        for (int i = 0; i < n; i++) {
            Vector2 pos = part.get(i);

            float uk = ux * pos.x + uy * pos.y + u0;
            float vk = vx * pos.x + vy * pos.y + v0;

            vertices[idx++] = pos.x - center.x;
            vertices[idx++] = pos.y - center.y;
            vertices[idx++] = white;
            vertices[idx++] = MathUtils.lerp(u1, u2, uk);
            vertices[idx++] = MathUtils.lerp(v2, v1, vk);
        }

        return vertices;
    }
}
