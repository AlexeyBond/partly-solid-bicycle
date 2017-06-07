package com.github.alexeybond.gdx_commons.game.modules;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.ioc.Module;
import com.github.alexeybond.gdx_commons.ioc.strategy.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 *
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
                String className = jsonData.getString("type");
                Class<? extends ComponentDeclaration> resultType = map.get(className);
                jsonData.remove("type");

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
    }

    @Override
    public void shutdown() {

    }
}
