package io.github.alexeybond.partly_solid_bicycle.core.interfaces.events;

public interface EventTrigger {
    /**
     * Trigger event with default executor.
     *
     * @param initializer event initializer
     */
    void trigger(Object initializer);

    /**
     * Trigger event with given executor.
     *
     * @param initializer event initializer
     * @param executor    event executor
     */
    void trigger(Object initializer, EventExecutor executor);
}
