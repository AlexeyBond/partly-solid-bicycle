package io.github.alexeybond.partly_solid_bicycle.core.impl.resource_management.type_managers;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import io.github.alexeybond.partly_solid_bicycle.core.impl.resource_management.utils.ResourceUtils;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Listener;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.resource_management.ResourceReference;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.resource_management.ResourceTypeManager;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Optional;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.SkipProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public abstract class BaseGdxAssetTypeManager<TResource, TGdxResource>
        implements ResourceTypeManager<TResource> {
    private final static Logger logger = new Logger("ResourceManager", Logger.DEBUG);

    @NotNull
    @Optional
    public String[] fileExtensions = {};

    @Nullable
    @Optional
    public String resourcePlaceholder = null;

    @NotNull
    private final AssetManager assetManager;

    private class ResourceReferenceImpl implements ResourceReference<TResource> {
        @NotNull
        private final AssetDescriptor<TGdxResource> descriptor;

        private TResource resource = null;

        private void preloadAsset() {
            assetManager.load(descriptor);
        }

        private ResourceReferenceImpl(@NotNull String assetName) {
            this.descriptor = new AssetDescriptor<TGdxResource>(
                    assetName,
                    getGdxResourceClass(),
                    getGdxLoaderParameters()
            );

            preloadAsset();
        }

        @NotNull
        private TResource get0() {
            if (null != resource) return resource;

            TGdxResource gdxResource;

            try {
                gdxResource = assetManager.get(descriptor.fileName, getGdxResourceClass());
            } catch (GdxRuntimeException e) {
                logger.info("Loading asset synchronously: " + descriptor.fileName);
                logger.debug("Exception that caused synchronous load of '" +
                                descriptor.fileName +
                                "':",
                        e
                );
                preloadAsset();
                assetManager.finishLoadingAsset(descriptor.fileName);
                gdxResource = assetManager.get(descriptor.fileName, getGdxResourceClass());
            }

            return resource = convertGdxResource(gdxResource);
        }

        @NotNull
        @Override
        public TResource get() {
            return get0();
        }

        @Override
        public void pull(@NotNull Listener<? super TResource> listener) {
            listener.receive(get0(), this);
        }

        @Override
        public void dispose() {
            if (null == resource) return;

            resource = null;
            try {
                assetManager.unload(descriptor.fileName);
            } catch (Exception e) {
                logger.debug("Error unloading asset", e);
            }
        }

        @Override
        public Object subscribe(@NotNull Listener<? super TResource> listener) {
            return null;
        }

        @Override
        public void unsubscribe(Object token, @NotNull Listener<? super TResource> listener) {

        }
    }

    protected BaseGdxAssetTypeManager() {
        this(IoC.<AssetManager>resolve("libgdx asset manager"));
    }

    protected BaseGdxAssetTypeManager(@NotNull AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @NotNull
    @Override
    @SkipProperty
    public ResourceReference<TResource> getResource(@NotNull String id) {
        return new ResourceReferenceImpl(getAssetPath(id));
    }

    @NotNull
    protected abstract Class<TGdxResource> getGdxResourceClass();

    @NotNull
    protected String getAssetPath(@NotNull String resourceId) {
        String path = ResourceUtils.getExistFilePath(
                ResourceUtils.getFilePathForResourceId(resourceId),
                fileExtensions
        );

        if (null != path) return path;

        if (null == resourcePlaceholder)
            throw new NoSuchElementException("Resource not found: " + resourceId);

        logger.error("Placeholder used for resource " + resourceId + " as no files were found ");

        path = ResourceUtils.getExistFilePath(
                ResourceUtils.getFilePathForResourceId(resourcePlaceholder),
                fileExtensions
        );

        return null == path ? resourcePlaceholder : path;
    }

    @Nullable
    protected AssetLoaderParameters<TGdxResource> getGdxLoaderParameters() {
        return null;
    }

    @NotNull
    protected abstract TResource convertGdxResource(@NotNull TGdxResource resource);
}
