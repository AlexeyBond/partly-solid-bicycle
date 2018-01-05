package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.batching;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.jetbrains.annotations.NotNull;

public enum BatchingModes {
    ;
    public static final BatchingMode<Batch> SPRITES = new BatchingMode<Batch>() {
        @Override
        @NotNull
        public Batch begin(@NotNull BatchingState state) {
            BatchingStateWithSpriteBatch s = (BatchingStateWithSpriteBatch) state;

            try {
                return s.beginBatch();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static final BatchingMode<ShapeRenderer> LINES = new BatchingMode<ShapeRenderer>() {
        @Override
        @NotNull
        public ShapeRenderer begin(@NotNull BatchingState state) {
            BatchingStateWithShapeRenderer s = (BatchingStateWithShapeRenderer) state;

            try {
                return s.beginShapes(ShapeRenderer.ShapeType.Line);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static final BatchingMode<ShapeRenderer> FILLED = new BatchingMode<ShapeRenderer>() {
        @Override
        @NotNull
        public ShapeRenderer begin(@NotNull BatchingState state) {
            BatchingStateWithShapeRenderer s = (BatchingStateWithShapeRenderer) state;

            try {
                return s.beginShapes(ShapeRenderer.ShapeType.Filled);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static final BatchingMode<ShapeRenderer> POINTS = new BatchingMode<ShapeRenderer>() {
        @Override
        @NotNull
        public ShapeRenderer begin(@NotNull BatchingState state) {
            BatchingStateWithShapeRenderer s = (BatchingStateWithShapeRenderer) state;

            try {
                return s.beginShapes(ShapeRenderer.ShapeType.Point);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static final BatchingMode<Void> NONE = new BatchingMode<Void>() {
        @Override
        public Void begin(@NotNull BatchingState state) {
            state.end();
            return null;
        }
    };
}