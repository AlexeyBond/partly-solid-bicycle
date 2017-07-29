package com.github.alexeybond.partly_solid_bicycle.application.impl.layers;

import com.github.alexeybond.partly_solid_bicycle.application.Layer;
import com.github.alexeybond.partly_solid_bicycle.application.Screen;

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
