package com.github.alexeybond.partly_solid_bicycle.resource_management;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;

/**
 *
 */
public class PreloadList implements Disposable {
    /**
     * Map "preload strategy" -> ["resource name", ...].
     */
    public HashMap<String, String[]> preload = new HashMap<String, String[]>();



    @Override
    public void dispose() {
        if (null != onUnload) onUnload.execute(this, assetManager);
    }

    transient PreloadListCallback onUnload = null;
    transient AssetManager assetManager = null;
}
