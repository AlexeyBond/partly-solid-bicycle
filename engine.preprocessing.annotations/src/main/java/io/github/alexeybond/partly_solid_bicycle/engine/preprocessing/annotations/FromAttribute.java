package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

/**
 * Makes connector companion assign a node at path defined by value of an attribute
 * (or component attached to that node) to the property.
 *
 * <p>
 * Example:
 * </p>
 * <pre>
 * {@literal @}Component(/* ...{@literal *}/)
 *  class MyComponent {
 *      // Creates a serializable property of type {@link
 *      io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath
 *      LogicNodePath} with serialized name "target" and
 *      // assigns this property to node found at that path
 *     {@literal @}FromAttribute
 *      public LogicNode target;
 *
 *      // The same, but with custom serialized name (that defines a name of a property that
 *      // contains path to the place where my mind is).
 *     {@literal @}FromAttribute("where")
 *      public LogicNode mind;
 *
 *     {@literal @}FromAttribute(defaultPath="/null")
 *      public LogicNode node1;
 *  }
 * </pre>
 */
public @interface FromAttribute {
    /**
     * @return name of the attribute containing path to the node; empty string means attribute name matching
     * property name
     */
    String value() default "";

    /**
     * @return default path; empty string means that attribute has no default value and is required
     */
    String defaultPath() default "";

    Meta asMeta() default @Meta({
            "property.bind=true",
            "property.bindMode=attribute",
            "property.bindAttribute=$value",
            "property.bindPathDefault=$defaultPath"
    });
}
