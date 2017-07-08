package com.github.alexeybond.gdx_commons.game.common_components.decl;

import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.Game;
import com.github.alexeybond.gdx_commons.game.common_components.AttachmentComponent;
import com.github.alexeybond.gdx_commons.game.declarative.ComponentDeclaration;
import com.github.alexeybond.gdx_commons.game.declarative.GameDeclaration;
import com.github.alexeybond.gdx_commons.game.systems.tagging.TaggingSystem;

/**
 *
 */
public class AttachToTaggedDecl implements ComponentDeclaration {
    public String tag;

    @Override
    public Component create(GameDeclaration gameDeclaration, Game game) {
        return new AttachmentComponent() {
            @Override
            protected Entity getMaster(Entity slave) {
                TaggingSystem taggingSystem = slave.game().systems().get("tagging");
                return taggingSystem.group(tag).getOnly();
            }
        };
    }
}
