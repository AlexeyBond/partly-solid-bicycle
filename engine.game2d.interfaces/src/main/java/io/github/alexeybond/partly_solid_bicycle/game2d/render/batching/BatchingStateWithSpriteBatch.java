package io.github.alexeybond.partly_solid_bicycle.game2d.render.batching;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface BatchingStateWithSpriteBatch extends BatchingState {
    Batch beginBatch();
}
