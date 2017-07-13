package com.github.alexeybond.gdx_commons.game.systems.timing;

import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.Events;
import com.github.alexeybond.gdx_commons.util.event.EventsOwner;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;

/**
 *
 */
public class TimingSystem implements GameSystem, EventsOwner {
    private Events events = new Events();
    private FloatProperty timeProp
            = events.event("time", FloatProperty.<TimingSystem>make(0));
    private FloatProperty deltaTimeProp
            = events.event("deltaTime", FloatProperty.<TimingSystem>make(0));
    private FloatProperty timeScaleProp
            = events.event("timeScale", FloatProperty.<TimingSystem>make(1));

    @Override
    public void onConnect(Game game) {
    }

    @Override
    public void onDisconnect(Game game) {
    }

    @Override
    public void update(float deltaTime) {
        float virtualDelta = deltaTime * timeScaleProp.get();
        deltaTimeProp.set(virtualDelta);
        timeProp.set(timeProp.get() + virtualDelta);
    }

    @Override
    public Events events() {
        return events;
    }

    private static class ScheduleListener<T extends Event>
            implements EventListener<FloatProperty> {
        private int subId;
        private final float time;
        private final T slaveEvent;

        ScheduleListener(
                float time,
                FloatProperty timeEvent,
                T slaveEvent) {
            this.time = time;
            this.slaveEvent = slaveEvent;
            this.subId = timeEvent.subscribe(this);
        }

        @Override
        public boolean onTriggered(FloatProperty event) {
            if (event.get() >= time) {
                subId = event.unsubscribe(subId);
                return slaveEvent.trigger();
            }

            return false;
        }
    }

    public <T extends Event> T scheduleAt(float time, T event) {
        new ScheduleListener<T>(time, timeProp, event);
        return event;
    }

    public Event scheduleAt(float time) {
        return scheduleAt(time, new Event());
    }
}
