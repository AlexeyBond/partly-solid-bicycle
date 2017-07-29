package com.github.alexeybond.partly_solid_bicycle.game.declarative.visitor;

import com.github.alexeybond.partly_solid_bicycle.game.declarative.EntityDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;

/**
 *
 */
public interface GameDeclarationVisitor {
    void beginVisitDeclaration(GameDeclaration gameDeclaration);

    void endVisitDeclaration(GameDeclaration gameDeclaration);

    void visitIncludedDeclaration(String name, GameDeclaration gameDeclaration);

    boolean beginVisitClassDeclarations();

    void endVisitClassDeclarations();

    void visitClassDeclaration(String className, EntityDeclaration entityDeclaration);

    boolean beginVisitEntityDeclarations();

    void endVisitEntityDeclarations();

    void visitEntityDeclaration(EntityDeclaration entityDeclaration);

    boolean beginVisitProperties();

    void endVisitProperties();

    void visitProperty(String propertyName, String[] propertyValue);
}
