package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler;

import org.jetbrains.annotations.NotNull;

public interface Schedule {
    void init(@NotNull ScheduleCallback callback);

    void repeat(double currentTime, @NotNull ScheduleCallback callback);
}
