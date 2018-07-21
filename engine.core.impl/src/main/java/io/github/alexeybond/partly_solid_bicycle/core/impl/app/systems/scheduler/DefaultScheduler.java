package io.github.alexeybond.partly_solid_bicycle.core.impl.app.systems.scheduler;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.Schedule;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.Scheduler;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.SchedulerEntry;
import org.jetbrains.annotations.NotNull;

import java.util.PriorityQueue;

public class DefaultScheduler implements Scheduler {
    private final PriorityQueue<SchedulerEntryImpl> queue = new PriorityQueue<SchedulerEntryImpl>();

    @NotNull
    @Override
    public SchedulerEntry schedule(@NotNull Schedule schedule) {
        return new SchedulerEntryImpl(queue, schedule);
    }

    public void executeUntil(final double time) {
        while (!queue.isEmpty()) {
            if (queue.peek().getNextTime() > time) {
                return;
            }

            queue.poll().run();
        }
    }
}
