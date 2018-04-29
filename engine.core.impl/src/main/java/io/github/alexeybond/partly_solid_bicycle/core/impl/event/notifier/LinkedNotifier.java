package io.github.alexeybond.partly_solid_bicycle.core.impl.event.notifier;

import io.github.alexeybond.partly_solid_bicycle.core.impl.util.ExceptionAccumulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import org.jetbrains.annotations.NotNull;

public class LinkedNotifier<TEvent> implements Topic<TEvent> {
    private final class ListenerRef {
        Listener<? super TEvent> listener;

        ListenerRef next;

        private ListenerRef(Listener<? super TEvent> listener, ListenerRef next) {
            this.listener = listener;
            this.next = next;
        }
    }

    private final ListenerRef terminal = new ListenerRef(null, null);

    @Override
    public Object subscribe(@NotNull Listener<? super TEvent> listener) {
        ListenerRef ref = new ListenerRef(listener, terminal.next);

        terminal.next = ref;

        return ref;
    }

    @Override
    public void unsubscribe(Object token, @NotNull Listener<? super TEvent> listener) {
        ListenerRef ref = (ListenerRef) token;

        if (ref.listener == listener) {
            ref.listener = null;
        }
    }

    void notifyListeners(@NotNull TEvent event, @NotNull Topic<TEvent> topic) {
        ListenerRef ref = terminal.next, prev = terminal;

        Throwable acc = ExceptionAccumulator.init();

        while (null != ref) {
            if (null == ref.listener) {
                ListenerRef next = ref.next;
                prev.next = next;
                ref = next;
            } else {
                try {
                    ref.listener.receive(event, topic);
                } catch (Exception e) {
                    acc = ExceptionAccumulator.add(acc, e);
                }
                prev = ref;
                ref = ref.next;
            }
        }

        ExceptionAccumulator.<RuntimeException>flush(acc);
    }
}
