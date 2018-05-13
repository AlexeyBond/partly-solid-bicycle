package io.github.alexeybond.partly_solid_bicycle.core.impl.event.topic;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Channel;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Notifier;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import org.jetbrains.annotations.NotNull;

public class ImmediateTopic<T>
        extends BaseNotifierTopic<T>
        implements Topic<T>, Channel<T> {
    public ImmediateTopic(@NotNull Notifier<T> notifier) {
        super(notifier);
    }

    @Override
    public void send(@NotNull T event) {
        notifier.notifyListeners(event, this);
    }
}
