package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;


/**
 * <pre>
 * {@literal @}Component(/* ...{@literal *}/)
 * class Component {
 *    {@literal @}On(phase="connect")
 *     public void onAttached(LogicNode node) {
 *         // ...
 *     }
 *
 *    {@literal @}On(phase="disconnect")
 *     public void onDetached(LogicNode node) {
 *         // ...
 *     }
 *
 *    {@literal @}On(event="../events/hit")
 *     public void onHit(HitInfo origin) {
 *         // ...
 *     }
 * }
 * </pre>
 */
public @interface On {
    String[] event() default "";

    String[] phase() default "";
}
