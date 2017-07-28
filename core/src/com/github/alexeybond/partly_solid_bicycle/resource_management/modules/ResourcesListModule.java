package com.github.alexeybond.partly_solid_bicycle.resource_management.modules;

import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;
import com.github.alexeybond.partly_solid_bicycle.resource_management.PreloadList;

/**
 * Module that keeps loaded a list of resources.
 *
 * Instances of this class may be added to {@link com.github.alexeybond.partly_solid_bicycle.application.Application}
 * or {@link com.github.alexeybond.partly_solid_bicycle.application.Screen} to keep resources for proper time.
 */
public class ResourcesListModule implements Module {
    private final String listName;

    public ResourcesListModule(String listName) {
        this.listName = listName;
    }

    @Override
    public void init() {
        IoC.<PreloadList>resolve("preload list", listName);
    }

    @Override
    public void shutdown() {
        IoC.resolve("unload list", listName);
    }
}
