package io.github.alexeybond.partly_solid_bicycle.core.modules.resource_management;

import com.badlogic.gdx.assets.AssetManager;
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.BaseModule;
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.strategies.Singleton;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;

import java.util.Collection;

public class GlobalGdxAssetManagerModule extends BaseModule {
    {
        depend("ioc");

        provide("global_gdx_asset_manager");
        provide("gdx_asset_manager");
    }

    private AssetManager assetManager;

    @Override
    public void init(Collection<Object> env) {
        assetManager = new AssetManager();

        IoC.register("libgdx asset manager", new Singleton(assetManager));
    }

    @Override
    public void shutdown() {
        assetManager.dispose();
        assetManager = null;
    }
}
