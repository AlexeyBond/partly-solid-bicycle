package io.github.alexeybond.partly_solid_bicycle.core.impl.event.topic;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.reference_counted.ReferenceCounted;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Notifier;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.RCEventChannel;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.StateTopic;
import org.jetbrains.annotations.NotNull;

public class LastRCStateTopic<T extends ReferenceCounted>
        extends BaseNotifierTopic<T>
        implements StateTopic<T>, RCEventChannel<T>, Runnable {
    private T current, next;
    private boolean notifying;

    public LastRCStateTopic(@NotNull Notifier<T> notifier) {
        super(notifier);
    }

    @Override
    public void send(@NotNull T event) {
        try {
            if (null != next) next.release();
        } finally {
            next = null;
        }

        next = event;
    }

    @Override
    public void pull(@NotNull Listener<? super T> listener) {
        T event = this.current;

        if (null == event) return; // TODO:: ???

        event.acquire();

        try {
            listener.receive(event, this);
        } finally {
            event.release();
        }
    }

    @Override
    public void run() {
        if (notifying) return;

        notifying = true;

        try {
            while (null != next) {
                try {
                    if (null != current)
                        current.release();
                } finally {
                    current = next;
                    next = null;
                }

                notifier.notifyListeners(current, this);
            }
        } finally {
            notifying = false;
        }
    }
}
