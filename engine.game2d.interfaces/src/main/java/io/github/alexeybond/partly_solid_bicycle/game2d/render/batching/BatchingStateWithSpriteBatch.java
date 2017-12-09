package io.github.alexeybond.partly_solid_bicycle.game2d.render.batching;

import com.badlogic.gdx.graphics.g2d.Batch;
import org.jetbrains.annotations.NotNull;

public interface BatchingStateWithSpriteBatch extends BatchingState {
    @NotNull
    Batch beginBatch();
}
