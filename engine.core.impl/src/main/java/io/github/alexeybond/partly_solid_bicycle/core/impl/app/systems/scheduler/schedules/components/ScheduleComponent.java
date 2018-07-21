package io.github.alexeybond.partly_solid_bicycle.core.impl.app.systems.scheduler.schedules.components;

import io.github.alexeybond.partly_solid_bicycle.core.impl.event.listeners.ForwardingSubscription;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.Schedule;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.Scheduler;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.SchedulerEntry;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Channel;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Signal;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeAttachmentListener;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.FromAttribute;
import org.jetbrains.annotations.NotNull;

public abstract class ScheduleComponent implements NodeAttachmentListener {
    private SchedulerEntry entry;

    private ForwardingSubscription<Signal> subscription;

    @FromAttribute(value = "scheduler", defaultPath = "/sys/scheduler")
    public Scheduler scheduler;

    @FromAttribute("target")
    public Channel<Signal> targetChannel;

    @Override
    public void onAttached(@NotNull LogicNode node) {
        entry = scheduler.schedule(getSchedule());
        subscription = new ForwardingSubscription<Signal>(targetChannel);
        subscription.subscribe(entry);
    }

    @Override
    public void onDetached(@NotNull LogicNode node) {
        subscription.clear();
        entry.terminate();
        entry = null;
    }

    @NotNull
    protected abstract Schedule getSchedule();
}
