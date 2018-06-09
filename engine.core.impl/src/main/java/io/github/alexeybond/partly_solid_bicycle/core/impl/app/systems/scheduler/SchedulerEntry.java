package io.github.alexeybond.partly_solid_bicycle.core.impl.app.systems.scheduler;

import io.github.alexeybond.partly_solid_bicycle.core.impl.event.notifier.ArrayNotifier;
import io.github.alexeybond.partly_solid_bicycle.core.impl.event.topic.ImmediateTopic;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.Schedule;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.ScheduleCallback;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;

class SchedulerEntry implements Comparable<SchedulerEntry>, ScheduleCallback {
    @NotNull
    private final Queue<SchedulerEntry> queue;

    @NotNull
    private final Schedule schedule;

    private double nextTime = Double.MIN_VALUE;

    private final ImmediateTopic<Void> topic;

    SchedulerEntry(@NotNull Queue<SchedulerEntry> queue, @NotNull Schedule schedule) {
        this.queue = queue;
        this.schedule = schedule;

        schedule.init(this);

        topic = new ImmediateTopic<Void>(new ArrayNotifier<Void>(1));
    }

    @Override
    public int compareTo(@NotNull SchedulerEntry o) {
        return Double.compare(nextTime, o.nextTime);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void scheduleNext(double time) {
        nextTime = time;
        queue.add(this);
    }

    double getNextTime() {
        return nextTime;
    }

    Topic<Void> getTopic() {
        return topic;
    }

    void run() {
        topic.send(null);

        schedule.repeat(nextTime, this);
    }
}
