package io.github.alexeybond.partly_solid_bicycle.core.impl.events.listeners;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventListener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SubscriptionTest {
    private EventSource<EventSource> source;
    private EventListener<EventSource> listener;
    private Subscription<EventSource> subscription;

    @Before public void setUp() {
        source = mock(EventSource.class);
        listener = mock(EventListener.class);
        subscription = new Subscription<EventSource>() {
            @Override
            public void onEvent(@NotNull EventSource source, @Nullable Object initializer) {
                listener.onEvent(source, initializer);
            }
        };
    }

    @Test public void shouldSubscribeAndUnsubscribe() {
        doReturn(4325).when(source).subscribe(any(EventListener.class));

        subscription.subscribe(source);

        verify(source, times(1)).subscribe(same(subscription));
        verifyNoMoreInteractions(source);

        subscription.clear();
        subscription.clear();

        verify(source, times(1)).unsubscribe(eq(4325), same(subscription));
        verifyNoMoreInteractions(source);
    }

    @Test public void shouldInvokeMethodWhenEventOccurs() {
        Object init = new Object();

        subscription.onEvent(source, init);

        verify(listener).onEvent(same(source), same(init));

        verifyNoMoreInteractions(source, listener);
    }
}
