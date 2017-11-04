package io.github.alexeybond.partly_solid_bicycle.core.impl.ioc;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCContainer;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCContainerHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Simple {@link IoCContainerHolder container holder} implementation that stores potentially different
 * containers for different threads.
 *
 * <p>
 *  This implementation uses thread-local variable instead of simple field so is a bit less efficient but
 *  safe in cases when multiple PSB applications may run in a JVM.
 * </p>
 */
public class MultiApplicationHolder implements IoCContainerHolder {
    private final ThreadLocal<IoCContainer> containerStorage = new InheritableThreadLocal<IoCContainer>();

    @NotNull
    @Override
    public IoCContainer get() {
        IoCContainer container = containerStorage.get();

        if (null == container) {
            throw new IllegalStateException("IoC container not initialized in thread " +
                    Thread.currentThread().toString() + ".");
        }

        return container;
    }

    @Override
    public void set(@Nullable IoCContainer container) {
        containerStorage.set(container);
    }
}
