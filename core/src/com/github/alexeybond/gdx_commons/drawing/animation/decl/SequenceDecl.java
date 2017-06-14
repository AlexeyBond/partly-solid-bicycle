package com.github.alexeybond.gdx_commons.drawing.animation.decl;

import com.badlogic.gdx.utils.Array;

/**
 *
 */
public class SequenceDecl {
    public Array<KeyFrameDecl> frames;

    public boolean loop = true;

    public TransformDecl transform = null;
}
