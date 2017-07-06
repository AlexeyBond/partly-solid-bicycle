package com.github.alexeybond.gdx_commons.game.systems.render.modules;

import com.github.alexeybond.gdx_commons.game.systems.render.camera.zoom.ConstantZoomFunction;
import com.github.alexeybond.gdx_commons.game.systems.render.camera.zoom.FixedDiagonalFunction;
import com.github.alexeybond.gdx_commons.game.systems.render.camera.zoom.UnitZoomFunction;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.IoCStrategy;
import com.github.alexeybond.gdx_commons.ioc.modules.Module;
import com.github.alexeybond.gdx_commons.ioc.strategy.Singleton;

/**
 *
 */
public class ZoomFunctionsModule implements Module {
    @Override
    public void init() {
        IoC.register("unit zoom function", new Singleton(UnitZoomFunction.INSTANCE));
        IoC.register("constant zoom function", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) {
                return new ConstantZoomFunction(Float.parseFloat(String.valueOf(args[0])));
            }
        });
        IoC.register("fixed diagonal zoom function", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) {
                return new FixedDiagonalFunction(Float.parseFloat(String.valueOf(args[0])));
            }
        });
    }

    @Override
    public void shutdown() {

    }
}
