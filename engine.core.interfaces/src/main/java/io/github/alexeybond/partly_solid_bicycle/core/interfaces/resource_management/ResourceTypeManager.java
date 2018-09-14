package io.github.alexeybond.partly_solid_bicycle.core.interfaces.resource_management;

import org.jetbrains.annotations.NotNull;

/**
 * Manager of a single type of resources.
 * <p>
 * Some subclass may represent (for example) texture manager, sound manager, etc.
 * </p>
 *
 * @param <TResource>
 */
public interface ResourceTypeManager<TResource> {
    @NotNull
    ResourceReference<TResource> getResource(@NotNull String id);
}
