package io.github.alexeybond.partly_solid_bicycle.core.impl.events.sources;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventListener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.EventSource;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.events.exceptions.NoEventOriginException;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public class DefaultEventSourceTest {
    private static EventSource ORIGIN = mock(EventSource.class);
    private static class Source extends DefaultEventSource<EventSource> {
        private Source(int capacity) {
            super(capacity);
        }

        @NotNull
        @Override
        public EventSource origin() throws NoEventOriginException {
            return ORIGIN;
        }

        protected void trigger(EventSource origin, Object initiator) {
            super.trigger(origin, initiator);
        }
    }

    private EventListener[] l;
    private EventSource origin;
    private Object initiator;

    @Before public void setUp() {
        l = new EventListener[16];

        for (int i = 0; i < l.length; i++) {
            l[i] = mock(EventListener.class);
        }

        origin = mock(EventSource.class);
        initiator = new Object();
    }

    @Test public void testOneListener() {
        Source source = new Source(1);

        int sub = source.subscribe(l[0]);

        assertTrue(sub >= 0);

        source.trigger(origin, initiator);

        verify(l[0], times(1))
                .onEvent(same(origin), same(initiator));
        reset(l[0]);

        source.unsubscribe(sub, l[1]);

        source.trigger(origin, initiator);

        verifyZeroInteractions(l[1]);
        verify(l[0], times(1))
                .onEvent(same(origin), same(initiator));
        reset(l[0]);

        sub = source.unsubscribe(sub, l[0]);

        source.trigger(origin, initiator);

        verifyZeroInteractions(l[0]);
        assertTrue(sub < 0);
    }

    @Test public void testMultipleListeners() {
        Source source = new Source(1);

        int[] sub = new int[l.length];

        for (int i = 0; i < l.length; i++) {
            sub[i] = source.subscribe(l[i]);
        }

        source.trigger(origin, initiator);

        for (int i = 0; i < l.length; i++) {
            verify(l[i], times(1))
                    .onEvent(same(origin), same(initiator));
            reset(l[i]);
        }

        source.unsubscribe(sub[7], l[7]);

        source.trigger(origin, initiator);

        for (int i = 0; i < l.length; i++) {
            verify(l[i], times(i == 7 ? 0 : 1))
                    .onEvent(same(origin), same(initiator));
            reset(l[i]);
        }
    }
}
