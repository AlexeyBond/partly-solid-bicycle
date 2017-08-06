package com.github.alexeybond.partly_solid_bicycle.application.impl.load;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.github.alexeybond.partly_solid_bicycle.application.LoadProgressManager;
import com.github.alexeybond.partly_solid_bicycle.application.LoadTask;

public class DefaultLoadProgressManager implements LoadProgressManager {
    private final static String DONE_MESSAGE = "Done";

    private final Array<LoadTask> repeatableTasks = new Array<LoadTask>();
    private final Queue<LoadTask> onceTasks = new Queue<LoadTask>();

    private LoadTask lastTask = null;

    @Override
    public boolean isCompleted() {
        if (onceTasks.size != 0)
            return false;

        for (int i = 0; i < repeatableTasks.size; i++) {
            if (!repeatableTasks.get(i).isDone())
                return false;
        }

        return true;
    }

    @Override
    public float getProgress() {
        float maxProgress = 0, doneProgress = 0;

        for (int i = 0; i < repeatableTasks.size; i++) {
            LoadTask task = repeatableTasks.get(i);

            maxProgress += task.getMaxProgress();
            doneProgress += task.getProgress();
        }

        for (int i = 0; i < onceTasks.size; i++) {
            LoadTask task = onceTasks.get(i);

            maxProgress += task.getMaxProgress();
            doneProgress += task.getProgress();
        }

        if (0 == maxProgress)
            return 1;

        return doneProgress / maxProgress;
    }

    @Override
    public void runNext() {
        for (int i = 0; i < repeatableTasks.size; i++) {
            LoadTask task = repeatableTasks.get(i);

            if (!task.isDone()) {
                task.run();
                lastTask = task;
                return;
            }
        }

        while (onceTasks.size != 0) {
            LoadTask task = onceTasks.removeFirst();

            if (task.isDone())
                continue;

            task.run();
            lastTask = task;

            if (!task.isDone())
                onceTasks.addFirst(task);

            return;
        }

        lastTask = null;
    }

    @Override
    public void addRepeatable(LoadTask task) {
        repeatableTasks.add(task);
    }

    @Override
    public void addOnce(LoadTask task) {
        onceTasks.addLast(task);
    }

    @Override
    public void remove(LoadTask task) {
        onceTasks.removeValue(task, true);
        repeatableTasks.removeValue(task, true);
    }

    @Override
    public String getMessage() {
        return (null == lastTask) ? DONE_MESSAGE : lastTask.getMessage();
    }
}
