package io.github.alexeybond.partly_solid_bicycle.core.impl.app.systems.scheduler;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.pm.ProcessManager;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.FloatVariable;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeAttachmentListener;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.FromAttribute;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Optional;
import org.jetbrains.annotations.NotNull;

@Component(
        kind = "system",
        name = "scheduler"
)
public class DefaultSchedulingSystem
        extends DefaultScheduler
        implements Runnable, NodeAttachmentListener {
    @FromAttribute(value = "currentTimeVar", defaultPath = "/time/current")
    public FloatVariable currentTime;

    @FromAttribute(value = "processManager", defaultPath = "/pm")
    public ProcessManager processManager;

    @Optional
    public String processId;

    @Override
    public void run() {
        executeUntil(currentTime.getDouble());
    }

    @Override
    public void onAttached(@NotNull LogicNode node) {
        processManager.addProcess(processId, this);
    }

    @Override
    public void onDetached(@NotNull LogicNode node) {
        processManager.removeProcess(processId, this);
    }
}
