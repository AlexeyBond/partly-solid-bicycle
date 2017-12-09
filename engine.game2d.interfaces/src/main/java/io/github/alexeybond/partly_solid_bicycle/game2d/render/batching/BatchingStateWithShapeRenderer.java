package io.github.alexeybond.partly_solid_bicycle.game2d.render.batching;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface BatchingStateWithShapeRenderer extends BatchingState {
    ShapeRenderer beginShapes(ShapeRenderer.ShapeType type);
}
