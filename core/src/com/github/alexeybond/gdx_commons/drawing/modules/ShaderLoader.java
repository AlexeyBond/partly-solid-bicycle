package com.github.alexeybond.gdx_commons.drawing.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.IoCStrategy;
import com.github.alexeybond.gdx_commons.ioc.modules.Module;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ShaderLoader implements Module {
    private Map<String, ShaderProgram> shaders = new HashMap<String, ShaderProgram>();

    @Override
    public void init() {
        IoC.register("load shader from files", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) {
                String vsFileName = (String) args[0];
                String psFileName = (String) args[1];

                String key = vsFileName + "\\/" + psFileName;

                ShaderProgram program = shaders.get(key);

                if (null == program) {
                    program = new ShaderProgram(
                            Gdx.files.internal(vsFileName),
                            Gdx.files.internal(psFileName)
                    );

                    if (!program.isCompiled()) {
                        Gdx.app.log("SHADERS", program.getVertexShaderSource());
                        Gdx.app.log("SHADERS", program.getFragmentShaderSource());
                        Gdx.app.log("SHADERS", program.getLog());
                        program.dispose();
                        throw new RuntimeException("Could not load/compile shader from '" + vsFileName + "' and '" + psFileName +"'.");
                    }

                    shaders.put(key, program);
                }

                return program;
            }
        });
    }

    @Override
    public void shutdown() {
        shaders.clear();
    }
}
