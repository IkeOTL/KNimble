/*
 * Copyright 2017 KudoDev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kudodev.knimble;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class RigidbodyTransform {

    private RigidbodyTransform parent = null;

    private final Vector3f position;
    private final Quaternionf rotation;

    private final Vector3f worldPosition = new Vector3f(0);
    private final Quaternionf worldRotation = new Quaternionf();

    private boolean isDirty;
    private final Matrix4f transMatrix = new Matrix4f();

    public RigidbodyTransform() {
        this(new Vector3f(0), new Quaternionf());
    }

    public RigidbodyTransform(RigidbodyTransform t) {
        this(new Vector3f(t.position), new Quaternionf(t.rotation));
    }

    public RigidbodyTransform(Vector3f p) {
        this(p, new Quaternionf());
    }

    public RigidbodyTransform(Vector3f p, Quaternionf r) {
        position = p;
        rotation = r;
        updateTransform();
    }

    public Matrix4f getTransMatrix() {
        if (isDirty) {
            updateTransform();
            isDirty = false;
        }
        return transMatrix;
    }

    public void setDirty() {
        isDirty = true;
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        isDirty = true;
    }

    public Vector3f getLocalPosition() {
        return position;
    }

    public Quaternionf getLocalRotation() {
        return rotation;
    }

    public void rotate(float f, Vector3f v) {
        rotation.rotateAxis(f, v);
        isDirty = true;
    }

    public Vector3f getWorldPosition() {
        if (isDirty) {
            updateTransform();
            isDirty = false;
        }
        return worldPosition;
    }

    public Quaternionf getWorldRotation() {
        if (isDirty) {
            updateTransform();
            isDirty = false;
        }
        return worldRotation;
    }

    public void setParent(RigidbodyTransform parent) {
        this.parent = parent;
    }

    private void updateTransform() {
        if (parent != null) {
            worldPosition.set(position);
            worldPosition.mulPosition(parent.getTransMatrix());

            worldRotation.set(parent.worldRotation);
            worldRotation.mul(rotation);

//            worldScale.set(parent.worldScale);
//            worldScale.mul(scale);
            transMatrix.translationRotateScale(
                    worldPosition.x(), worldPosition.y(), worldPosition.z(),
                    worldRotation.x(), worldRotation.y(), worldRotation.z(), worldRotation.w(),
                    1, 1, 1); // scaled mat4s ruin collision math

            parent.getTransMatrix().mul(transMatrix, transMatrix);
        } else {
            worldPosition.set(position);
            worldRotation.set(rotation);
//            worldScale.set(scale);

            transMatrix.translationRotateScale(
                    worldPosition.x(), worldPosition.y(), worldPosition.z(),
                    worldRotation.x(), worldRotation.y(), worldRotation.z(), worldRotation.w(),
                    1, 1, 1); // scaled mat4s ruin collision math
        }
    }
}
