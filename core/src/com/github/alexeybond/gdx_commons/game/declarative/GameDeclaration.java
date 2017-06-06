package com.github.alexeybond.gdx_commons.game.declarative;

import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.util.event.props.Property;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data object describing a game world as a set of entities.
 */
public class GameDeclaration {
    /**
     * Reusable entity declarations (classes).
     *
     * Entity classes may inherit other classes.
     */
    public HashMap<String, EntityDeclaration> classes
            = new HashMap<String, EntityDeclaration>();

    /**
     * The actual entities to create.
     */
    public EntityDeclaration[] entities = new EntityDeclaration[0];

    /**
     * Game properties.
     *
     * Property objects should be created by one of entities or subsystems.
     */
    public LinkedHashMap<String, String[]> properties
            = new LinkedHashMap<String, String[]>();

    /**
     * Fill a game world with entities described by this declaration.
     */
    public Game apply(Game game) {
        for (EntityDeclaration entityDeclaration : entities)
            entityDeclaration.apply(new Entity(game), classes);

        for (Map.Entry<String, String[]> entry : properties.entrySet())
            game.events().<Property<GameSystem>>event(entry.getKey())
                    .load(null, entry.getValue());

        return game;
    }
}
