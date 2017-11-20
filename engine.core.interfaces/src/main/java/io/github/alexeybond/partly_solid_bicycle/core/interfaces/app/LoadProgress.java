package io.github.alexeybond.partly_solid_bicycle.core.interfaces.app;

/**
 * Object that provides methods to control resources loading process.
 */
public interface LoadProgress {
    /**
     * @return {@code true} if all resources are loaded
     */
    boolean isCompleted();

    /**
     * @return relation of amount of loaded resources to amount af all resources
     * that are being loaded (number in range [0.0, 1.0])
     */
    float getProgress();

    /**
     * Perform a step of resource loading.
     */
    void run();

    /**
     * @return verbose text message that represents load status
     */
    String getStatusMessage();
}
