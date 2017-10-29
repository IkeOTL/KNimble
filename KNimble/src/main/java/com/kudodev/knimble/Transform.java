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

    protected final Vector3f position;
    protected final Vector3f scale;
    protected final Quaternionf rotation;

    protected Vector3f worldPosition = new Vector3f(0);
    protected Vector3f worldScale = new Vector3f(1);
    protected Quaternionf worldRotation = new Quaternionf();

    protected Transform parent = null;
    protected final Matrix4f transMatrix = new Matrix4f();
    protected boolean dirty;

    public Transform() {
        this(new Vector3f(0), new Vector3f(1), new Quaternionf());
    }

    public Transform(Vector3f p) {
        this(p, new Vector3f(1), new Quaternionf());
    }

    public Transform(Vector3f p, Vector3f s, Quaternionf r) {
        position = p;
        scale = s;
        rotation = r;

        setDirty(true);
    }

    public Matrix4f getTransMatrix() {
        updateTransform();
        return transMatrix;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean b) {
        dirty = b;
    }

    public Transform getParent() {
        return parent;
    }

    public Transform getRootParent() {
        if (parent == null) {
            return this;
        }
        return parent.getRootParent();
    }

    public void setParent(Transform parent) {
        this.parent = parent;
        updateTransform(true);
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        setDirty(true);
    }

    public Vector3f getLocalPosition() {
        return position;
    }

    public Quaternionf getLocalRotation() {
        return rotation;
    }

    public Vector3f getLocalScale() {
        return scale;
    }

    public void rotate(float f, Vector3f v) {
        rotation.rotateAxis(f, v);
        setDirty(true);
    }

    public Vector3f getWorldPosition() {
        updateTransform();
        return worldPosition;
    }

    public Quaternionf getWorldRotation() {
        updateTransform();
        return worldRotation;
    }

    public Vector3f getWorldScale() {
        updateTransform();
        return worldScale;
    }

    public void updateTransform() {
        updateTransform(false);
    }

    public void updateTransform(boolean force) {
        if (!dirty && !force) {
            return;
        }
        dirty = false;

        if (parent != null) {
            updateTransform(parent);
            return;
        }

        worldPosition.set(position);
        worldRotation.set(rotation);
        worldScale.set(scale);

        transMatrix.translationRotateScale(worldPosition, worldRotation, worldScale);
    }

    public void updateTransform(Transform parent) {
        worldPosition.set(position);
        worldPosition.mulPosition(parent.getTransMatrix());

        worldRotation.set(parent.getWorldRotation());
        worldRotation.mul(rotation);

        worldScale.set(scale);
        Vector3f s = parent.getWorldScale();
        if (s != null) {
            worldScale.mul(s);
        }

        transMatrix.translationRotateScale(worldPosition, worldRotation, worldScale);
    }

}
