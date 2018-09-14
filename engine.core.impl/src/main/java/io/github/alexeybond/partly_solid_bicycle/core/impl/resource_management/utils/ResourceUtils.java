package io.github.alexeybond.partly_solid_bicycle.core.impl.resource_management.utils;

import com.badlogic.gdx.Gdx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public enum ResourceUtils {
    ;

    @NotNull
    public static String getFilePathForResourceId(@NotNull String resourceId) {
        return resourceId.replace('.', File.separatorChar);
    }

    @Nullable
    public static String getExistFilePath(
            @NotNull String path,
            @NotNull String[] extensions
    ) {
        for (String extension : extensions) {
            String fullPath = path + extension;

            if (Gdx.files.local(fullPath).exists()) {
                return fullPath;
            }
        }

        return null;
    }
}
