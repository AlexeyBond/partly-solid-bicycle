package io.github.alexeybond.partly_solid_bicycle.core.events;

public interface EventSource {
    int subscribe(EventListener<? extends EventSource> listener);

    int unsubscribe(int subId, EventListener<? extends EventSource> listener);
}
