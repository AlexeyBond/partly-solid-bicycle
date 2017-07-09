package com.github.alexeybond.gdx_commons.game.declarative.visitor.impl;

import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.GameSystem;
import com.github.alexeybond.gdx_commons.game.declarative.EntityDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.visitor.GameDeclarationVisitor;
import com.github.alexeybond.gdx_commons.util.event.props.Property;

public class ApplyGameDeclarationVisitor implements GameDeclarationVisitor {
    private final ApplyEntityDeclarationVisitor entityDeclarationVisitor = new ApplyEntityDeclarationVisitor();

    private Game game;
    private GameDeclaration rootGameDeclaration;

    @Override
    public void beginVisitDeclaration(GameDeclaration gameDeclaration) {

    }

    @Override
    public void endVisitDeclaration(GameDeclaration gameDeclaration) {

    }

    @Override
    public void visitIncludedDeclaration(String name, GameDeclaration gameDeclaration) {
        gameDeclaration.visit(this);
    }

    @Override
    public boolean beginVisitClassDeclarations() {
        return false;
    }

    @Override
    public void endVisitClassDeclarations() {

    }

    @Override
    public void visitClassDeclaration(String className, EntityDeclaration entityDeclaration) {

    }

    @Override
    public boolean beginVisitEntityDeclarations() {
        return true;
    }

    @Override
    public void endVisitEntityDeclarations() {

    }

    @Override
    public void visitEntityDeclaration(EntityDeclaration entityDeclaration) {
        entityDeclarationVisitor.doVisit(entityDeclaration, rootGameDeclaration, new Entity(game));
    }

    @Override
    public boolean beginVisitProperties() {
        return true;
    }

    @Override
    public void endVisitProperties() {

    }

    @Override
    public void visitProperty(String propertyName, String[] propertyValue) {
        game.events().<Property<GameSystem>>event(propertyName).load(null, propertyValue);
    }

    public Game doVisit(GameDeclaration gameDeclaration, Game game) {
        try {
            this.rootGameDeclaration = gameDeclaration;
            this.game = game;

            rootGameDeclaration.visit(this);
        } finally {
            this.rootGameDeclaration = null;
            this.game = null;
        }

        return game;
    }
}
