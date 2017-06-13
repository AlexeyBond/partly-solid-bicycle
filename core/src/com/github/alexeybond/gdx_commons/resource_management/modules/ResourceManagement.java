package com.github.alexeybond.gdx_commons.resource_management.modules;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.IoCStrategy;
import com.github.alexeybond.gdx_commons.ioc.modules.Module;
import com.github.alexeybond.gdx_commons.ioc.strategy.Singleton;
import com.github.alexeybond.gdx_commons.resource_management.PreloadList;
import com.github.alexeybond.gdx_commons.resource_management.PreloadListLoader;

/**
 *
 */
public class ResourceManagement implements Module {
    private static TextureLoader.TextureParameter loopTextureParameter
            = new TextureLoader.TextureParameter() {{
        wrapU = Texture.TextureWrap.Repeat;
        wrapV = Texture.TextureWrap.Repeat;
        minFilter = Texture.TextureFilter.Linear;
        magFilter = Texture.TextureFilter.Linear;
    }};

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
                    log.info("Loading asset synchronously (please preload it in release version): " + name);
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

    private <T> IoCStrategy assetUnloadStrategy(final Class<T> type) {
        return new IoCStrategy() {
            @Override
            public Object resolve(Object... args) {
                assetManager.unload((String) args[0]);
                return null;
            }
        };
    }

    private <T> void registerAssetTypeStrategies(
            String name, final Class<T> type, final AssetLoaderParameters<T> params) {
        IoC.register("preload " + name, assetPreloadStrategy(type, params));
        IoC.register("load " + name, assetLoadStrategy(type, params));
        IoC.register("unload " + name, assetUnloadStrategy(type));
    }

    @Override
    public void init() {
        assetManager = new AssetManager();

        IoC.register("asset manager", new Singleton(assetManager));

        PreloadListLoader preloadListLoader
                = new PreloadListLoader(assetManager.getFileHandleResolver());
        assetManager.setLoader(PreloadList.class, preloadListLoader);

        preloadListLoader.registerClass("lists", PreloadList.class);
        preloadListLoader.registerClass("skins", Skin.class);
        preloadListLoader.registerClass("sounds", Sound.class);
        preloadListLoader.registerClass("particles", ParticleEffect.class);
        preloadListLoader.registerClass("textures", Texture.class);
        preloadListLoader.registerClass("atlases", TextureAtlas.class);
        preloadListLoader.registerClass("texture loops", Texture.class, loopTextureParameter);

        registerAssetTypeStrategies("list", PreloadList.class, null);

        registerAssetTypeStrategies("skin", Skin.class, null);
        registerAssetTypeStrategies("sound", Sound.class, null);
        registerAssetTypeStrategies("particles", ParticleEffect.class, null);
        registerAssetTypeStrategies("texture", Texture.class, null);
        registerAssetTypeStrategies("atlas", TextureAtlas.class, null);

        registerAssetTypeStrategies("texture loop", Texture.class, loopTextureParameter);

        IoC.register("get texture region", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) {
                // TODO:: Implement texture/texture atlas management
                return new TextureRegion(IoC.<Texture>resolve("load texture", args[0] + ".png"));
            }
        });
    }

    @Override
    public void shutdown() {
        assetManager.dispose();
    }
}
