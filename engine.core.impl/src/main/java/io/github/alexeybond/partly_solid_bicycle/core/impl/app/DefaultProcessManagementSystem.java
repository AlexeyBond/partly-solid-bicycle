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

import java.util.Collections;
import java.util.List;

/**
 * Process manager that is a {@link System system} in terms of ECS.
 */
@Component(name = "processManager", kind = "system")
public class DefaultProcessManagementSystem
        extends DefaultProcessManager
        implements ProcessManagementSystem {
    private ProcessManager master;

    @Optional
    public String masterId = "processManager";

    public String processName;

    /**
     * List of process order hints.
     * <p>
     * Each nested list represents a sequence of names of processes depending on each other.
     * For example in this case:
     * <pre>
     * "orderHints": [
     *  ["a", "b", "c"],
     *  ["b", "z"],
     *  ["x", "w"],
     * ]
     * </pre>
     * the following process dependencies are configured:
     * <ul>
     * <li>"b" depends on "a"</li>
     * <li>"c" depends on "b"</li>
     * <li>"z" depends on "b"</li>
     * <li>"w" depends on "x"</li>
     * </ul>
     * </p>
     */
    @Optional
    public List<List<String>> orderHints = Collections.emptyList();

    @Override
    public void onJoin(@NotNull Engine owner, @NotNull Id<System> id)
            throws Exception {
        for (List<String> hint : orderHints) {
            for (int i = 1; i < hint.size(); i++) {
                orderHint(hint.get(i - 1), hint.get(i));
            }
        }

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
