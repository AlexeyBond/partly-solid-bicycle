package io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene.sublayers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene.SubLayer;
import org.jetbrains.annotations.NotNull;

public interface GdxStageSubLayer extends SubLayer {
    @NotNull
    Stage getStage();
}
