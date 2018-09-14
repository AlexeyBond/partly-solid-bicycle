package io.github.alexeybond.partly_solid_bicycle.core.modules.resource_management;

import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.GeneratedModule;

@GeneratedModule(
        depends = {
                "gdx_asset_manager"
        },
        reverseDepends = {"application_config"}
)
public class GdxResourceTypeManagersModule extends GdxResourceTypeManagersModuleImpl {
}
