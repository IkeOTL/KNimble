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

import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class BasicTransform extends Transform {

    private final Vector3f position;
    private final Vector3f scale;
    private final Quaternionf rotation;

    private final Vector3f worldPosition = new Vector3f(0);
    private final Vector3f worldScale = new Vector3f(1);
    private final Quaternionf worldRotation = new Quaternionf();

    public BasicTransform() {
        this(new Vector3f(0), new Vector3f(1), new Quaternionf());
    }

    public BasicTransform(Vector3f p) {
        this(p, new Vector3f(1), new Quaternionf());
    }

    public BasicTransform(Vector3f p, Vector3f s, Quaternionf r) {
        position = p;
        scale = s;
        rotation = r;

        setDirty();
        updateTransform();
    }

    public void setPosition(float x, float y, float z) {
        position.set(x, y, z);
        setDirty();
    }

    @Override
    public Vector3f getLocalPosition() {
        return position;
    }

    @Override
    public Quaternionf getLocalRotation() {
        return rotation;
    }

    @Override
    public Vector3f getLocalScale() {
        return scale;
    }

    public void rotate(float f, Vector3f v) {
        rotation.rotateAxis(f, v);
        setDirty();
    }

    @Override
    public Vector3f getWorldPosition() {
        updateTransform();
        return worldPosition;
    }

    @Override
    public Quaternionf getWorldRotation() {
        updateTransform();
        return worldRotation;
    }

    @Override
    public Vector3f getWorldScale() {
        updateTransform();
        return worldScale;
    }

    @Override
    protected void updateTransform() {
        if (!isDirty) {
            return;
        }
        isDirty = false;

        worldPosition.set(position);
        worldRotation.set(rotation);
        worldScale.set(scale);

        transMatrix.translationRotateScale(worldPosition, worldRotation, worldScale);

    }

    @Override
    protected void updateTransform(Transform parent) {
        if (!isDirty) {
            return;
        }
        isDirty = false;

        worldPosition.set(position);
        worldPosition.mulPosition(parent.getTransMatrix());

        worldRotation.set(parent.getWorldRotation());
        worldRotation.mul(rotation);

        worldScale.set(parent.getWorldScale());
        worldScale.mul(scale);

        transMatrix.translationRotateScale(worldPosition, worldRotation, worldScale);

        parent.getTransMatrix().mul(transMatrix, transMatrix);
    }
}
