package com.github.alexeybond.partly_solid_bicycle.game.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoCStrategy;
import com.github.alexeybond.partly_solid_bicycle.ioc.modules.Module;
import com.github.alexeybond.partly_solid_bicycle.ioc.strategy.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * <p>
 *      Valid component declarations:
 * </p>
 *
 * <pre>{@code
 *  {
 *      "component type alias": { ..parameters or nothing.. },
 *      "org.my.component.Class": { .. },
 *      "component instance name": { "type": "type alias", .. },
 *      "component instance name": { "type": "org.my.component.Class", .. },
 *      "component instance name": { "class": "org.my.component.Class" }
 *  }
 * }</pre>
 * <p>
 *      The last one uses LibGDX deserializer, not the one registered here.
 * </p>
 */
public class GameSerialization implements Module {
    @Override
    public void init() {
        final Map<String, Class<? extends ComponentDeclaration>> map
                = new HashMap<String, Class<? extends ComponentDeclaration>>();
        IoC.register("component type aliases", new Singleton(map));

        Json json = new Json();
        IoC.register("json for game serialization", new Singleton(json));

        json.setSerializer(ComponentDeclaration.class, new Json.Serializer<ComponentDeclaration>() {
            @Override
            public void write(Json json, ComponentDeclaration object, Class knownType) {
                json.writeValue(object);
            }

            @Override
            public ComponentDeclaration read(Json json, JsonValue jsonData, Class type) {
                String className;
                JsonValue typeValue = jsonData.get("type");
                if (null == typeValue) {
                    className = jsonData.name();
                } else {
                    className = typeValue.asString();
                    jsonData.remove("type");
                }

                Class<? extends ComponentDeclaration> resultType = map.get(className);

                if (null == resultType) {
                    try {
                        resultType = (Class<? extends ComponentDeclaration>) type.getClassLoader().loadClass(className);
                        return json.readValue(resultType, jsonData);
                    } catch (ClassNotFoundException e) {
                        throw new SerializationException(e);
                    }
                }

                return json.readValue(resultType, jsonData);
            }
        });

        IoC.register("load game declaration", new IoCStrategy() {
            @Override
            public Object resolve(Object... args) {
                FileHandle fileHandle = (FileHandle) args[0];
                Json json = IoC.resolve("json for game serialization");

                GameDeclaration declaration = json.fromJson(GameDeclaration.class, fileHandle);

                if (declaration.include.length != 0) {
                    IoCStrategy loadStrategy = IoC.resolveS("load game declaration");
                    FileHandle parent = fileHandle.parent();

                    declaration.included = new GameDeclaration[declaration.include.length];

                    for (int i = 0; i < declaration.include.length; i++) {
                        FileHandle includedFileHandle;

                        includedFileHandle = parent.child(declaration.include[i]);

                        if (!includedFileHandle.exists() || includedFileHandle.isDirectory()) {
                            includedFileHandle = Gdx.files.getFileHandle(declaration.include[i], parent.type());
                        }

                        declaration.included[i]
                                = (GameDeclaration) loadStrategy.resolve(includedFileHandle);
                    }
                }

                return declaration;
            }
        });
    }

    @Override
    public void shutdown() {

    }
}
