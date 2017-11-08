package io.github.alexeybond.partly_solid_bicycle.game3d.interfaces.transform;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public interface Transform3D {
    Vector3 getPositionRef();

    Vector3 getScaleRef();

    Quaternion getRotationRef();

    Matrix4 readMatrix(Matrix4 dst);
}
