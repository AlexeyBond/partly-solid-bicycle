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

    SpriteBatch beginBatch() {
        return state.beginBatch(this);
    }

    ShapeRenderer beginFilled() {
        return state.beginFilled(this);
    }

    ShapeRenderer beginLines() {
        return state.beginLines(this);
    }

    ShapeRenderer beginPoints() {
        return state.beginPoints(this);
    }

    void flush() {
        state.flush(this);
    }

    void end() {
        state.end(this);
    }

    void setProjection(Matrix4 matrix) {
        batch.setProjectionMatrix(matrix);
        shaper.setProjectionMatrix(matrix);
    }
}
