package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app;

public interface LoadProgress {
    boolean isCompleted();

    float getProgress();

    void run();

    String getStatusMessage();
}
