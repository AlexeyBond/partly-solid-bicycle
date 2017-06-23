package com.github.alexeybond.gdx_commons.game.systems.render.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;

/**
 *
 */
public class PolySpriteComponent extends BaseRenderComponent {
    // [ xl, yl, xl, yl, ... ]
    private final float[] localPositions;
    // [ x, y, c, u, v, ... ]
    private final float[] vertices;
    private final short[] triangles;
    private final Texture texture;

    protected PolySpriteComponent(
            String passName,
            float[] localPositions,
            float[] vertices,
            short[] triangles,
            Texture texture) {
        super(passName);
        this.localPositions = localPositions;
        this.vertices = vertices;
        this.triangles = triangles;
        this.texture = texture;
    }

    private static float[] extractLocalPositions(float[] vertices, int n) {
        float[] locals = new float[n * 2];
        int readIndex = 0, writeIndex = 0;

        for (int i = 0; i < n; i++) {
            locals[writeIndex++] = vertices[readIndex++];
            locals[writeIndex++] = vertices[readIndex++];
            readIndex += 3;
        }

        return locals;
    }

    public PolySpriteComponent(
            String passName,
            float[] vertices,
            short[] triangles,
            Texture texture) {
        this(passName, extractLocalPositions(vertices, vertices.length / 5), vertices, triangles, texture);
    }

    @Override
    public void draw(DrawingContext context) {
        PolygonSpriteBatch psb = (PolygonSpriteBatch) context.state().beginBatch();

        recomputeVertices();

        psb.draw(texture,
                vertices, 0, vertices.length,
                triangles, 0, triangles.length);
    }

    private void recomputeVertices() {
        float posX = position.ref().x, posY = position.ref().y;
        float rot = rotation.get();

        float cosR = MathUtils.cosDeg(rot), sinR = MathUtils.sinDeg(rot);

        int vIdx = 0;
        for (int i = 0; i < localPositions.length; i += 2) {
            float x = localPositions[i], y = localPositions[i + 1];

            vertices[vIdx++] = posX + x * cosR - y * sinR;
            vertices[vIdx++] = posY + y * cosR + x * sinR;
            vIdx += 3;
        }
    }
}
