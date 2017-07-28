package com.github.alexeybond.partly_solid_bicycle.game.declarative.visitor.impl;

import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.EntityDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.visitor.EntityDeclarationVisitor;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.Property;

/**
 *
 */
public class ApplyEntityDeclarationVisitor implements EntityDeclarationVisitor {
    private Entity entity;
    private GameDeclaration gameDeclaration;

    @Override
    public void beginVisitDeclaration(EntityDeclaration entityDeclaration) {

    }

    @Override
    public void visitInheritedClasses(String[] classNames) {
        for (int i = 0; i < classNames.length; i++)
            gameDeclaration.getEntityClass(classNames[i]).visit(this);
    }

    @Override
    public void visitComponent(String componentName, ComponentDeclaration componentDeclaration) {
        entity.components().add(componentName, componentDeclaration.create(gameDeclaration, entity.game()));
    }

    @Override
    public void visitProperty(String propertyName, String[] propertyValue) {
        entity.events().<Property>event(propertyName).load(propertyValue);
    }

    @Override
    public void endVisitDeclaration(EntityDeclaration entityDeclaration) {

    }

    public Entity doVisit(EntityDeclaration entityDeclaration, GameDeclaration gameDeclaration, Entity entity) {
        try {
            this.entity = entity;
            this.gameDeclaration = gameDeclaration;

            entityDeclaration.visit(this);
        } finally {
            this.entity = null;
            this.gameDeclaration = null;
        }

        return entity;
    }
}
