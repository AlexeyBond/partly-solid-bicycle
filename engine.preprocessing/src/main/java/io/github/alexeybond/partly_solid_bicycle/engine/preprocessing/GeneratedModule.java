package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module;

import java.util.Collection;

public abstract class GeneratedModule implements Module {
    @Override
    public void init(Collection<Object> env) {
        interact();
    }

    @Override
    public void shutdown() {
        interact();
    }

    private void interact() {
        throw new Error("Code for " + this.getClass().getCanonicalName() + " was not generated.");
    }
}
