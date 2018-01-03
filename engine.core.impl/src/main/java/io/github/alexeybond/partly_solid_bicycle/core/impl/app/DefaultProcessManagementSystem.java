package io.github.alexeybond.partly_solid_bicycle.core.impl.app;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ProcessManager;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.Engine;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.System;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ecs.systems.ProcessManagementSystem;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Process manager that is a {@link System system} in terms of ECS.
 */
@Component({"processManager"})
public class DefaultProcessManagementSystem
        extends DefaultProcessManager
        implements ProcessManagementSystem {
    private ProcessManager master;

    @Optional
    public String masterId = "processManager";

    public String processName;

    @Override
    public void onJoin(@NotNull Engine owner, @NotNull Id<System> id)
            throws Exception {
        master = owner.getScope()
                .<ProcessManagementSystem>get(owner.getScope().getIdSet().get(masterId)).get();
        master.addProcess(processName, this);
    }

    @Override
    public void onLeave(@NotNull Engine owner, @NotNull Id<System> id)
            throws Exception {
        master.removeProcess(processName, this);
    }

    @Override
    public void onDispose(@NotNull Engine owner, @NotNull Id<System> id)
            throws Exception {
        onLeave(owner, id);
    }

    @Nullable
    @Override
    public System forward(@NotNull Engine from, @NotNull Engine to) {
        return this;
    }
}
