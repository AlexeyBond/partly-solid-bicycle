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
 */
public @interface On {
    String value();
}
