package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler;

public interface ScheduleCallback {
    void cancel();

    void scheduleNext(double time);
}
