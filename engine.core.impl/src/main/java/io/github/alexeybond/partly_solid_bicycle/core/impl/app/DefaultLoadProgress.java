package io.github.alexeybond.partly_solid_bicycle.core.impl.app;

import com.badlogic.gdx.assets.AssetManager;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.app.LoadProgress;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link LoadProgress} that uses underlying {@link AssetManager}.
 */
public class DefaultLoadProgress implements LoadProgress {
    @NotNull
    private final AssetManager assetManager;

    public DefaultLoadProgress(
            @NotNull AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override
    public boolean isCompleted() {
        return assetManager.getProgress() >= 1.0f;
    }

    @Override
    public float getProgress() {
        return assetManager.getProgress();
    }

    @Override
    public void run() {
        assetManager.update();
    }

    @Override
    public String getStatusMessage() {
        int done = assetManager.getLoadedAssets();
        int queued = assetManager.getQueuedAssets();

        return "[" + done + "/" + (done + queued) + "]Loading assets...";
    }
}
