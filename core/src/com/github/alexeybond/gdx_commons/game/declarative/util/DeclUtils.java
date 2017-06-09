package com.github.alexeybond.gdx_commons.game.declarative.util;

import com.badlogic.gdx.math.Vector2;
import com.github.alexeybond.gdx_commons.game.declarative.EntityDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;

/**
 *
 */
public enum  DeclUtils {;
    public static Vector2 readVector(Vector2 present, float[] val, float valX, float valY) {
        if (null != present) return present;

        if (val.length != 0) return new Vector2(val[0], val[1]);

        return new Vector2(valX, valY);
    }

    public static EntityDeclaration[] readClasses(
            EntityDeclaration[] present, GameDeclaration gameDeclaration, String[] classNames) {
        if (null != present) return present;

        present = new EntityDeclaration[classNames.length];

        for (int i = 0; i < classNames.length; i++) {
            present[i] = gameDeclaration.getEntityClass(classNames[i]);
        }

        return present;
    }
}
