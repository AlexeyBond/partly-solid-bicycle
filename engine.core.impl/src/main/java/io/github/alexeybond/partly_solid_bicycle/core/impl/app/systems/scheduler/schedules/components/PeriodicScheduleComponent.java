package io.github.alexeybond.partly_solid_bicycle.core.impl.app.systems.scheduler.schedules.components;

import io.github.alexeybond.partly_solid_bicycle.core.impl.app.systems.scheduler.schedules.PeriodicSchedule;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.scheduler.Schedule;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.FloatVariable;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.FromAttribute;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Optional;
import org.jetbrains.annotations.NotNull;

@Component(
        kind = "component",
        name = "schedule.periodic"
)
public class PeriodicScheduleComponent extends ScheduleComponent {
    @FromAttribute(value = "currentTimeVar", defaultPath = "/time/current")
    public FloatVariable currentTime;

    public double period;

    @Optional
    public double startDelay = 0;

    @Optional
    public double startTime = -1;

    @NotNull
    @Override
    protected Schedule getSchedule() {
        double currentTime = this.currentTime.getDouble();

        if (startTime < 0) {
            startTime = currentTime + startDelay;
        } else {
            double timesTriggered = Math.floor((currentTime - startTime) / period) + 1.0;

            startTime += period * timesTriggered;
        }

        return new PeriodicSchedule(startTime, period);
    }
}
