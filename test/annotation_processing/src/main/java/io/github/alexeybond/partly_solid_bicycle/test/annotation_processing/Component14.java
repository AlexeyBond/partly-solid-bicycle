package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.*;

@Component(name = "component-14", kind = "any")
public class Component14 {
    @SkipProperty
    public int aInvocations;

    @SkipProperty
    public int bInvocations;

    @SkipProperty
    public int cInvocations;

    @OnEventAtPath("../eventA")
    public void onEventA() {
        ++aInvocations;
    }

    @OnEventAtAttribute("eventB")
    public void onEventB(Object event) {
        ++bInvocations;
    }

    @FromPath("../eventC")
    public Topic<Integer> eventC;

    @OnEventAtProperty("eventC")
    public void onEventC(Integer event, Topic<Integer> topic) {
        if (topic != eventC) throw new AssertionError("Unexpected topic");

        ++cInvocations;
    }
}
