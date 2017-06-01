package com.github.alexeybond.gdx_commons.drawing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

/**
 *
 */
public class DrawingState {
    final SpriteBatch batch = new SpriteBatch();
    final ShapeRenderer shaper = new ShapeRenderer();
    InternalDrawingState state = InternalDrawingState.NONE;

    public SpriteBatch beginBatch() {
        return state.beginBatch(this);
    }

    public ShapeRenderer beginFilled() {
        return state.beginFilled(this);
    }

    public ShapeRenderer beginLines() {
        return state.beginLines(this);
    }

    public ShapeRenderer beginPoints() {
        return state.beginPoints(this);
    }

    public void flush() {
        state.flush(this);
    }

    public void end() {
        state.end(this);
    }

    public void setProjection(Matrix4 matrix) {
        batch.setProjectionMatrix(matrix);
        shaper.setProjectionMatrix(matrix);
    }

    public void getProjection(Matrix4 out) {
        out.set(batch.getProjectionMatrix());
    }
}
