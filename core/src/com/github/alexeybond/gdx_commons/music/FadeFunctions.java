package com.github.alexeybond.gdx_commons.music;

/**
 *
 */
public enum FadeFunctions implements FadeFunction {
    /**
     * Changes volume of fading tracks linearly.
     */
    LINEAR {
        @Override
        public float get(float progress) {
            return progress;
        }
    },
    /**
     * Keeps volume of fading track at maximum. Useful for fading-in a track starting
     * with a part with a low (and probably growing) volume.
     */
    UNIT {
        @Override
        public float get(float progress) {
            return 1;
        }
    },
}
