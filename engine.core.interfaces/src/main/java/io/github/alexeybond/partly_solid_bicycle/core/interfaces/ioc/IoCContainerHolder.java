package io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Storage of {@link IoCContainer}'s. Depending on implementation may store separate containers
 * for different threads or use one container for all threads.
 */
public interface IoCContainerHolder {
    /**
     * Get current container.
     *
     * @return the current container
     * @throws IllegalStateException if no current container is set
     */
    @NotNull
    IoCContainer get();

    /**
     * Set current container.
     *
     * @param container the container or {@code null} to clear container reference
     */
    void set(@Nullable IoCContainer container);
}
