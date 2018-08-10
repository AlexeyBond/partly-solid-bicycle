package main.java.com.github.alexeybond.partly_solid_bicycle.ext.controllers;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import com.github.alexeybond.partly_solid_bicycle.game.Game;

/**
 * {@link ControllerSystem} implementation that always uses first available controller.
 */
public class AnyControllerSystem implements ControllerSystem {
    private Controller currentController;

    private ControllerListener controllerListener = new ControllerAdapter() {
        @Override
        public void disconnected(Controller controller) {
            if (controller == currentController) {
                lookupController();
            }
        }

        @Override
        public void connected(Controller controller) {
            if (NullController.INSTANCE == currentController) {
                lookupController();
            }
        }
    };

    private void lookupController() {
        Array<Controller> controllers = Controllers.getControllers();

        if (controllers.size != 0) {
            currentController = controllers.get(0);
        } else {
            currentController = NullController.INSTANCE;
        }
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void onConnect(Game game) {
        lookupController();
        Controllers.addListener(controllerListener);
    }

    @Override
    public void onDisconnect(Game game) {
        Controllers.removeListener(controllerListener);
    }

    @Override
    public Controller getController() {
        return currentController;
    }
}
