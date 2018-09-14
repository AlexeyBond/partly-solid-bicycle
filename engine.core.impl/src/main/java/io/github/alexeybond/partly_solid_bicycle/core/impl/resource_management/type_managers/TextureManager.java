package io.github.alexeybond.partly_solid_bicycle.core.impl.resource_management.type_managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.alexeybond.partly_solid_bicycle.core.modules.resource_management.GdxResourceTypeManagersModule;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import org.jetbrains.annotations.NotNull;

@Component(
        kind = "applicationComponent",
        name = "textureManager",
        modules = {
                GdxResourceTypeManagersModule.class
        }
)
public class TextureManager extends BaseGdxAssetTypeManager<TextureRegion, Texture> {
    private final static String[] DEFAULT_EXTENSIONS = {
            ".png",
    };

    {
        fileExtensions = DEFAULT_EXTENSIONS;
    }

    @NotNull
    @Override
    protected Class<Texture> getGdxResourceClass() {
        return Texture.class;
    }

    @NotNull
    @Override
    protected TextureRegion convertGdxResource(@NotNull Texture texture) {
        return new TextureRegion(texture);
    }
}
