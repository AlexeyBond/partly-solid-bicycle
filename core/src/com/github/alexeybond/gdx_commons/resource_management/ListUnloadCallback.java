package com.github.alexeybond.gdx_commons.resource_management;

import com.badlogic.gdx.assets.AssetManager;

/**
 *
 */
public interface ListUnloadCallback {
    void onUnload(PreloadList list, AssetManager assetManager);
}
