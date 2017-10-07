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
public abstract class Transform {

    protected boolean isDirty;
    protected final Matrix4f transMatrix = new Matrix4f();

    public Matrix4f getTransMatrix() {
        updateTransform();
        return transMatrix;
    }

    public void setDirty() {
        isDirty = true;
    }

    public abstract Vector3f getLocalPosition();

    public abstract Vector3f getLocalScale();

    public abstract Quaternionf getLocalRotation();

    public abstract Vector3f getWorldPosition();

    public abstract Vector3f getWorldScale();

    public abstract Quaternionf getWorldRotation();

    protected abstract void updateTransform();

    protected abstract void updateTransform(Transform t);

}
