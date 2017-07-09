package com.github.alexeybond.gdx_commons.game.declarative.visitor;

import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.EntityDeclaration;

/**
 *
 */
public interface EntityDeclarationVisitor {
    void beginVisitDeclaration(EntityDeclaration entityDeclaration);

    void visitInheritedClasses(String[] classNames);

    void visitComponent(String componentName, ComponentDeclaration componentDeclaration);

    void visitProperty(String propertyName, String[] propertyValue);

    void endVisitDeclaration(EntityDeclaration entityDeclaration);
}
