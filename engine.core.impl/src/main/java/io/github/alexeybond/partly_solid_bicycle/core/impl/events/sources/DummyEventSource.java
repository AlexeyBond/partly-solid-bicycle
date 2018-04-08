package io.github.alexeybond.partly_solid_bicycle.core.impl.events.sources;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.exceptions.NoEventOriginException;
import org.jetbrains.annotations.NotNull;

public class DummyEventSource extends DefaultEventSource<DummyEventSource> {
    public DummyEventSource(int capacity) {
        super(capacity);
    }

    @NotNull
    @Override
    public DummyEventSource origin() throws NoEventOriginException {
        return this;
    }

    public void trigger(Object initiator) {
        trigger(this, initiator);
    }
}
