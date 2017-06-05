package com.github.alexeybond.gdx_commons.game.systems.timing;

import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.util.event.Event;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.Events;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;

/**
 *
 */
public class TimingSystem implements GameSystem {
    private Events<TimingSystem> events = new Events<TimingSystem>();
    private FloatProperty<TimingSystem> timeProp
            = events.event("time", FloatProperty.<TimingSystem>make(0));
    private FloatProperty<TimingSystem> deltaTimeProp
            = events.event("deltaTime", FloatProperty.<TimingSystem>make(0));
    private FloatProperty<TimingSystem> timeScaleProp
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
        deltaTimeProp.set(this, virtualDelta);
        timeProp.set(this, timeProp.get() + virtualDelta);
    }

    private static class ScheduleListener<T extends Event<TimingSystem>>
            implements EventListener<TimingSystem, FloatProperty<TimingSystem>> {
        private int subId;
        private final float time;
        private final T slaveEvent;

        ScheduleListener(
                float time,
                FloatProperty<TimingSystem> timeEvent,
                T slaveEvent) {
            this.time = time;
            this.slaveEvent = slaveEvent;
            this.subId = timeEvent.subscribe(this);
        }

        @Override
        public boolean onTriggered(TimingSystem timingSystem, FloatProperty<TimingSystem> event) {
            if (event.get() >= time) {
                subId = event.unsubscribe(subId);
                return slaveEvent.trigger(timingSystem);
            }

            return false;
        }
    }

    public <T extends Event<TimingSystem>> T scheduleAt(float time, T event) {
        new ScheduleListener<T>(time, timeProp, event);
        return event;
    }

    public Event<TimingSystem> scheduleAt(float time) {
        return scheduleAt(time, new Event<TimingSystem>());
    }
}
