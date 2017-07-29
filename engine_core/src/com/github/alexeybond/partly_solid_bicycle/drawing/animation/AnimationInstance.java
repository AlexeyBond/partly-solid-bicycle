package com.github.alexeybond.partly_solid_bicycle.drawing.animation;

import com.badlogic.gdx.utils.Disposable;

/**
 *
 */
public interface AnimationInstance extends Disposable {
    void update(float deltaTime);

    KeyFrame currentFrame();

    void setSequence(String sequenceName);
}
