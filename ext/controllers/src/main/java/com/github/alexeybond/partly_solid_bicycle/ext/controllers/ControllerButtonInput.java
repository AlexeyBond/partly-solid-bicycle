package main.java.com.github.alexeybond.partly_solid_bicycle.ext.controllers;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.timing.TimingSystem;
import com.github.alexeybond.partly_solid_bicycle.util.event.helpers.Subscription;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.BooleanProperty;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.FloatProperty;

public class ControllerButtonInput implements Component {
    private final String outputName, controllerName;
    private final int buttonCode;

    private final Subscription<FloatProperty> dtSub = new Subscription<FloatProperty>() {
        @Override
        public boolean onTriggered(FloatProperty event) {
            output.set(system.getController().getButton(buttonCode));

            return true;
        }
    };

    private ControllerSystem system;
    private BooleanProperty output;

    private ControllerButtonInput(String outputName, String controllerName, int buttonCode) {
        this.outputName = outputName;
        this.controllerName = controllerName;
        this.buttonCode = buttonCode;
    }

    @Override
    public void onConnect(Entity entity) {
        dtSub.set(entity
                .game()
                .systems()
                .<TimingSystem>get("timing")
                .events()
                .<FloatProperty>event("deltaTime"));
        system = entity.game().systems().get(controllerName);
        output = entity.events().event(outputName, BooleanProperty.make());
    }

    @Override
    public void onDisconnect(Entity entity) {
        dtSub.clear();
    }

    public static class Decl implements ComponentDeclaration {
        public String output = "button";
        public String controller = "controller";
        public int button = 0;

        @Override
        public Component create(GameDeclaration gameDeclaration, Game game) {
            return new ControllerButtonInput(
                    output,
                    controller,
                    button
            );
        }
    }
}
