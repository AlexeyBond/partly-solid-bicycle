package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Signal;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;

public interface SchedulerEntry extends Topic<Signal> {
    void terminate();
}
