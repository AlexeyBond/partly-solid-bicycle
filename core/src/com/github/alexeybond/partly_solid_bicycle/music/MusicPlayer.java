package com.github.alexeybond.partly_solid_bicycle.music;

import com.badlogic.gdx.audio.Music;

/**
 * Music player plays music (C.O.)
 *
 * Music player provides a functionality not implemented in LibGDX - fading music in/out when
 * track is switched.
 */
public interface MusicPlayer {
    /**
     * Set fade timings.
     *
     * <pre>
     *               "out"
     *       /--------------------/
     *      /                    /
     *     V                    V
     *     |LALALALaLaLALaLaLala|                 < previous music
     *                 |        |
     *                 |lalalAlALALaLALALALA|     < new music
     *                 |        |           |
     *                 ^        ^           ^
     *                /________/           /
     *               /  "cross"           /
     *              /--------------------/
     *                      "in"
     * </pre>
     */
    MusicPlayer setFade(float out, float cross, float in);

    /**
     * Set fade functions.
     */
    MusicPlayer setFadeFunction(FadeFunction out, FadeFunction in);

    /**
     * Enqueue a music. The enqueued music will play after next {@link #next()} call or
     * when currently playing non-looping track finishes.
     *
     * When track is switched automatically fade function will not be applied (or will be
     * applied only to the next track).
     */
    MusicPlayer enqueue(Music music, boolean loop);

    /**
     * Stop all playing tracks (without fading).
     */
    MusicPlayer stop();

    /**
     * Update tracks volume.
     */
    MusicPlayer update(float dt);

    /**
     * Switch to next track.
     *
     * If any track is fading out it will be stopped (unless there is no track fading in).
     */
    MusicPlayer next();
}
