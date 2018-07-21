package io.github.alexeybond.partly_solid_bicycle.core.impl.app.systems.scheduler.schedules;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.Schedule;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.ScheduleCallback;
import org.jetbrains.annotations.NotNull;

public class PeriodicSchedule implements Schedule {
    private final double start, interval;

    public PeriodicSchedule(double start, double interval) {
        this.start = start;
        this.interval = interval;
    }

    @Override
    public void init(@NotNull ScheduleCallback callback) {
        callback.scheduleNext(start);
    }

    @Override
    public void repeat(double currentTime, @NotNull ScheduleCallback callback) {
        callback.scheduleNext(interval);
    }
}
