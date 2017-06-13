package com.github.alexeybond.gdx_commons.resource_management;

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

    }
}
