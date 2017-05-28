package com.github.alexeybond.gdx_commons.drawing;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Predicate;
import com.github.alexeybond.gdx_commons.drawing.rt.FboTarget;

/**
 *
 */
public class Technique {
    private Scene scene;

    public final Runnable initFor(Scene scene) {
        this.scene = scene;
        return build();
    }

    protected Runnable build() {
        return seq(
                toOutput(),
                pass("background"),
                pass("main"),
                pass("foreground")
        );
    }

    /**
     * Execute sequence of actions.
     */
    protected Runnable seq(final Runnable... seq) {
        return new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < seq.length; i++) {
                    seq[i].run();
                }
            }
        };
    }

    protected Runnable conditional(final Predicate<Scene> condition, final Runnable runnable) {
        return new Runnable() {
            @Override
            public void run() {
                if (condition.evaluate(scene)) {
                    runnable.run();
                }
            }
        };
    }

    /**
     * Execute some passes/actions only in frames after output target resize and in the first frame.
     *
     * <pre>
     *     seq(
     *       onResize(seq(
     *         releaseTargets("temp-1", "temp-2", "temp-3"),
     *         allocScreenSizeTargets("temp-1", "temp-2", "temp-3"),
     *         ... // Clear/fill intermediate targets
     *       )),
     *       ...
     *     )
     * </pre>
     */
    protected Runnable onResize(final Runnable runnable) {
        return conditional(new Predicate<Scene>() {
            private int savedW = 0, savedH = 0;

            @Override
            public boolean evaluate(Scene scene) {
                RenderTarget out = scene.context().getOutputTarget();

                if (out.width() != savedW || out.height() != savedH) {
                    savedW = out.width();
                    savedH = out.height();
                    return true;
                }

                return false;
            }
        }, runnable);
    }

    /**
     * Draw a pass.
     */
    protected Runnable pass(String name, PassController controller) {
        return scene.addPass(name, new Pass(scene.context(), controller));
    }

    /**
     * Draw a pass with default settings.
     */
    protected Runnable pass(String name) {
        return pass(name, new PassController());
    }

    /**
     * Start drawing to some target.
     *
     * @param name    name of render target slot or {@code null} for output target
     */
    protected Runnable toTarget(String name) {
        if (null == name) {
            return new Runnable() {
                @Override
                public void run() {
                    scene.context().renderTo(scene.context().getOutputTarget());
                }
            };
        } else {
            final TargetSlot slot = scene.context().getSlot(name);
            return new Runnable() {
                @Override
                public void run() {
                    scene.context().renderTo(slot);
                }
            };
        }
    }

    /**
     * Start drawing to the output target.
     */
    protected Runnable toOutput() {
        return toTarget(null);
    }

    /**
     * Swap contents of two render target slots.
     */
    protected Runnable swapTargets(String target1, String target2) {
        final TargetSlot slot1 = scene.context().getSlot(target1);
        final TargetSlot slot2 = scene.context().getSlot(target2);

        return new Runnable() {
            @Override
            public void run() {
                slot1.swap(slot2);
            }
        };
    }

    /**
     * Clear some render target slots.
     */
    protected Runnable releaseTargets(String... targets) {
        final TargetSlot[] slots = new TargetSlot[targets.length];

        for (int i = 0; i < targets.length; i++)
            slots[i] = scene.context().getSlot(targets[i]);

        return new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < slots.length; i++) {
                    slots[i].clear();
                }
            }
        };
    }

    /**
     * Create (or allocate from pool) render targets of the same size as output target and store in
     * slots with given names.
     */
    protected Runnable allocScrrenSizeTargets(String... targets) {
        final TargetSlot[] slots = new TargetSlot[targets.length];

        for (int i = 0; i < targets.length; i++)
            slots[i] = scene.context().getSlot(targets[i]);

        return new Runnable() {
            @Override
            public void run() {
                RenderTarget out = scene.context().getOutputTarget();
                // TODO:: Use pool
                for (int i = 0; i < slots.length; i++) {
                    slots[i].set(new FboTarget(
                            new FrameBuffer(
                                    Pixmap.Format.RGBA8888,
                                    out.width(),
                                    out.height(),
                                    false)));
                }
            }
        };
    }
}
