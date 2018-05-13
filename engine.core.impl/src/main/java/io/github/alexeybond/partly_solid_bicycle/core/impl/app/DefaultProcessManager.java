package io.github.alexeybond.partly_solid_bicycle.core.impl.app;

import io.github.alexeybond.partly_solid_bicycle.core.impl.util.TopologicalSort;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.systems.pm.ProcessManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of of {@link ProcessManager process manager}.
 *
 * <p>
 *  This implementation stores all processes and ordering hints to re-compute process order when process list
 *  changes.
 *  Order re-computation is relatively (relatively to execution of ordered processes which is just a iteration
 *  over a array) heavy operation but it's also not very frequent as process list is not meant to be changed
 *  frequently.
 * </p>
 */
public class DefaultProcessManager implements ProcessManager, Runnable {
    private Runnable[] orderedProcesses;
    private final Map<String, List<String>> hints = new HashMap<String, List<String>>();
    private final Map<String, Runnable> processes = new HashMap<String, Runnable>();

    private Runnable[] computeOrder() {
        TopologicalSort<String, Runnable> sort = new TopologicalSort<String, Runnable>();

        for (Map.Entry<String, Runnable> entry : processes.entrySet()) {
            sort.node(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, List<String>> entry : hints.entrySet()) {
            for (String hint : entry.getValue()) {
                sort.edge(entry.getKey(), hint);
            }
        }

        return sort
                .ordered(new ArrayList<Runnable>(processes.size()))
                .toArray(new Runnable[processes.size()]);
    }

    @Override
    public void orderHint(String first, String second) {
        List<String> l = hints.get(first);

        if (null == l) hints.put(first, l = new ArrayList<String>());

        l.add(second);

        orderedProcesses = null;
    }

    @Override
    public void addProcess(String name, Runnable runnable) {
        orderedProcesses = null;
        Runnable old = processes.put(name, runnable);

        if (null != old) {
            processes.put(name, old);
            throw new IllegalStateException("There already is a process named '" + name + "'.");
        }
    }

    @Override
    public void removeProcess(String name, Runnable runnable) {
        orderedProcesses = null;
        Runnable removed = processes.remove(name);

        if (runnable != removed) {
            processes.put(name, removed);
            throw new IllegalStateException("The runnable provided for process '" +
                    name + "' does not match the registered one.");
        }
    }

    @Override
    public void run() {
        Runnable[] processes = this.orderedProcesses;

        if (null == processes) {
            processes = this.orderedProcesses = computeOrder();
        }

        for (Runnable process : processes) {
            process.run();
        }
    }
}
