package com.github.alexeybond.gdx_commons.application.impl.layers;

import com.github.alexeybond.gdx_commons.application.Layer;
import com.github.alexeybond.gdx_commons.application.Screen;

/**
 *
 */
public abstract class LayerAdapter implements Layer {
    @Override
    public void update(float dt) {
    }

    @Override
    public void enter(Screen prev) {
    }

    @Override
    public void leave(Screen next) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void unpause() {
    }
}
