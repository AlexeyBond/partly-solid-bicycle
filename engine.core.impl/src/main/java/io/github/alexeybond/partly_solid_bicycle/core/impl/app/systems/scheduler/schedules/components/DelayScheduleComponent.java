package io.github.alexeybond.partly_solid_bicycle.core.impl.app.systems.scheduler.schedules.components;

import io.github.alexeybond.partly_solid_bicycle.core.impl.app.systems.scheduler.schedules.OnceSchedule;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.Schedule;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.FloatVariable;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.FromAttribute;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Optional;
import org.jetbrains.annotations.NotNull;

@Component(
        kind = "component",
        name = "schedule.delay"
)
public class DelayScheduleComponent extends ScheduleComponent {
    @FromAttribute(value = "currentTimeVar", defaultPath = "/time/current")
    public FloatVariable currentTime;

    public double delay;

    @Optional
    public double time = -1;

    @NotNull
    @Override
    protected Schedule getSchedule() {
        time = time > 0 ? time : currentTime.getDouble() + delay;
        return new OnceSchedule(time);
    }
}
