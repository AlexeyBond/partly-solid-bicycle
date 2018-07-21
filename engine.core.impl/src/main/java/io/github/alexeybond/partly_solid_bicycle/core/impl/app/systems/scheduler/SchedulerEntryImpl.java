package io.github.alexeybond.partly_solid_bicycle.core.impl.app.systems.scheduler;

import io.github.alexeybond.partly_solid_bicycle.core.impl.event.notifier.ArrayNotifier;
import io.github.alexeybond.partly_solid_bicycle.core.impl.event.topic.ImmediateTopic;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.Schedule;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.ScheduleCallback;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.SchedulerEntry;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Signal;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;

class SchedulerEntryImpl implements Comparable<SchedulerEntryImpl>, ScheduleCallback, SchedulerEntry {
    @NotNull
    private final Queue<SchedulerEntryImpl> queue;

    @NotNull
    private final Schedule schedule;

    private double nextTime = Double.MIN_VALUE;

    private final ImmediateTopic<Signal> topic;

    private boolean scheduled = false;

    SchedulerEntryImpl(@NotNull Queue<SchedulerEntryImpl> queue, @NotNull Schedule schedule) {
        this.queue = queue;
        this.schedule = schedule;

        schedule.init(this);

        topic = new ImmediateTopic<Signal>(new ArrayNotifier<Signal>(1));
    }

    @Override
    public int compareTo(@NotNull SchedulerEntryImpl o) {
        return Double.compare(nextTime, o.nextTime);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void scheduleNext(double time) {
        if (scheduled) throw new IllegalStateException("Already scheduled");
        nextTime = time;
        queue.add(this);
        scheduled = true;
    }

    double getNextTime() {
        return nextTime;
    }

    void run() {
        try {
            topic.send(Signal.SIGNAL);
        } finally {
            scheduled = false;
            schedule.repeat(nextTime, this);
        }
    }

    @Override
    public Object subscribe(@NotNull Listener<? super Signal> listener) {
        return topic.subscribe(listener);
    }

    @Override
    public void unsubscribe(Object token, @NotNull Listener<? super Signal> listener) {
        topic.unsubscribe(token, listener);
    }

    @Override
    public void terminate() {
        queue.remove(this);
    }
}
