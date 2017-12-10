package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.batching;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.jetbrains.annotations.NotNull;

public interface BatchingStateWithShapeRenderer extends BatchingState {
    @NotNull
    ShapeRenderer beginShapes(@NotNull ShapeRenderer.ShapeType type);
}
