package io.github.alexeybond.partly_solid_bicycle.core.impl.test_utils;

import io.github.alexeybond.partly_solid_bicycle.core.impl.data.gdxjson.GdxJsonDataReader;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;

import java.io.InputStream;

public enum TestUtils {
    ;

    public static InputDataObject parseJSON(String str) {
        return new GdxJsonDataReader().parseData(str);
    }

    public static InputDataObject parseJSON(Class<?> clz, String resource) {
        InputStream is = clz.getResourceAsStream(resource);

        try {
            return new GdxJsonDataReader().parseData(is);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
