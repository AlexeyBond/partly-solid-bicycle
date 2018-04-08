package io.github.alexeybond.partly_solid_bicycle.core.impl.app.module;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Base class for human-written {@link Module modules}.
 * <p>
 * Provides convenient methods for creation of dependency information.
 * <p>
 * Example:
 * <pre>
 *     class MyModule extends {@link BaseModule} {
 *         {
 *             provide("my_module_id");
 *             depend("ioc");
 *         }
 *
 *         public void init(Collection{@literal <}Object{@literal >} env) {
 *             // ... whatever ... //
 *         }
 *
 *         public void shutdown() {
 *             // ... whatever ... //
 *         }
 *     }
 * </pre>
 */
public abstract class BaseModule implements Module {
    private final ArrayList<Iterable<String>> dependencyInfo = new ArrayList<Iterable<String>>();
    private final ArrayList<String> provided = new ArrayList<String>();
    private final ArrayList<String> direct = new ArrayList<String>();
    private final ArrayList<String> reverse = new ArrayList<String>();

    {
        dependencyInfo.add(provided);
        dependencyInfo.add(direct);
        dependencyInfo.add(reverse);
    }

    protected void provide(@NotNull String dep) {
        provided.add(dep);
    }

    protected void depend(@NotNull String dep) {
        direct.add(dep);
    }

    protected void reverseDepend(@NotNull String dep) {
        reverse.add(dep);
    }

    @Override
    public final Iterable<Iterable<String>> dependencyInfo() {
        return dependencyInfo;
    }
}
