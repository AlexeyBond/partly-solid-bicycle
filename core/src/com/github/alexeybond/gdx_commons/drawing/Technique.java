package com.github.alexeybond.gdx_commons.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Predicate;
import com.github.alexeybond.gdx_commons.drawing.rt.FboTarget;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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

                if (out.getPixelsWidth() != savedW || out.getPixelsHeight() != savedH) {
                    savedW = out.getPixelsWidth();
                    savedH = out.getPixelsHeight();
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
     * Draw a pass again.
     */
    protected Runnable repeatPass(String name) {
        return scene.getPass(name);
    }

    /**
     * Clear color buffer.
     */
    protected Runnable clearColor() {
        return new Runnable() {
            @Override
            public void run() {
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            }
        };
    }

    protected Runnable clearStencil() {
        return new Runnable() {
            @Override
            public void run() {
                Gdx.gl.glClearStencil(0);
                Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT);
            }
        };
    }

    /**
     * Draw to stencil buffer only. All drawn geometry will set stencil buffer to 1.
     */
    protected Runnable toStencil(final Runnable drawing) {
        final DrawingState state = scene.context().state();
        return new Runnable() {
            @Override
            public void run() {
                state.flush();

                GL20 gl = Gdx.gl;
                gl.glEnable(GL20.GL_STENCIL_TEST);
                gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0x1);
                gl.glStencilOp(GL20.GL_REPLACE, GL20.GL_REPLACE, GL20.GL_REPLACE);
                gl.glStencilMask(0xFF);
                gl.glColorMask(false, false, false, false);

                try {
                    drawing.run();
                    state.flush();
                } finally {
                    gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);
                    gl.glColorMask(true, true, true, true);
                    gl.glDisable(GL20.GL_STENCIL_TEST);
                }
            }
        };
    }

    /**
     * Enable stencil test for a drawable. Test will be passed when stencil value is non equal to zero.
     */
    protected Runnable withStencilTest(final Runnable drawing) {
        final DrawingState state = scene.context().state();
        return new Runnable() {
            @Override
            public void run() {
                state.flush();

                GL20 gl = Gdx.gl;
                gl.glStencilFunc(GL20.GL_NOTEQUAL, 0, 0xFF);
                gl.glEnable(GL20.GL_STENCIL_TEST);

                try {
                    drawing.run();
                    state.flush();
                } finally {
                    gl.glDisable(GL20.GL_STENCIL_TEST);
                }
            }
        };
    }

    /**
     * Draw a circle with center in (0,0)
     */
    protected Runnable circle(final float radius, final int segments) {
        final DrawingState state = scene.context().state();
        return new Runnable() {
            @Override
            public void run() {
                ShapeRenderer shapeRenderer = state.beginFilled();

                shapeRenderer.getProjectionMatrix().idt();
                shapeRenderer.updateMatrices();

                shapeRenderer.circle(0, 0, radius, segments);
            }
        };
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
     * Draw another scene to current target.
     */
    protected Runnable include(final Scene included) {
        return new Runnable() {
            @Override
            public void run() {
                included.context().setOutputTarget(scene.context().getCurrentRenderTarget());
                included.draw();
            }
        };
    }

    protected Runnable pushingProjection(final Runnable run) {
        final Matrix4 projectionBackup = new Matrix4();
        return new Runnable() {
            @Override
            public void run() {
                DrawingState state = scene.context().state();
                state.getProjection(projectionBackup);
                run.run();
                state.setProjection(projectionBackup);
            }
        };
    }

    protected Runnable withProjection(final ProjectionMode mode) {
        final Matrix4 tmp = new Matrix4();

        return new Runnable() {
            @Override
            public void run() {
                DrawingContext context = scene.context();
                RenderTarget target = context.getCurrentRenderTarget();
                mode.setup(tmp, target.width(), target.height());
                context.state().setProjection(tmp);
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
                                    out.getPixelsWidth(),
                                    out.getPixelsHeight(),
                                    false)));
                }
            }
        };
    }
}
