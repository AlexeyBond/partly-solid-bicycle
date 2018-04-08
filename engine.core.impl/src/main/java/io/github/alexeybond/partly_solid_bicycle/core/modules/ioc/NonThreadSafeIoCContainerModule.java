package io.github.alexeybond.partly_solid_bicycle.core.modules.ioc;

import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.BaseModule;
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.DefaultContainer;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;

import java.util.Collection;
import java.util.HashMap;

public class NonThreadSafeIoCContainerModule extends BaseModule {
    {
        provide("ioc");
        depend("ioc_holder");
    }

    @Override
    public void init(Collection<Object> env) {
        IoC.use(new DefaultContainer(new HashMap<Object, IoCStrategy>()));
    }

    @Override
    public void shutdown() {

    }
}
