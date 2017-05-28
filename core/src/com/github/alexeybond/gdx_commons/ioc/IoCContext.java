package com.github.alexeybond.gdx_commons.ioc;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class IoCContext {
    private Map<String, IoCStrategy> strategyMap = new HashMap<String, IoCStrategy>();

    public void register(String key, IoCStrategy strategy) {
        strategyMap.put(key, strategy);
    }

    public IoCStrategy resolve(String key) {
        IoCStrategy strategy = strategyMap.get(key);

        if (null == strategy) throw new RuntimeException("Not found: " + key);

        return strategy;
    }
}
