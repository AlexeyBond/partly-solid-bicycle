package io.github.alexeybond.partly_solid_bicycle.core.impl.app.systems.scheduler.schedules;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.Schedule;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.ScheduleCallback;
import org.jetbrains.annotations.NotNull;

public class OnceSchedule implements Schedule {
    private final double time;

    public OnceSchedule(double time) {
        this.time = time;
    }

    @Override
    public void init(@NotNull ScheduleCallback callback) {
        callback.scheduleNext(time);
    }

    @Override
    public void repeat(double currentTime, @NotNull ScheduleCallback callback) {
        callback.cancel();
    }
}
