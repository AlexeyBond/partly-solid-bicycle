package io.github.alexeybond.partly_solid_bicycle.core.impl.data.storage;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonWriter;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.NullInputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.gdxjson.GdxJsonDataReader;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.gdxjson.GdxJsonWriter;
import io.github.alexeybond.partly_solid_bicycle.core.impl.event.listeners.UniqueSubscription;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.Id;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.Topic;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.event.variables.ObjectVariable;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Component;
import io.github.alexeybond.partly_solid_bicycle.engine.preprocessing.annotations.Optional;
import org.jetbrains.annotations.NotNull;

import java.io.Writer;

@Component(
        name = "fileStorage",
        kind = "applicationComponent"
)
public class FileStorageNode extends StorageNode {
    private boolean dirty = false;

    private Runnable writeTask = new Runnable() {
        @Override
        public void run() {
            if (dirty) {
                ObjectVariable<InputDataObject> variable = getComponent();
                Writer writer = Gdx.files.local(filePath).writer(false);

                try {
                    try {
                        new GdxJsonWriter().doWrite(
                                new JsonWriter(writer),
                                variable.get()
                        );
                    } finally {
                        writer.close();
                    }
                } catch (Exception e) {
                    Gdx.app.error(
                            "FILE-STORAGE",
                            "Error writing '" +
                                    filePath + "'",
                            e
                    );
                    return;
                }

                dirty = false;

                Gdx.app.log(
                        "FILE-STORAGE",
                        "File '" +
                                filePath +
                                "' updated"
                );
            }
        }
    };

    private UniqueSubscription<Object> changeSubscription = new UniqueSubscription<Object>() {
        @Override
        protected void doReceive(@NotNull Object event, @NotNull Topic<?> topic) {
            dirty = true;
            Gdx.app.postRunnable(writeTask);
        }
    };

    /**
     * Path to local file containing current data.
     */
    public String filePath;

    /**
     * Path to internal file containing default data (such as default settings).
     */
    @Optional
    public String defaultFilePath = null;

    /**
     * Default content of the storage.
     */
    @Optional
    public InputDataObject defaults = NullInputDataObject.INSTANCE;

    public FileStorageNode() {
        super(NullInputDataObject.INSTANCE);
    }

    @Override
    protected void onConnected0(@NotNull LogicNode parent, Id<LogicNode> id) {
        super.onConnected0(parent, id);

        loadDataFile(Files.FileType.Local, filePath);
        loadDataFile(Files.FileType.Internal, defaultFilePath);
        loadDefaults(defaults);

        changeSubscription.subscribe((Topic<?>) getComponent());
    }

    @Override
    protected void onDisconnected0(@NotNull LogicNode parent) {
        changeSubscription.clear();

        super.onDisconnected0(parent);
    }

    private void loadDataFile(Files.FileType fileType, String filePath) {
        if (null == filePath) {
            return;
        }

        FileHandle handle = Gdx.files.getFileHandle(filePath, fileType);

        if (!handle.exists() || handle.isDirectory()) {
            Gdx.app.error(
                    "FILE-STORAGE",
                    "File '" + filePath + "' (" + fileType.name() + ") does not exist"
            );

            return;
        }

        InputDataObject data = new GdxJsonDataReader().parseData(handle);

        loadDefaults(data);
    }
}
