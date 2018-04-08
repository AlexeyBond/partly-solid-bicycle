package io.github.alexeybond.partly_solid_bicycle.core.modules.ioc;

import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.BaseModule;
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.DefaultContainer;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoCStrategy;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadSafeIoCContainerModule extends BaseModule {
    {
        provide("ioc");
        provide("ioc_threadsafe");

        depend("ioc_holder");
    }

    @Override
    public void init(Collection<Object> env) {
        IoC.use(new DefaultContainer(new ConcurrentHashMap<Object, IoCStrategy>()));
    }

    @Override
    public void shutdown() {

    }
}
