package io.github.alexeybond.partly_solid_bicycle.core.impl.ioc;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCContainer;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCContainerHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Simple {@link IoCContainerHolder container holder} implementation that uses one container for all threads.
 *
 * <p>
 *  This implementation is safe to use when at most one PSB application at time runs in JVM.
 * </p>
 */
public class SingleApplicationHolder implements IoCContainerHolder {
    private IoCContainer container;

    @NotNull
    @Override
    public IoCContainer get() {
        IoCContainer container = this.container;

        if (null == container) throw new IllegalStateException("IoC container not initialized.");

        return container;
    }

    @Override
    public void set(@Nullable IoCContainer container) {
        this.container = container;
    }
}
