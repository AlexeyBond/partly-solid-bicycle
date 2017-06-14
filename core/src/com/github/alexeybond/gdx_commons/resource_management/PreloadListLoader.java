package com.github.alexeybond.gdx_commons.resource_management;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class PreloadListLoader
        extends SynchronousAssetLoader<PreloadList, AssetLoaderParameters<PreloadList>> {
    private static Json json = new Json();

    private final static class ClassSettings<T> {
        final Class<T> clazz;
        final AssetLoaderParameters<T> parameters;

        private ClassSettings(Class<T> clazz, AssetLoaderParameters<T> parameters) {
            this.clazz = clazz;
            this.parameters = parameters;
        }
    }

    private final HashMap<String, ClassSettings> classes = new HashMap<String, ClassSettings>();

    private final AssetManager assetManager;
    private final ListUnloadCallback unloadCallback;

    private PreloadList preloadList;

    public PreloadListLoader(FileHandleResolver resolver, AssetManager assetManager, ListUnloadCallback unloadCallback) {
        super(resolver);
        this.assetManager = assetManager;
        this.unloadCallback = unloadCallback;
    }

    private PreloadList doLoad(FileHandle fileHandle) {
        if (null != preloadList) return preloadList;

        preloadList = json.fromJson(PreloadList.class, fileHandle);

        preloadList.onUnload = unloadCallback;
        preloadList.assetManager = assetManager;

        return preloadList;
    }

    @Override
    public PreloadList load(
            AssetManager assetManager,
            String fileName,
            FileHandle file,
            AssetLoaderParameters<PreloadList> parameter) {
        return doLoad(file);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(
            String fileName,
            FileHandle file,
            AssetLoaderParameters<PreloadList> parameter) {
        Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
        PreloadList preloadList = doLoad(file);

        for (Map.Entry<String, String[]> entry : preloadList.preload.entrySet()) {
            ClassSettings<?> settings = classes.get(entry.getKey());

            if (null == settings) {
                throw new RuntimeException("Unknown preload resource type: \"" + entry.getKey() + "\"");
            }

            for (String assetFile : entry.getValue()) {
                dependencies.add(new AssetDescriptor(assetFile, settings.clazz, settings.parameters));
            }
        }

        return dependencies;
    }

    public <T> void registerClass(String name, Class<T> clazz, AssetLoaderParameters<T> parameters) {
        classes.put(name, new ClassSettings<T>(clazz, parameters));
    }

    public <T> void registerClass(String name, Class<T> clazz) {
        registerClass(name, clazz, null);
    }
}
