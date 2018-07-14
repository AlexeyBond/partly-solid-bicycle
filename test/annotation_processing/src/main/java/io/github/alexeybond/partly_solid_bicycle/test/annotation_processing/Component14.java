package io.github.alexeybond.partly_solid_bicycle.test.annotation_processing;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.*;

@Component(name = "component-14", kind = "any")
public class Component14 {
    @OnEventAtPath("../eventA")
    public void onEventA() {

    }

    @OnEventAtAttribute("eventB")
    public void onEventB(Object event) {

    }

    @FromPath("../eventC")
    public Topic<Integer> eventC;

    @OnEventAtProperty("eventC")
    public void onEventC(Integer event, Topic<Integer> topic) {

    }
}
