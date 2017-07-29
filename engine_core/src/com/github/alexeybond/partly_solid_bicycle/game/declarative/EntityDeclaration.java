package com.github.alexeybond.partly_solid_bicycle.game.declarative;

import com.github.alexeybond.partly_solid_bicycle.game.declarative.visitor.EntityDeclarationVisitor;

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

    public void visit(EntityDeclarationVisitor visitor) {
        visitor.beginVisitDeclaration(this);
        visitor.visitInheritedClasses(inherit);

        for (Map.Entry<String, ComponentDeclaration> e : components.entrySet())
            visitor.visitComponent(e.getKey(), e.getValue());

        for (Map.Entry<String, String[]> e : properties.entrySet())
            visitor.visitProperty(e.getKey(), e.getValue());

        visitor.endVisitDeclaration(this);
    }
}
