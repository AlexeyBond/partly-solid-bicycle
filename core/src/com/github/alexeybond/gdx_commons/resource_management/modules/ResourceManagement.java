package com.github.alexeybond.gdx_commons.resource_management.modules;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.IoCStrategy;
import com.github.alexeybond.gdx_commons.ioc.Module;
import com.github.alexeybond.gdx_commons.ioc.strategy.Singleton;

/**
 *
 */
public class ResourceManagement implements Module {
    private Logger log = new Logger("ResourceManagement", Logger.DEBUG);

    private AssetManager assetManager;

    private <T> IoCStrategy assetLoadStrategy(final Class<T> type, final AssetLoaderParameters<T> params) {
        return new IoCStrategy() {
            @Override
            public Object resolve(Object... args) {
                String name = (String) args[0];
                try {
                    return assetManager.get(name, type);
                } catch (GdxRuntimeException e) {
                    log.info(
                            "Loading asset synchronously (please preload it in release version): " + name,
                            e);
                    assetManager.load(name, type, params);
                    assetManager.finishLoadingAsset(name);
                    return assetManager.get(name, type);
                }
            }
        };
    }

    private <T> IoCStrategy assetPreloadStrategy(final Class<T> type, final AssetLoaderParameters<T> params) {
        return new IoCStrategy() {
            @Override
            public Object resolve(Object... args) {
                assetManager.load((String) args[0], type, params);
                return null;
            }
        };
    }

    private <T> void registerAssetTypeStrategies(
            String name, final Class<T> type, final AssetLoaderParameters<T> params) {
        IoC.register("preload " + name, assetPreloadStrategy(type, params));
        IoC.register("load " + name, assetLoadStrategy(type, params));
    }

    @Override
    public void init() {
        assetManager = new AssetManager();

        IoC.register("asset manager", new Singleton(assetManager));

        registerAssetTypeStrategies("skin", Skin.class, null);
        registerAssetTypeStrategies("sound", Sound.class, null);
        registerAssetTypeStrategies("particles", ParticleEffect.class, null);
    }

    @Override
    public void shutdown() {
        assetManager.dispose();
    }
}
