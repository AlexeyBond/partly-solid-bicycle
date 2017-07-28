package com.github.alexeybond.partly_solid_bicycle.drawing.animation.impl;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.github.alexeybond.partly_solid_bicycle.drawing.animation.AnimationInstance;
import com.github.alexeybond.partly_solid_bicycle.drawing.animation.KeyFrame;
import com.github.alexeybond.partly_solid_bicycle.drawing.animation.decl.AnimationDecl;
import com.github.alexeybond.partly_solid_bicycle.drawing.animation.decl.KeyFrameDecl;
import com.github.alexeybond.partly_solid_bicycle.drawing.animation.decl.SequenceDecl;
import com.github.alexeybond.partly_solid_bicycle.drawing.animation.decl.TransformDecl;
import com.github.alexeybond.partly_solid_bicycle.drawing.sprite.DefaultSpriteTemplate;
import com.github.alexeybond.partly_solid_bicycle.ioc.IoC;
import com.github.alexeybond.partly_solid_bicycle.util.event.EventsOwner;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Animation {
    private final Array<String> eventNames = new Array<String>(true, 0);

    private final Map<String, Sequence> sequences = new HashMap<String, Sequence>();
    private final Sequence defaultSequence;
    private final String defaultSequenceName;

    Sequence sequence(String name) {
        Sequence sequence = sequences.get(name);

        if (null == sequence)
            throw new IllegalArgumentException("Illegal sequence name: \"" + name + "\".");

        return sequence;
    }

    Sequence defaultSequence() {
        return defaultSequence;
    }

    public String defaultSequenceName() {
        return defaultSequenceName;
    }

    /**
     * Create a animation instance that causes events on given event group when event is described in
     * keyframe declaration.
     */
    public <E extends EventsOwner> BoundAnimationInstance bind(E events) {
        return new BoundAnimationInstance(this, events.events(), eventNames);
    }

    /**
     * Create a animation instance that will not trigger events described in keyframe declarations.
     */
    public AnimationInstance instantiate() {
        return new BasicAnimationInstance(this);
    }

    /**
     * Constructs an animation using given declaration object.
     *
     * <p>This constructor is expansive enough so do not create more than one {@link Animation} for a
     * {@link AnimationDecl}.</p>
     */
    public Animation(AnimationDecl decl) {
        defaultSequenceName = decl.defaultSequence;

        final ObjectIntMap<String> eventNameMap = new ObjectIntMap<String>();

        final Vector2 tmp = new Vector2();
        final TransformDecl animationTransform = decl.transform == null ? TransformDecl.DEFAULT : decl.transform;

        for (Map.Entry<String, SequenceDecl> sequenceEntry : decl.sequences.entrySet()) {
            SequenceDecl sd = sequenceEntry.getValue();

            final TransformDecl sequenceTransform = sd.transform == null ? animationTransform : sd.transform;

            if (sd.frames == null || sd.frames.size == 0)
                throw new IllegalArgumentException("Illegal animation sequence without keyframes: \""
                    + sequenceEntry.getKey() + "\".");

            Array<KeyFrameImpl> keyFrames = new Array<KeyFrameImpl>(true, sd.frames.size);

            for (int i = 0; i < sd.frames.size; i++) {
                KeyFrameDecl kfd = sd.frames.get(i);

                final TransformDecl frameTransform = kfd.transform == null ? sequenceTransform : kfd.transform;

                TextureRegion region = IoC.resolve("get texture region", kfd.image);
                int eventIndex = -1;

                if (null != kfd.event) {
                    eventIndex = eventNameMap.get(kfd.event, -1);

                    if (-1 == eventIndex) {
                        eventIndex = eventNames.size;
                        eventNames.add(kfd.event);
                    }
                }

                frameTransform.getOffset(tmp, region);

                keyFrames.add(new KeyFrameImpl(
                        region,
                        tmp.x, tmp.y,
                        frameTransform.scale,
                        frameTransform.rotate,
                        kfd.duration,
                        eventIndex
                ));
            }

            Sequence sequence = new Sequence();

            sequence.loop = sd.loop;
            sequence.frames = keyFrames;

            sequences.put(sequenceEntry.getKey(), sequence);
        }

        defaultSequence = sequences.get(defaultSequenceName);

        if (null == defaultSequence)
            throw new IllegalArgumentException("Default sequence \"" + defaultSequenceName + "\" not defined.");
    }

    static class Sequence {
        Array<KeyFrameImpl> frames;

        boolean loop;
    }

    static class KeyFrameImpl extends DefaultSpriteTemplate implements KeyFrame {
        final float duration;
        final int eventIndex;

        KeyFrameImpl(
                TextureRegion region,
                float originX, float originY,
                float scale, float rotation,
                float duration, int eventIndex) {
            super(region, originX, originY, scale, rotation);
            this.duration = duration;
            this.eventIndex = eventIndex;
        }
    }
}
