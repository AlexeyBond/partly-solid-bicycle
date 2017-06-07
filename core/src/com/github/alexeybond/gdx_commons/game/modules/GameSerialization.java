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
    }

    @Override
    public void shutdown() {

    }
}
