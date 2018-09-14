package io.github.alexeybond.partly_solid_bicycle.core.impl.resource_management.type_managers;

import com.badlogic.gdx.audio.Sound;
import io.github.alexeybond.partly_solid_bicycle.core.modules.resource_management.GdxResourceTypeManagersModule;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import org.jetbrains.annotations.NotNull;

@Component(
        kind = "applicationComponent",
        name = "soundManager",
        modules = {
                GdxResourceTypeManagersModule.class
        }
)
public class SoundManager extends BaseGdxAssetTypeManager<Sound, Sound> {
    private final static String[] DEFAULT_EXTENSIONS = {
            ".wav",
    };

    {
        fileExtensions = DEFAULT_EXTENSIONS;
    }

    @NotNull
    @Override
    protected Class<Sound> getGdxResourceClass() {
        return Sound.class;
    }

    @NotNull
    @Override
    protected Sound convertGdxResource(@NotNull Sound sound) {
        return sound;
    }
}
