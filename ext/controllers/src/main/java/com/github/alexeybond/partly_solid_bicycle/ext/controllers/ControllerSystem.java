package main.java.com.github.alexeybond.partly_solid_bicycle.ext.controllers;

import com.badlogic.gdx.controllers.Controller;
import com.github.alexeybond.partly_solid_bicycle.game.GameSystem;

public interface ControllerSystem extends GameSystem {
    Controller getController();
}
