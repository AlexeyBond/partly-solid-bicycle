package io.github.alexeybond.partly_solid_bicycle.core.impl.app.module;

import io.github.alexeybond.partly_solid_bicycle.core.impl.util.ExceptionAccumulator;
import io.github.alexeybond.partly_solid_bicycle.core.impl.util.TopologicalSort;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.Module;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.*;

public class ModuleSet implements Closeable {
    private ArrayList<Module> shutdownQueue;

    private <T> T iterableItem(Iterable<T> iterable, int index, T def) {
        Iterator<T> iter = iterable.iterator();
        T cur = def;

        for (int i = -1; i < index; ++i) {
            if (!iter.hasNext()) return def;
            cur = iter.next();
        }

        return cur;
    }

    private Iterable<String> moduleProvided(Module module) {
        return iterableItem(module.dependencyInfo(), 0, Collections.<String>emptyList());
    }

    private Iterable<String> moduleDependencies(Module module) {
        return iterableItem(module.dependencyInfo(), 1, Collections.<String>emptyList());
    }

    private Iterable<String> moduleReverseDependencies(Module module) {
        return iterableItem(module.dependencyInfo(), 2, Collections.<String>emptyList());
    }

    private @NotNull
    Collection<Module> sort(final @NotNull Collection<Module> modules) {
        Map<String, Module> providers = new HashMap<String, Module>();

        Throwable err = ExceptionAccumulator.init();

        for (Module module : modules) {
            for (String provided : moduleProvided(module)) {
                Module old = providers.put(provided, module);

                if (old == module) {
                    System.err.println("Invalid dependency info in module '" +
                            module.toString() +
                            "' provided dependency '" +
                            provided + "' mentioned multiple times.");
                } else if (null != old) {
                    String msg = "Modules '" +
                            module.toString() +
                            "' and '" +
                            old.toString() +
                            "' provide the same dependency '" +
                            provided + "'";
                    err = ExceptionAccumulator.add(err, new IllegalStateException(msg));
                }
            }
        }

        TopologicalSort<Module, Module> topologicalSort = new TopologicalSort<Module, Module>();

        for (Module module : modules) {
            topologicalSort.node(module, module);
        }

        for (Module module : modules) {
            for (String dependency : moduleDependencies(module)) {
                Module provider = providers.get(dependency);

                if (null == provider) {
                    String msg = "Module '" +
                            module.toString() +
                            "' has unsatisfied dependency '" +
                            dependency + "'";

                    err = ExceptionAccumulator.add(err, new IllegalStateException(msg));
                    continue;
                }

                topologicalSort.edge(provider, module);
            }
        }

        Collection<Module> sorted = null;

        try {
            sorted = topologicalSort.ordered(new ArrayList<Module>(modules.size()));
        } catch (IllegalStateException e) {
            err = ExceptionAccumulator.add(err,
                    new IllegalStateException("Module dependency graph has loops.", e));
        }

        ExceptionAccumulator.<RuntimeException>flush(err);

        return sorted;
    }

    private void start(final @NotNull Collection<Module> modules, @NotNull Collection<Object> env) {
        for (Module module : modules) {
            module.init(env);
            shutdownQueue.add(module);
        }
    }

    private void shutdown() {
        Throwable acc = ExceptionAccumulator.init();

        for (int i = shutdownQueue.size() - 1; i >= 0; --i) {
            try {
                shutdownQueue.get(i).shutdown();
            } catch (Exception e) {
                acc = ExceptionAccumulator.add(acc, e);
            }
        }

        shutdownQueue.clear();
        ExceptionAccumulator.<RuntimeException>flush(acc);
    }

    public ModuleSet(@NotNull Collection<Module> modules, @NotNull Collection<Object> env) {
        this.shutdownQueue = new ArrayList<Module>(modules.size());

        try {
            start(sort(modules), env);
        } catch (Exception e) {
            Throwable acc = ExceptionAccumulator.add(ExceptionAccumulator.init(), e);
            try {
                shutdown();
            } catch (Exception ee) {
                acc = ExceptionAccumulator.add(acc, ee);
            }

            ExceptionAccumulator.<RuntimeException>flush(acc);
        }
    }

    @Override
    public void close() {
        shutdown();
    }
}
