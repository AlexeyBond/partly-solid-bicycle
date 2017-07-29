package com.github.alexeybond.partly_solid_bicycle.music.impl;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.github.alexeybond.partly_solid_bicycle.music.FadeFunction;
import com.github.alexeybond.partly_solid_bicycle.music.FadeFunctions;
import com.github.alexeybond.partly_solid_bicycle.music.MusicPlayer;

/**
 *
 */
public class MusicPlayerImpl implements MusicPlayer {
    private final Music.OnCompletionListener endCallback
            = new Music.OnCompletionListener() {
        @Override
        public void onCompletion(Music music) {
            if (music != current) return;
            next();
        }
    };

    private final Array<Music> queue = new Array<Music>(true, 2);

    private FadeFunction fadeInFunction = FadeFunctions.LINEAR;
    private FadeFunction fadeOutFunction = FadeFunctions.LINEAR;
    private float fadeOut = 2, fadeIn = 2, crossFade = 1;
    private float fadeProgress;

    private Music current = null;
    private Music prev = null;

    @Override
    public MusicPlayer setFade(float out, float cross, float in) {
        if (cross > out || cross > in || out < 0 || in < 0 || cross < 0) {
            throw new IllegalArgumentException("Illegal fade timings: " +
                    out + "/" + cross + "/" + in + ".");
        }

        this.fadeOut = out;
        this.fadeIn = in;
        this.crossFade = cross;
        return this;
    }

    @Override
    public MusicPlayer setFadeFunction(FadeFunction out, FadeFunction in) {
        this.fadeOutFunction = null == out ? FadeFunctions.LINEAR : out;
        this.fadeInFunction = null == in ? FadeFunctions.LINEAR : in;
        return this;
    }

    @Override
    public MusicPlayer enqueue(Music music, boolean loop) {
        if (null != music) {
            music.setLooping(loop);
            music.setOnCompletionListener(endCallback);
        }

        queue.add(music);

        return this;
    }

    @Override
    public MusicPlayer stop() {
        if (null != prev) {
            prev.stop();
            prev = null;
        }

        if (null != current) {
            current.stop();
            current = null;
        }

        queue.clear();

        return this;
    }

    @Override
    public MusicPlayer update(float dt) {
        if (fadeProgress < 0) {
            return this;
        }

        fadeProgress += dt;
        if (null != prev) prev.setVolume(prevVolume());
        if (null != current) current.setVolume(curVolume());

        if (fadeProgress > (fadeIn + fadeOut - crossFade)) {
            if (null != prev) prev.stop();
            fadeProgress = -1;
        }

        return this;
    }

    @Override
    public MusicPlayer next() {
        if (null != prev && null != current) {
            prev.stop();
            prev = null;
        }

        if (null != current) {
            prev = current;
        }

        current = queue.size > 0 ? queue.removeIndex(0) : null;

        if (null != current) current.play();

        fadeProgress = null == prev ? (fadeOut - crossFade) : 0;

        update(0);

        return this;
    }

    private float prevVolume() {
        if (fadeProgress < 0) return 0;
        float progress = Math.max(0f, 1f - (fadeProgress / fadeOut));
        return fadeOutFunction.get(progress);
    }

    private float curVolume() {
        if (fadeProgress < 0) return 1;
        float progress = (fadeProgress - (fadeIn - crossFade)) / fadeIn;
        progress = MathUtils.clamp(progress, 0f, 1f);
        return fadeInFunction.get(progress);
    }
}
