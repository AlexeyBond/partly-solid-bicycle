package com.github.alexeybond.partly_solid_bicycle.ioc;

/**
 *
 */
public enum IoC {;
    private static IoCContextHolder contextHolder;

    public static void use(IoCContext context) {
        contextHolder.set(context);
    }

    public static void init(IoCContextHolder contextHolder) {
        IoC.contextHolder = contextHolder;
    }

    public static void register(String key, IoCStrategy strategy) {
        contextHolder.get().register(key, strategy);
    }

    public static IoCStrategy resolveS(String key) {
        return contextHolder.get().resolve(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T resolve(String key, Object... args) {
        return (T) resolveS(key).resolve(args);
    }
}
