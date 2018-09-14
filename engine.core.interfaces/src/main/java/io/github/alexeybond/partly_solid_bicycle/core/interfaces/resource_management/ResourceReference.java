package io.github.alexeybond.partly_solid_bicycle.core.interfaces.resource_management;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.StateTopic;
import org.jetbrains.annotations.NotNull;

/**
 * Reference to a resource (such as texture, sound, etc.).
 * <p>
 * {@link ResourceReference} implements {@link StateTopic} so it can notify interested
 * object when resource changes (this is mostly useful in development environment as
 * allows to update some resources on-the-fly).
 * </p>
 *
 * @param <TResource>
 */
public interface ResourceReference<TResource> extends StateTopic<TResource> {
    /**
     * Returns current resource instance.
     * <p>
     * Will load resource synchronously if it is not loaded yet.
     * </p>
     * <p>
     * This method is necessary to simplify and optimize resource access in cases where
     * resource is not going to change.
     * </p>
     *
     * @return the resource instance
     */
    @NotNull
    TResource get();

    /**
     * Pass current resource instance to given listener.
     * <p>
     * May load resource synchronously or pass some placeholder resource if the resource is not loaded yet.
     * </p>
     * <p>
     * However invariant is that if client
     * <ol>
     * <li>Calls {@link #pull(Listener) pull}(listener)</li>
     * <li>Then calls {@link #subscribe(Listener) subscribe}(listener)</li>
     * </ol>
     * then listener will be notified when the resource is loaded unless
     * {@link #unsubscribe(Object, Listener) unsubscribe}(token, listener) is called before resource load
     * is completed.
     * </p>
     *
     * @param listener the listener to notify
     */
    @Override
    void pull(@NotNull Listener<? super TResource> listener);

    /**
     * Disposes resource instance associated with this reference.
     * <p>
     * Interaction with {@link ResourceReference} after {@code #dispose()} call will cause
     * undefined behavior.
     * </p>
     */
    void dispose();
}
