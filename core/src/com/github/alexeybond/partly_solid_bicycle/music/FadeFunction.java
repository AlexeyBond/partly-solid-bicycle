package com.github.alexeybond.partly_solid_bicycle.music;

/**
 * Function computing a volume of fading in/out track.
 *
 * For fading-out tracks parameter will change from 1 to 0, for fading-in from 0 to 1.
 */
public interface FadeFunction {
    float get(float progress);
}
