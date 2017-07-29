package com.github.alexeybond.partly_solid_bicycle.game.systems.input.components;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.util.event.Event;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventListener;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.BooleanProperty;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.ObjectProperty;
import com.github.alexeybond.partly_solid_bicycle.game.systems.input.InputSystem;

import java.util.Map;

/**
 * Component that forwards key states to boolean properties of entity.
 *
 * For each entry of map ("property" -> "KEY_NAME") component creates two entity properties:
 * <ul>
 *  <li>"property" - the boolean property that will be updated when key event occurs</li>
 *  <li>"propertyKey" - property containing name of key the first property is bound to. The binding
 *      will be updated when the property is updated</li>
 * </ul>
 *
 * If there already is a property "propertyKey" then component will use it's value instead of value passed to
 * constructor.
 */
public class KeyBindingsComponent implements Component {
    private class Binding implements EventListener {
        private String keyName, propertyName;
        private BooleanProperty keyEvent;
        private BooleanProperty entityEvent;
        private ObjectProperty<String> bindingProperty;
        private int keySubIdx, bindSubIdx;

        Binding(String keyName, String propertyName) {
            this.keyName = keyName;
            this.propertyName = propertyName;
        }

        /** Initialize binding on entity */
        void bind(Entity entity) {
            entityEvent = entity.events().event(
                    propertyName, BooleanProperty.make());
            bindingProperty = entity.events().event(
                    propertyName + "Key", ObjectProperty.<String>make(keyName));
            bindSubIdx = bindingProperty.subscribe(this);

            // If there already was a binding property - use it's original value
            bind0(inputSystem.input().keyEvent(bindingProperty.get()));
        }

        /** Destroy the binding completely */
        void unbind() {
            unbind0();
            bindSubIdx = bindingProperty.unsubscribe(bindSubIdx);
        }

        /** Unsubscribe from key event */
        private void unbind0() {
            keySubIdx = keyEvent.unsubscribe(keySubIdx);
        }

        /** Subscribe to key event */
        private void bind0(BooleanProperty newKeyEvent) {
            keyEvent = newKeyEvent;
            keySubIdx = keyEvent.subscribe(this);
            entityEvent.set(keyEvent.get());
        }

        private void rebind(String keyName) {
            BooleanProperty newKeyEvent = inputSystem.input().keyEvent(keyName);
            unbind0();
            bind0(newKeyEvent);
            this.keyName = keyName;
        }

        @Override
        public boolean onTriggered(Event event) {
            if (event == keyEvent) {
                return entityEvent.set(keyEvent.get());
            } else if (event == bindingProperty) {
                rebind(bindingProperty.get());
                return true;
            }

            return false;
        }
    }

    private final Binding[] bindings;
    private InputSystem inputSystem;

    /**
     * @param bindings    map from property name to key name
     */
    public KeyBindingsComponent(Map<String, String> bindings) {
        this.bindings = new Binding[bindings.size()];

        int i = 0;

        for (Map.Entry<String, String> entry : bindings.entrySet()) {
            this.bindings[i++] = new Binding(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public void onConnect(Entity entity) {
        inputSystem = entity.game().systems().get("input");

        for (int i = 0; i < bindings.length; i++) {
            bindings[i].bind(entity);
        }
    }

    @Override
    public void onDisconnect(Entity entity) {
        for (int i = 0; i < bindings.length; i++) {
            bindings[i].unbind();
        }
    }
}
