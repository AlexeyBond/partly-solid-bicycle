package main.java.com.github.alexeybond.partly_solid_bicycle.ext.controllers;

import com.badlogic.gdx.controllers.Controller;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.timing.TimingSystem;
import com.github.alexeybond.partly_solid_bicycle.util.event.Event;
import com.github.alexeybond.partly_solid_bicycle.util.event.helpers.Subscription;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.FloatProperty;

public class ControllerAxisAdaptiveInput implements Component {
    private final String outputName, controllerName, resetEventName;
    private final int axisCode;

    private float center, min, max;

    private final Subscription<FloatProperty> dtSub = new Subscription<FloatProperty>() {
        @Override
        public boolean onTriggered(FloatProperty event) {
            float currentState = getCurrent();

            min = Math.min(min, currentState);
            max = Math.max(max, currentState);

            float result = currentState > center ?
                    (currentState - center) / (max - center) :
                    (currentState - center) / (center - min);

            output.set(result);

            return true;
        }
    };

    private final Subscription<Event> resetSub = new Subscription<Event>() {
        @Override
        public boolean onTriggered(Event event) {
            reset(lastController);

            return true;
        }
    };

    private ControllerSystem system;
    private FloatProperty output;
    private Controller lastController;

    private final float startDelta;

    private void reset(final Controller controller) {
        center = controller.getAxis(axisCode);
        min = center - startDelta;
        max = center + startDelta;

        lastController = controller;
    }

    private float getCurrent() {
        Controller controller = system.getController();

        if (controller != lastController) reset(controller);

        return controller.getAxis(axisCode);
    }

    private ControllerAxisAdaptiveInput(
            String outputName,
            String controllerName,
            String resetEventName, int axisCode,
            float startDelta
    ) {
        this.outputName = outputName;
        this.controllerName = controllerName;
        this.resetEventName = resetEventName;
        this.axisCode = axisCode;
        this.startDelta = startDelta;
    }


    @Override
    public void onConnect(Entity entity) {
        dtSub.set(entity
                .game()
                .systems()
                .<TimingSystem>get("timing")
                .events()
                .<FloatProperty>event("deltaTime"));
        resetSub.set(entity.events().event(resetEventName, Event.makeEvent()));
        system = entity.game().systems().get(controllerName);
        output = entity.events().event(outputName, FloatProperty.make());
    }

    @Override
    public void onDisconnect(Entity entity) {
        dtSub.clear();
    }

    public static class Decl implements ComponentDeclaration {
        public String output = "axis";
        public String controller = "controller";
        public int axis = 0;
        public float startDelta = 0.1f;
        public String resetEvent = "resetAdaptiveAxes";

        @Override
        public Component create(GameDeclaration gameDeclaration, Game game) {
            return new ControllerAxisAdaptiveInput(output, controller, resetEvent, axis, startDelta);
        }
    }
}
