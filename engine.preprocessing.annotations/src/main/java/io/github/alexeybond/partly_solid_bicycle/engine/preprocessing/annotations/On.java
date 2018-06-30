package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;


/**
 * <pre>
 * {@literal @}Component(/* ...{@literal *}/)
 * class Component {
 *    {@literal @}On("!connect")
 *     public void onAttached(LogicNode node) {
 *         // ...
 *     }
 *
 *    {@literal @}On("!disconnect")
 *     public void onDetached(LogicNode node) {
 *         // ...
 *     }
 *
 *    {@literal @}On("../events/hit")
 *     public void onHit(HitInfo origin) {
 *         // ...
 *     }
 *
 *    {@literal @}On("@event|../events/hit")
 *     public void onEvent() {
 *         // ...
 *     }
 * }
 * </pre>
 * <h2>@On with events</h2>
 * <p>
 * Method argument types:
 * <ul>
 * <li>No arguments - the method will be just called when any event happens on given path</li>
 * <li>One argument of any type - the event is casted to that type and passed as the argument when it happens</li>
 * </ul>
 * </p>
 * <p>
 * Path expressions:
 * <ul>
 * <li>
 * {@code "<path>"} - the method is subscribed to events on given path
 * </li>
 * <li>
 * {@code "@<property>"} - the path is being read from given property of component.
 * The property must have type
 * {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath LogicNodePath}.
 * If the property does not exist then an non-optional property with given name will be created.
 * </li>
 * <li>
 * {@code "@<property>|<path>"} - a optional property of type
 * {@link io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNodePath LogicNodePath}
 * will be created to be a source of path the method will be subscribed to.
 * The given path will be a default value for the created property.
 * </li>
 * </ul>
 * </p>
 */
public @interface On {
    String value();

    Meta asMeta() default @Meta({
            "method.subscribe=true",
            "method.subscribeExpression=$value"
    });
}
