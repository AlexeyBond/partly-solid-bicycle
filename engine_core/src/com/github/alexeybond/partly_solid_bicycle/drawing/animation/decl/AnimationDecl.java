package com.github.alexeybond.partly_solid_bicycle.drawing.animation.decl;

import com.github.alexeybond.partly_solid_bicycle.drawing.animation.impl.Animation;

import java.util.HashMap;

/**
 *
 */
public class AnimationDecl {
    public HashMap<String, SequenceDecl> sequences;

    public String defaultSequence = "default";

    public TransformDecl transform = null;

    private transient Animation animation = null;

    public Animation getAnimation() {
        if (null != animation) return animation;

        return animation = new Animation(this);
    }
}
