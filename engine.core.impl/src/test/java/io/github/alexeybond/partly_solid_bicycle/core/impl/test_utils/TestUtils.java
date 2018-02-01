package io.github.alexeybond.partly_solid_bicycle.core.impl.test_utils;

import io.github.alexeybond.partly_solid_bicycle.core.impl.data.gdxjson.GdxJsonDataReader;
import io.github.alexeybond.partly_solid_bicycle.core.interfaces.data.InputDataObject;

public enum TestUtils {
    ;

    public static InputDataObject parseJSON(String str) {
        return new GdxJsonDataReader().parseData(str);
    }
}
