package com.github.alexeybond.gdx_commons.game.declarative;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.util.event.props.Property;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * <pre>{@code
 *      {
 *          "inherit": ["SomeClass"],
 *          "components": {
 *              "body": {
 *                  // ...
 *              },
 *              "sprite": {
 *                  // ...
 *              }
 *          },
 *          "properties": {
 *              "position": ["4.10", "4.20"],
 *              "rotation": ["42"]
 *          }
 *      }
 * }</pre>
 */
public class EntityDeclaration {
    public String[] inherit = new String[0];
    public LinkedHashMap<String, ComponentDeclaration> components
            = new LinkedHashMap<String, ComponentDeclaration>();
    public LinkedHashMap<String, String[]> properties
            = new LinkedHashMap<String, String[]>();

    /**
     * Create a entity described by this declaration.
     *
     * @throws NullPointerException if one of class names in {@link #inherit} is not valid (no entry with
     *          that name in {@code classes} map)
     * @throws java.util.NoSuchElementException if one of property names in {@link #properties} is invalid
     *          (no one of created components created property with that name)
     */
    public Entity apply(Entity entity, Map<String, EntityDeclaration> classes) {
        for (String anInherit : inherit)
            entity = classes.get(anInherit).apply(entity, classes);

        for (Map.Entry<String, ComponentDeclaration> entry : components.entrySet())
            entity.components().add(entry.getKey(), entry.getValue().create());

        for (Map.Entry<String, String[]> entry : properties.entrySet())
            entity.events().<Property<Component>>event(entry.getKey()).load(null, entry.getValue());

        return entity;
    }
}
