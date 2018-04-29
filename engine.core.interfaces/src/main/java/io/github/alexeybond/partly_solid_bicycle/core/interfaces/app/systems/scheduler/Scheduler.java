package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import org.jetbrains.annotations.NotNull;

public interface Scheduler {
    @NotNull
    Topic<Void> schedule(@NotNull Schedule schedule);
}
