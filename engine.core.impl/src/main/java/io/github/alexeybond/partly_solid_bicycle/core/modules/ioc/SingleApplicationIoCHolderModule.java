package io.github.alexeybond.partly_solid_bicycle.core.modules.ioc;

import io.github.alexeybond.partly_solid_bicycle.core.impl.app.module.BaseModule;
import io.github.alexeybond.partly_solid_bicycle.core.impl.ioc.SingleApplicationHolder;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.ioc.IoC;

import java.util.Collection;

public class SingleApplicationIoCHolderModule extends BaseModule {
    {
        provide("ioc_holder");
    }

    @Override
    public void init(Collection<Object> env) {
        IoC.use(new SingleApplicationHolder());
    }

    @Override
    public void shutdown() {

    }
}
