package io.github.alexeybond.partly_solid_bicycle.game2d.impl.render.batching;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.batching.BatchingMode;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.batching.BatchingState;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.batching.BatchingStateWithShapeRenderer;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.batching.BatchingStateWithSpriteBatch;
import org.jetbrains.annotations.NotNull;

/**
 * Default implementation of {@link BatchingState}. Supports {@link Batch}'es and {@link ShapeRenderer}'s.
 */
public class DefaultBatchingState
        implements BatchingState,
        BatchingStateWithSpriteBatch,
        BatchingStateWithShapeRenderer {
    final Batch batch;
    final ShapeRenderer shapeRenderer;
    Object x;
    DefaultBatchingInternalState internalState;

    public DefaultBatchingState(Batch batch, ShapeRenderer shapeRenderer) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
    }

    @NotNull
    @Override
    public ShapeRenderer beginShapes(@NotNull ShapeRenderer.ShapeType type) {
        return internalState.beginShapes(this, type);
    }

    @NotNull
    @Override
    public Batch beginBatch() {
        return internalState.beginBatch(this);
    }

    @Override
    public void flush() {
        internalState.flush(this);
    }

    @Override
    public <T> T begin(@NotNull BatchingMode<T> mode) {
        return mode.begin(this);
    }

    @Override
    public void end() {
        internalState.end(this);
    }

    @Override
    public void setProjection(@NotNull Matrix4 projectionMatrix) {
        batch.setProjectionMatrix(projectionMatrix);
        shapeRenderer.setProjectionMatrix(projectionMatrix);
    }
}
