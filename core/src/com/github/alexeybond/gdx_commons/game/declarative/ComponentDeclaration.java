package com.github.alexeybond.gdx_commons.game.declarative;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Game;

/**
 *
 * <pre>{@code
 *     // ...
 *
 *     class MyComponent implements Component {
 *         // ...
 *     }
 *     public static class Declaration implements ComponentDeclaration {
 *         public String parameter1, parameter2;
 *
 *         @Override
 *         public Component create() {
 *             return new MyComponent(parameter1, parameter2);
 *         }
 *     }
 * }</pre>
 *
 * In entity declaration:
 *
 * <pre>
 *     "componentX": {
 *       "class": "org.my.game.Declaration",
 *       "parameter1": "value1",
 *       "parameter2": "value2"
 *     }
 * </pre>
 */
public interface ComponentDeclaration {
    Component create(GameDeclaration gameDeclaration, Game game);
}
