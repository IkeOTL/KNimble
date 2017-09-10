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
public class Transform {

    protected Transform parent = null;

    public final Vector3f position;
    public final Vector3f scale;
    public final Quaternionf rotation;

    private final Vector3f worldPosition = new Vector3f();
    private final Vector3f worldScale = new Vector3f();
    private final Quaternionf worldRotation = new Quaternionf();

    private boolean isDirty;
    private final Matrix4f transMatrix = new Matrix4f();

    public Transform() {
        this(new Vector3f(0), new Quaternionf(), new Vector3f(1));
    }

    public Transform(Transform t) {
        this(new Vector3f(t.position), new Quaternionf(t.rotation), new Vector3f(t.scale));
    }

    public Transform(Vector3f p) {
        this(p, new Quaternionf(), new Vector3f(1));
    }

    public Transform(Vector3f p, Quaternionf r, Vector3f s) {
        position = p;
        rotation = r;
        scale = s;
        updateTransMatrix();
    }

    public Matrix4f getTransMatrix() {
        if (isDirty) {
            updateTransMatrix();
            isDirty = false;
        }
        return transMatrix;
    }

    public void setDirty() {
        isDirty = true;
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        worldPosition.set(x, y, z);
        isDirty = true;
    }

    public void setScale(float x, float y, float z) {
        scale.set(x, y, z);
        worldScale.set(x, y, z);
        isDirty = true;
    }

    public void rotate(float f, Vector3f v) {
        rotation.rotateAxis(f, v);
        worldRotation.set(rotation);
        isDirty = true;
    }

    public Vector3f getWorldPosition() {
        if (isDirty) {
            updateTransMatrix();
            isDirty = false;
        }
        return worldPosition;
    }

    public Vector3f getWorldScale() {
        if (isDirty) {
            updateTransMatrix();
            isDirty = false;
        }
        return worldScale;
    }

    public Quaternionf getWorldRotation() {
        if (isDirty) {
            updateTransMatrix();
            isDirty = false;
        }
        return worldRotation;
    }

    public void setParent(Transform parent) {
        this.parent = parent;
    }

    private void updateTransMatrix() {
        if (parent != null) {
            worldPosition.set(position);
            worldPosition.mulPosition(parent.getTransMatrix());

            worldRotation.set(parent.worldRotation);
            worldRotation.mul(rotation);

            worldScale.set(parent.worldScale);
            worldScale.mul(scale);

            // TODO improve?            
            transMatrix.translationRotateScale(worldPosition, worldRotation, worldScale);
            parent.getTransMatrix().mul(transMatrix, transMatrix);
//            transMatrix.set(parent.getTransMatrix()).mul(new Matrix4f().translationRotateScale(worldPosition, worldRotation, worldScale));
        } else {
            worldPosition.set(position);
            worldRotation.set(rotation);
            worldScale.set(scale);
            transMatrix.translationRotateScale(worldPosition, worldRotation, worldScale);
        }
    }
}
