package io.github.alexeybond.partly_solid_bicycle.game2d.impl.render.batching;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public enum DefaultBatchingInternalState {
    INITIAL {
        @Override
        void flush(DefaultBatchingState state) {
        }

        @Override
        void end(DefaultBatchingState state) {
        }
    },

    SHAPE {
        @Override
        void flush(DefaultBatchingState state) {
            state.shapeRenderer.flush();
        }

        @Override
        void end(DefaultBatchingState state) {
            state.shapeRenderer.end();
        }

        void begin(DefaultBatchingState state, ShapeRenderer.ShapeType type) {
            begin(state);
            state.shapeRenderer.begin(type);
            state.x = type;
        }

        @Override
        ShapeRenderer beginShapes(DefaultBatchingState state, ShapeRenderer.ShapeType shapeType) {
            if (shapeType == state.x) return state.shapeRenderer;
            return super.beginShapes(state, shapeType);
        }
    },

    BATCH {
        @Override
        void begin(DefaultBatchingState state) {
            super.begin(state);
            state.batch.begin();
        }

        @Override
        void end(DefaultBatchingState state) {
            state.batch.end();
        }

        @Override
        void flush(DefaultBatchingState state) {
            state.batch.flush();
        }

        @Override
        Batch beginBatch(DefaultBatchingState state) {
            return state.batch;
        }
    };

    void begin(DefaultBatchingState state) {
        state.internalState.end(state);
        state.internalState = this;
    }

    void end(DefaultBatchingState state) {
        INITIAL.begin(state);
    }

    abstract void flush(DefaultBatchingState state);

    Batch beginBatch(DefaultBatchingState state) {
        BATCH.begin(state);
        return state.batch;
    }

    ShapeRenderer beginShapes(DefaultBatchingState state, ShapeRenderer.ShapeType shapeType) {
        SHAPE.begin(state);
        return state.shapeRenderer;
    }
}
