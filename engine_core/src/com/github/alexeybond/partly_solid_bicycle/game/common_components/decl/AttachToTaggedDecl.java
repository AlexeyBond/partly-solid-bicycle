package com.github.alexeybond.partly_solid_bicycle.game.common_components.decl;

import com.github.alexeybond.partly_solid_bicycle.game.Component;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.Game;
import com.github.alexeybond.partly_solid_bicycle.game.common_components.AttachmentComponent;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.ComponentDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.declarative.GameDeclaration;
import com.github.alexeybond.partly_solid_bicycle.game.systems.tagging.TaggingSystem;

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
