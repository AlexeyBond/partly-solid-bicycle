package io.github.alexeybond.partly_solid_bicycle.core.impl.event.notifier;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@SuppressWarnings({"unchecked"})
public class LinkedNotifierTest {
    private Object evt1 = new Object(), evt2 = new Object();
    private Listener listener1, listener2;
    private Topic topic;

    @Before
    public void setUp() {
        listener1 = mock(Listener.class);
        listener2 = mock(Listener.class);
        topic = mock(Topic.class);
    }

    @Test
    public void testNotifyListener() {
        LinkedNotifier notifier = new LinkedNotifier();

        notifier.subscribe(listener1);

        notifier.notifyListeners(evt1, topic);

        verify(listener1).receive(same(evt1), same(topic));
        verifyNoMoreInteractions(listener1);
    }

    @Test
    public void testUnsubscribeListener() {
        LinkedNotifier notifier = new LinkedNotifier();

        Object token = notifier.subscribe(listener1);
        notifier.unsubscribe(token, listener1);

        notifier.notifyListeners(evt1, topic);

        verify(listener1, times(0)).receive(any(), (Topic) any());
    }

    @Test
    public void testMultipleListeners() {
        LinkedNotifier notifier = new LinkedNotifier();

        notifier.subscribe(listener1);
        notifier.subscribe(listener2);

        notifier.notifyListeners(evt1, topic);
        notifier.notifyListeners(evt2, topic);

        verify(listener1, times(1)).receive(same(evt1), same(topic));
        verify(listener2, times(1)).receive(same(evt1), same(topic));
        verify(listener1, times(1)).receive(same(evt2), same(topic));
        verify(listener2, times(1)).receive(same(evt2), same(topic));

        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    @Test
    public void testExceptions() {
        LinkedNotifier notifier = new LinkedNotifier();

        notifier.subscribe(listener1);
        notifier.subscribe(listener2);

        doThrow(new RuntimeException()).when(listener1).receive(any(), (Topic) any());
        doThrow(new RuntimeException()).when(listener2).receive(any(), (Topic) any());

        try {
            notifier.notifyListeners(evt1, topic);
            fail();
        } catch (RuntimeException e) {
            // ok
        }

        verify(listener1).receive(same(evt1), same(topic));
        verify(listener2).receive(same(evt1), same(topic));
    }
}
