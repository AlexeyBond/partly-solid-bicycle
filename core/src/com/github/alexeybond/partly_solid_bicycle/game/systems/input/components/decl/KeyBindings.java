package com.github.alexeybond.partly_solid_bicycle.game.systems.input.components.decl;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.input.components.KeyBindingsComponent;

import java.util.HashMap;

/**
 *
 */
public class KeyBindings implements ComponentDeclaration {
    /** property_name -> key_name */
    public HashMap<String, String> bind = new HashMap<String, String>();

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new KeyBindingsComponent(bind);
    }
}
