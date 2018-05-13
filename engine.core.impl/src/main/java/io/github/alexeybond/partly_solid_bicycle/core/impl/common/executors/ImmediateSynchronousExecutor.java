package io.github.alexeybond.partly_solid_bicycle.core.impl.common.executors;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public enum ImmediateSynchronousExecutor implements Executor {
    INSTANCE;

    @Override
    public void execute(@NotNull Runnable command) {
        command.run();
    }
}
