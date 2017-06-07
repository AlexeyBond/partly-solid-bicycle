package com.github.alexeybond.gdx_commons.game.systems.input.components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.input.components.KeyBindingsComponent;

import java.util.HashMap;

/**
 *
 */
public class KeyBindings implements ComponentDeclaration {
    /** property_name -> key_name */
    public HashMap<String, String> bind = new HashMap<String, String>();

    @Override
    public Component create() {
        return new KeyBindingsComponent(bind);
    }
}
