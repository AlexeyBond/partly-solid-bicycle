package io.github.alexeybond.partly_solid_bicycle.core.impl.app;

import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.ProcessManager;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Optional;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Process manager that is a {@link System system} in terms of ECS.
 */
@Component(name = "processManager", kind = "system")
public class DefaultProcessManagementSystem
        extends DefaultProcessManager {
    private ProcessManager master;

    @Optional
    public String masterId = "processManager";

    public String processName;

    /**
     * List of process order hints.
     * <p>
     * Each nested list represents a sequence of names of processes depending on each other.
     * For example in this case:
     * <pre>
     * "orderHints": [
     *  ["a", "b", "c"],
     *  ["b", "z"],
     *  ["x", "w"],
     * ]
     * </pre>
     * the following process dependencies are configured:
     * <ul>
     * <li>"b" depends on "a"</li>
     * <li>"c" depends on "b"</li>
     * <li>"z" depends on "b"</li>
     * <li>"w" depends on "x"</li>
     * </ul>
     * </p>
     */
    @Optional
    public List<List<String>> orderHints = Collections.emptyList();

    // ToDo: invoke from connector...
    public void onConnect(@NotNull LogicNode owner)
            throws Exception {
        for (List<String> hint : orderHints) {
            for (int i = 1; i < hint.size(); i++) {
                orderHint(hint.get(i - 1), hint.get(i));
            }
        }

        //master = null;
        master.addProcess(processName, this);
    }

    public void dispose()
            throws Exception {
        master.removeProcess(processName, this);
    }
}
