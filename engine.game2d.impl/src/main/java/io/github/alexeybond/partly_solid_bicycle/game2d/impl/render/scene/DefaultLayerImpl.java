package io.github.alexeybond.partly_solid_bicycle.game2d.impl.render.scene;

import com.badlogic.gdx.utils.Array;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.DrawingState;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene.Layer;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene.SubLayer;
import io.github.alexeybond.partly_solid_bicycle.game2d.interfaces.render.scene.SubLayerFactory;
import org.jetbrains.annotations.NotNull;

public class DefaultLayerImpl implements Layer {
    private class SubLayerRecord implements Comparable<SubLayerRecord> {
        private final int priority;
        @NotNull
        private final String id;
        @NotNull
        private final SubLayer subLayer;

        private SubLayerRecord(int priority, @NotNull String id, @NotNull SubLayer subLayer) {
            this.priority = priority;
            this.id = id;
            this.subLayer = subLayer;
        }

        @Override
        public int compareTo(@NotNull SubLayerRecord o) {
            if (priority < o.priority) return -1;
            if (priority > o.priority) return 1;
            return 0;
        }
    }

    private boolean listDirty = false;

    private final Array<SubLayerRecord> subLayersList = new Array<SubLayerRecord>();

    private SubLayer getOrCreateSubLayer(int priority, String id, SubLayerFactory<?> factory) {
        for (int i = 0; i < subLayersList.size; i++) {
            SubLayerRecord record = subLayersList.get(i);
            if (record.priority == priority && record.id.equals(id)) {
                return record.subLayer;
            }
        }

        SubLayerRecord record = new SubLayerRecord(priority, id, factory.create());
        subLayersList.add(record);
        listDirty = true;
        return record.subLayer;
    }

    @Override
    @NotNull
    public <T extends SubLayer> T subLayer(int priority, @NotNull String id, @NotNull SubLayerFactory<T> factory) {
        @SuppressWarnings({"unchecked"})
        T subLayer = (T) getOrCreateSubLayer(priority, id, factory);

        return subLayer;
    }

    @Override
    public void dispose() {
        for (SubLayerRecord record : subLayersList) {
            record.subLayer.dispose();
        }
    }

    @Override
    public void draw(@NotNull DrawingState state) {
        if (listDirty) {
            subLayersList.sort();
            listDirty = false;
        }

        for (SubLayerRecord record : subLayersList) {
            record.subLayer.draw(state);
        }
    }
}
