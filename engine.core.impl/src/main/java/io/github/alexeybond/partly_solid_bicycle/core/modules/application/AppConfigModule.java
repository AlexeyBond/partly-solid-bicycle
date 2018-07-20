package io.github.alexeybond.partly_solid_bicycle.core.modules.application;

import com.badlogic.gdx.Gdx;
import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.BaseModule;
import io.github.alexeybond.partly_solid_bicycle.core.impl.data.gdxjson.GdxJsonDataReader;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.factory.NodeFactories;
import io.github.alexeybond.partly_solid_bicycle.core.impl.world_tree.populator.DeclarativePopulator;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.common.id.IdSet;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.LogicNode;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.world_tree.NodeFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class AppConfigModule extends BaseModule {
    {
        provide("application_config");

        depend("application_root");
        depend("core_components");
    }

    private static final String DEFAULT_PATH = "application.json";

    @NotNull
    private final String configPath;

    public AppConfigModule(@NotNull String configPath) {
        this.configPath = configPath;
    }

    public AppConfigModule() {
        this(DEFAULT_PATH);
    }

    @Override
    public void init(Collection<Object> env) {
        LogicNode appRoot = IoC.resolve("application root node");
        IdSet<LogicNode> idSet = appRoot.getTreeContext().getIdSet();

        LogicNode configNode = appRoot.getOrAdd(idSet.get("config"), NodeFactories.EMPTY_GROUP, null);

        NodeFactory<InputDataObject> nodeFactory = IoC.resolve(
                "node factory for node kind", "applicationComponent");

        InputDataObject configData = new GdxJsonDataReader()
                .parseData(Gdx.files.internal(configPath));

        configNode.populate(new DeclarativePopulator(nodeFactory, configData));
    }

    @Override
    public void shutdown() {

    }
}
