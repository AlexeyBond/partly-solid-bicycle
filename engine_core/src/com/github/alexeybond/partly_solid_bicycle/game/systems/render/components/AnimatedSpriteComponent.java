package com.github.alexeybond.partly_solid_bicycle.game.systems.render.components;

import com.github.alexeybond.partly_solid_bicycle.drawing.animation.AnimationInstance;
import com.github.alexeybond.partly_solid_bicycle.drawing.animation.impl.Animation;
import com.github.alexeybond.partly_solid_bicycle.drawing.sprite.SpriteInstance;
import com.github.alexeybond.partly_solid_bicycle.drawing.sprite.SpriteTemplate;
import com.github.alexeybond.partly_solid_bicycle.game.Entity;
import com.github.alexeybond.partly_solid_bicycle.game.systems.timing.TimingSystem;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventListener;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.FloatProperty;
import com.github.alexeybond.partly_solid_bicycle.util.event.props.ObjectProperty;

/**
 *
 */
public class AnimatedSpriteComponent
        extends SpriteComponent {
    private final Animation animation;
    private AnimationInstance animationInstance;
    private ObjectProperty<String> animationProp;
    private FloatProperty deltaTimeProp;
    private int animationPropSubIdx = -1, timeSubIdx = -1;

    public AnimatedSpriteComponent(
            String passName, SpriteInstance sprite, float scale, Animation animation) {
        super(passName, sprite, scale);
        this.animation = animation;
    }

    @Override
    protected SpriteTemplate getTemplate() {
        return animationInstance.currentFrame();
    }

    @Override
    public void onConnect(Entity entity) {
        animationInstance = animation.<Entity>bind(entity);
        animationProp = entity.events().event(
                "animation", ObjectProperty.<String>make(animation.defaultSequenceName()));

        animationPropSubIdx = animationProp.subscribe(
                new EventListener<ObjectProperty<String>>() {
            @Override
            public boolean onTriggered(ObjectProperty<String> event) {
                animationInstance.setSequence(event.get());
                return true;
            }
        });

        TimingSystem timingSystem = entity.game().systems().get("timing");
        deltaTimeProp = timingSystem.events().event("deltaTime");

        timeSubIdx = deltaTimeProp.subscribe(
                new EventListener<FloatProperty>() {
            @Override
            public boolean onTriggered(FloatProperty event) {
                animationInstance.update(event.get());
                return true;
            }
        });

        super.onConnect(entity);
    }

    @Override
    public void onDisconnect(Entity entity) {
        super.onDisconnect(entity);

        animationPropSubIdx = animationProp.unsubscribe(animationPropSubIdx);
        timeSubIdx = deltaTimeProp.unsubscribe(timeSubIdx);
        animationInstance.dispose();
    }
}
