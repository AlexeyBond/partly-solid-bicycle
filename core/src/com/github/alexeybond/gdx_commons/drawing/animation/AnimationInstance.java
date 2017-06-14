package com.github.alexeybond.gdx_commons.drawing.animation;

import com.badlogic.gdx.utils.Disposable;

/**
 *
 */
public interface AnimationInstance extends Disposable {
    void update(float deltaTime);

    KeyFrame currentFrame();

    void setSequence(String sequenceName);
}
