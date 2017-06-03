package com.github.alexeybond.gdx_commons.drawing.projection;

import com.badlogic.gdx.math.Matrix4;
import com.github.alexeybond.gdx_commons.drawing.ProjectionMode;

/**
 *
 */
public enum OrthoProjection implements ProjectionMode {
    UNIT {
        @Override
        public void setup(Matrix4 projectionMatrix, float targetWidth, float targetHeight) {
            projectionMatrix.setToOrtho2D(0,0,1,1);
        }
    },
    UNIT_PN {
        @Override
        public void setup(Matrix4 projectionMatrix, float targetWidth, float targetHeight) {
            projectionMatrix.setToOrtho2D(-1,-1,1,1);
        }
    },
    SCREEN {
        @Override
        public void setup(Matrix4 projectionMatrix, float targetWidth, float targetHeight) {
            projectionMatrix.setToOrtho2D(0,0,targetWidth,targetHeight);
        }
    },
    SCREEN_MID {
        @Override
        public void setup(Matrix4 projectionMatrix, float targetWidth, float targetHeight) {
            float hw = 0.5f * targetWidth;
            float hh = 0.5f * targetHeight;
            projectionMatrix.setToOrtho2D(-hw,-hh,hw,hh);
        }
    }
}
