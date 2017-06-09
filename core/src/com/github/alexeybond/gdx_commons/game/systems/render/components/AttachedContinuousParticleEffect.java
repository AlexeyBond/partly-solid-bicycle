package com.github.alexeybond.gdx_commons.game.systems.render.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.alexeybond.gdx_commons.drawing.DrawingContext;
import com.github.alexeybond.gdx_commons.game.Component;
import com.github.alexeybond.gdx_commons.game.Entity;
import com.github.alexeybond.gdx_commons.game.systems.timing.TimingSystem;
import com.github.alexeybond.gdx_commons.ioc.IoC;
import com.github.alexeybond.gdx_commons.util.event.EventListener;
import com.github.alexeybond.gdx_commons.util.event.props.BooleanProperty;
import com.github.alexeybond.gdx_commons.util.event.props.FloatProperty;

/**
 *
 */
public class AttachedContinuousParticleEffect
        extends BaseRenderComponent implements EventListener<Component, BooleanProperty<Component>> {
    private final ParticleEffectPool effectPool;
    private final Vector2 relativePos;
    private final float relativeRotation;
    private final String enablePropertyName;

    private int enableSubIdx = -1, timeSubIdx = -1;

    private Array<ParticleEffectPool.PooledEffect> preCompletedEffects
            = new Array<ParticleEffectPool.PooledEffect>(false, 4);
    private ParticleEffectPool.PooledEffect effect;

    private BooleanProperty<Component> enableProperty;
    private FloatProperty<TimingSystem> deltaTimeEvent;

    private final Vector2 tmp = new Vector2();

    private float lastDeltaTime;

    public AttachedContinuousParticleEffect(
            String passName,
            ParticleEffectPool effectPool,
            Vector2 relativePos,
            float relativeRotation, String enablePropertyName) {
        super(passName);
        this.effectPool = effectPool;
        this.relativePos = relativePos;
        this.relativeRotation = relativeRotation;
        this.enablePropertyName = enablePropertyName;
    }

    public AttachedContinuousParticleEffect(
            String passName,
            String effectName,
            Vector2 relativePos,
            float relativeRotation,
            String enablePropertyName) {
        this(
                passName,
                IoC.<ParticleEffectPool>resolve("get particle pool", effectName),
                relativePos,
                relativeRotation,
                enablePropertyName);
    }

    @Override
    public void draw(DrawingContext context) {
        Batch batch = context.state().beginBatch();

        if (null != effect) {
            setupEffectTransform();
            effect.draw(batch, lastDeltaTime);

            if (effect.isComplete()) {
                effect.free();
                effect = null;
            }
        }

        for (int i = 0; i < preCompletedEffects.size; i++) {
            ParticleEffectPool.PooledEffect effect = preCompletedEffects.get(i);
            effect.draw(batch, lastDeltaTime);
            if (effect.isComplete()) {
                preCompletedEffects.removeIndex(i);
                --i;
                effect.free();
            }
        }

        lastDeltaTime = 0;
    }

    @Override
    public void onConnect(Entity entity) {
        super.onConnect(entity);

        enableProperty = entity
                .events()
                .event(enablePropertyName, BooleanProperty.<Component>make(true));
        deltaTimeEvent = entity
                .game()
                .systems()
                .<TimingSystem>get("timing")
                .events()
                .event("deltaTime");

        enableSubIdx = enableProperty.subscribe(this);
        timeSubIdx = deltaTimeEvent.subscribe(new EventListener<TimingSystem, FloatProperty<TimingSystem>>() {
            @Override
            public boolean onTriggered(TimingSystem timingSystem, FloatProperty<TimingSystem> dt) {
                lastDeltaTime = dt.get();
                return true;
            }
        });

        if (enableProperty.get()) {
            startEffect();
        }
    }

    @Override
    public void onDisconnect(Entity entity) {
        super.onDisconnect(entity);

        enableSubIdx = enableProperty.unsubscribe(enableSubIdx);
        timeSubIdx = deltaTimeEvent.unsubscribe(timeSubIdx);

        if (null != effect) effect.free();
    }

    private void setupEffectTransform() {
        float entityRotation = rotation.get();
        float totalRotation = relativeRotation + entityRotation;
        Vector2 pos = tmp.set(relativePos).rotate(entityRotation).add(position.ref());

        Array<ParticleEmitter> emitters = effect.getEmitters();

        for (int i = 0; i < emitters.size; i++) {
            ParticleEmitter emitter = emitters.get(i);

            emitter.setPosition(pos.x, pos.y);
            patchEmitterParameter(emitter.getAngle(), totalRotation);
        }
    }

    private void patchEmitterParameter(ParticleEmitter.ScaledNumericValue param, float targetValue) {
        float delta = (param.getHighMax() - param.getHighMin()) * .5f;
        param.setHigh(targetValue - delta, targetValue + delta);
        delta = (param.getLowMax() - param.getLowMin()) * .5f;
        param.setLow(targetValue - delta, targetValue + delta);
    }

    private void stopEffect() {
        if (null != effect) {
            effect.allowCompletion();
            preCompletedEffects.add(effect);
            effect = null;
        }
    }

    private void startEffect() {
        effect = effectPool.obtain();
        effect.reset();
    }

    @Override
    public boolean onTriggered(Component component, BooleanProperty<Component> event) {
        if (event.get()) {
            startEffect();
        } else {
            stopEffect();
        }

        return true;
    }
}
