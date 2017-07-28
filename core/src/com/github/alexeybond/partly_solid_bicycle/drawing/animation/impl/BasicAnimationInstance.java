package com.github.alexeybond.partly_solid_bicycle.drawing.animation.impl;

import com.github.alexeybond.partly_solid_bicycle.drawing.animation.AnimationInstance;
import com.github.alexeybond.partly_solid_bicycle.drawing.animation.KeyFrame;

/**
 * Basic implementation of {@link AnimationInstance}. Does not trigger any events described in keyframes
 * but provides protected method that may be overridden to trigger events.
 */
class BasicAnimationInstance implements AnimationInstance {
    private final Animation animation;
    private Animation.Sequence currentSequence;
    private Animation.KeyFrameImpl currentFrame;
    private int currentFrameIndex;

    /** {@code true} when last frame of non-looping animation is reached */
    private boolean paused;
    private float timeAcc = 0;

    BasicAnimationInstance(Animation animation) {
        this.animation = animation;

        setSequence(animation.defaultSequence());
    }

    private void setSequence(Animation.Sequence sequence) {
        this.currentSequence = sequence;
        this.currentFrameIndex = 0;
        this.currentFrame = this.currentSequence.frames.get(0);
        this.paused = false;
    }

    @Override
    public void update(float deltaTime) {
        if (paused) return;

        timeAcc += deltaTime;

        while (currentFrame.duration <= timeAcc) {
            timeAcc -= currentFrame.duration;

            ++currentFrameIndex;

            if (currentFrameIndex >= currentSequence.frames.size) {
                if (currentSequence.loop) {
                    currentFrameIndex = 0;
                } else {
                    paused = true;
                    return;
                }
            }

            currentFrame = currentSequence.frames.get(currentFrameIndex);

            if (currentFrame.eventIndex >= 0) {
                triggerEvent(currentFrame.eventIndex);
            }
        }
    }

    @Override
    public KeyFrame currentFrame() {
        return currentFrame;
    }

    @Override
    public void setSequence(String sequenceName) {
        setSequence(animation.sequence(sequenceName));
    }

    protected void triggerEvent(int eventIndex) {
        // By default do nothing
    }

    @Override
    public void dispose() {
    }
}
