package com.github.alexeybond.gdx_commons.game.declarative;

import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.util.event.props.Property;

import java.util.*;

/**
 * Data object describing a game world as a set of entities.
 */
public class GameDeclaration {
    /**
     * List of game declaration files that should be included in this one.
     */
    public String[] include = new String[0];

    public transient GameDeclaration[] included = new GameDeclaration[0];

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

    private EntityDeclaration getEntityClass0(String className) {
        EntityDeclaration declaration = classes.get(className);

        for (int i = 0; i < included.length && null == declaration; i++) {
            declaration = included[i].getEntityClass(className);
        }

        return declaration;
    }

    public EntityDeclaration getEntityClass(String className) {
        EntityDeclaration declaration = getEntityClass0(className);

        if (null == declaration)
            throw new NoSuchElementException("No such entity class: \"" + className + "\"");

        return declaration;
    }

    /**
     * Fill a game world with entities described by this declaration.
     */
    public Game apply(Game game) {
        for (GameDeclaration includedDeclaration : included)
            game = includedDeclaration.apply(game);

        for (EntityDeclaration entityDeclaration : entities)
            entityDeclaration.apply(new Entity(game), this);

        for (Map.Entry<String, String[]> entry : properties.entrySet())
            game.events().<Property<GameSystem>>event(entry.getKey())
                    .load(null, entry.getValue());

        return game;
    }

    /**
     * Convert this declaration from declaration with includes to a "flat" declaration.
     *
     * This method assumes that included declarations (if any) are loaded.
     *
     * If there is no declarations included then {@code this} is returned.
     */
    public GameDeclaration flatten() {
        if (include.length == 0) return this;

        GameDeclaration result = new GameDeclaration();

        ArrayList<EntityDeclaration> allEntities = new ArrayList<EntityDeclaration>();

        for (GameDeclaration includedDeclaration : included) {
            GameDeclaration d = includedDeclaration.flatten();
            result.classes.putAll(d.classes);
            result.properties.putAll(d.properties);
            allEntities.addAll(Arrays.asList(d.entities));
        }

        result.classes.putAll(this.classes);
        result.properties.putAll(this.properties);
        allEntities.addAll(Arrays.asList(entities));

        result.entities = allEntities.toArray(new EntityDeclaration[allEntities.size()]);

        return result;
    }
}
