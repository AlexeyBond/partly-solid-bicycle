package com.github.alexeybond.gdx_commons.game.declarative;

import com.github.alexeybond.gdx_commons.game.declarative.visitor.GameDeclarationVisitor;

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

    public void visit(GameDeclarationVisitor visitor) {
        visitor.beginVisitDeclaration(this);

        for (int i = 0; i < included.length; i++)
            visitor.visitIncludedDeclaration(include[i], included[i]);

        if (visitor.beginVisitClassDeclarations()) {
            for (Map.Entry<String, EntityDeclaration> e : classes.entrySet())
                visitor.visitClassDeclaration(e.getKey(), e.getValue());

            visitor.endVisitClassDeclarations();
        }

        if (visitor.beginVisitEntityDeclarations()) {
            for (int i = 0; i < entities.length; i++)
                visitor.visitEntityDeclaration(entities[i]);

            visitor.endVisitEntityDeclarations();
        }

        if (visitor.beginVisitProperties()) {
            for (Map.Entry<String, String[]> e : properties.entrySet())
                visitor.visitProperty(e.getKey(), e.getValue());

            visitor.endVisitProperties();
        }

        visitor.endVisitDeclaration(this);
    }

    private EntityDeclaration getEntityClass0(String className) {
        EntityDeclaration declaration = classes.get(className);

        for (int i = 0; i < included.length && null == declaration; i++) {
            declaration = included[i].getEntityClass0(className);
        }

        return declaration;
    }

    public EntityDeclaration getEntityClass(String className) {
        EntityDeclaration declaration = getEntityClass0(className);

        if (null == declaration)
            throw new NoSuchElementException("No such entity class: \"" + className + "\"");

        return declaration;
    }
}
