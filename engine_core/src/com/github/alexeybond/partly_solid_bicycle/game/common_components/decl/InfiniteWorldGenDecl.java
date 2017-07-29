package com.github.alexeybond.partly_solid_bicycle.game.common_components.decl;

import com.badlogic.gdx.utils.Array;
import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.common_components.InfiniteWorldGenerator;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.EntityDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;

/**
 *
 */
public class InfiniteWorldGenDecl implements ComponentDeclaration {
    public String observerTag = "player";
    public String generateTag = "generated";
    public String[] generateClasses;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        Array<EntityDeclaration> genClasses = new Array<EntityDeclaration>(generateClasses.length);

        for (int i = 0; i < generateClasses.length; i++) {
            genClasses.add(gameDeclaration.getEntityClass(generateClasses[i]));
        }

        return new InfiniteWorldGenerator(observerTag, generateTag, genClasses, gameDeclaration);
    }
}
