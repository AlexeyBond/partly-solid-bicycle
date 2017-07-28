package com.github.alexeybond.partly_solid_bicycle.drawing;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

/**
 *
 */
public class DrawingState {
    final Batch batch;
    final ShapeRenderer shaper;
    InternalDrawingState state = InternalDrawingState.NONE;

    public DrawingState(Batch batch, ShapeRenderer shaper) {
        this.batch = batch;
        this.shaper = shaper;
    }

    public Batch beginBatch() {
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
