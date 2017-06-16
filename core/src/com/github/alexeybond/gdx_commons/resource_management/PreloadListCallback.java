package com.github.alexeybond.gdx_commons.resource_management;

import com.badlogic.gdx.assets.AssetManager;

/**
 *
 */
public interface PreloadListCallback {
    void execute(PreloadList list, AssetManager assetManager);
}
