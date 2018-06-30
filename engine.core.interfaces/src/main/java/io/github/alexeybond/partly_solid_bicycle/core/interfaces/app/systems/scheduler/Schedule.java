package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler;

import org.jetbrains.annotations.NotNull;

public interface Schedule {
    /**
     * Initialize new scheduler entry.
     * <p>
     * No calls to {@code callback} methods will cause undefined behavior.
     * Interaction with {@code callback} after return will cause undefined behavior.
     *
     * @param callback {@link ScheduleCallback} instance that will update scheduler entry
     */
    void init(@NotNull ScheduleCallback callback);

    /**
     * Initialize new scheduler entry.
     * <p>
     * No calls to {@code callback} methods will cause undefined behavior.
     * Interaction with {@code callback} after return will cause undefined behavior.
     *
     * @param currentTime last time the entry was scheduled for
     * @param callback    {@link ScheduleCallback} instance that will update scheduler entry
     */
    void repeat(double currentTime, @NotNull ScheduleCallback callback);
}
