package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler;

import org.jetbrains.annotations.NotNull;

public interface Scheduler {
    @NotNull
    SchedulerEntry schedule(@NotNull Schedule schedule);
}
