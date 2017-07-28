package com.github.alexeybond.partly_solid_bicycle.resource_management;

import com.badlogic.gdx.assets.AssetManager;

/**
 *
 */
public interface PreloadListCallback {
    void execute(PreloadList list, AssetManager assetManager);
}
