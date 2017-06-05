package com.github.alexeybond.gdx_commons.drawing;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 */
enum InternalDrawingState {
    NONE {
        @Override
        InternalDrawingState enter(DrawingState state) {
            return NONE;
        }

        @Override
        void leave(DrawingState state) {
        }

        @Override
        void flush(DrawingState state) {
        }
    },

    BATCH {
        @Override
        InternalDrawingState enter(DrawingState state) {
            state.batch.begin();
            return BATCH;
        }

        @Override
        void leave(DrawingState state) {
            state.batch.end();
        }

        @Override
        Batch beginBatch(DrawingState state) {
            return state.batch;
        }

        @Override
        void flush(DrawingState state) {
            state.batch.flush();
        }
    },

    FILLED {
        @Override
        ShapeRenderer beginFilled(DrawingState state) {
            return state.shaper;
        }

        @Override
        InternalDrawingState enter(DrawingState state) {
            state.shaper.begin(ShapeRenderer.ShapeType.Filled);
            return FILLED;
        }
    },

    LINES {
        @Override
        ShapeRenderer beginLines(DrawingState state) {
            return state.shaper;
        }

        @Override
        InternalDrawingState enter(DrawingState state) {
            state.shaper.begin(ShapeRenderer.ShapeType.Line);
            return LINES;
        }
    },

    POINTS {
        @Override
        ShapeRenderer beginPoints(DrawingState state) {
            return state.shaper;
        }

        @Override
        InternalDrawingState enter(DrawingState state) {
            state.shaper.begin(ShapeRenderer.ShapeType.Point);
            return POINTS;
        }
    };

    private DrawingState changeState(DrawingState state, InternalDrawingState internalState) {
        state.state.leave(state);
        state.state = internalState.enter(state);
        return state;
    }

    Batch beginBatch(DrawingState state) {
        return changeState(state, BATCH).batch;
    }

    ShapeRenderer beginFilled(DrawingState state) {
        return changeState(state, FILLED).shaper;
    }

    ShapeRenderer beginLines(DrawingState state) {
        return changeState(state, LINES).shaper;
    }

    ShapeRenderer beginPoints(DrawingState state) {
        return changeState(state, POINTS).shaper;
    }

    void end(DrawingState state) {
        state.state.leave(state);
        state.state = NONE;
    }

    abstract InternalDrawingState enter(DrawingState state);

    void leave(DrawingState state) {
        state.shaper.end();
    }

    void flush(DrawingState state) {
        state.shaper.flush();
    }
}
