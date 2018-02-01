package io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * Annotation for a {@link Module module} base class of which should be generated by annotation processor.
 * <p>
 * A module may be declared like this:
 * <pre>
 * {@link GeneratedModule @GeneratedModule}
 * public class MyModule extends MyModuleImpl {}
 * </pre>
 * <em>
 *     Note: class {@code MyModuleImpl} will be generated by annotation processor in the same package where
 *     {@code MyModule} is located.
 *     Annotation processor will fail if such class is already present in project sources or dependencies.
 * </em>
 * </p>
 * <p>
 * It's also possible to add custom behaviour to a generated module by overriding {@link Module#init(Collection)}
 * and {@link Module#shutdown()} methods (but do not forget to call superclass methods):
 * <pre>
 * {@link GeneratedModule @GeneratedModule}
 * public class MyModule extends MyModuleImpl implements {@link Module} {
 *     {@link Override @Override}
 *     public void init({@link Iterable}{@code <Object>} envs) {
 *         super.init(envs);
 *
 *         // ... custom code here ...
 *     }
 *
 *     {@link Override @Override}
 *     public void shutdown() {
 *         try {
 *             // ... custom code here ...
 *         } finally {
 *             super.shutdown();
 *         }
 *     }
 * }
 * </pre>
 * </p>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface GeneratedModule {
    /**
     * Set to {@code true} if this module should be used as default module.
     * <p>
     * Components annotated with {@link Component} annotation and with {@link Component#modules() module list}
     * kept empty will be registered by all default modules of the project.
     * Annotation processor will fail with error if there is no default modules in project
     * and there is a component that has a empty module list.
     * </p>
     * @return {@code true} iff the module should be used as default module for current project
     */
    boolean useAsDefault() default false;
}