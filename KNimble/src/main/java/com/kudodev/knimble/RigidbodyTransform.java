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
public class RigidbodyTransform extends Transform {

    private Rigidbody rigidbody;

    public RigidbodyTransform() {
        this(new Vector3f(0), new Quaternionf());
    }

    public RigidbodyTransform(Vector3f p) {
        this(p, new Quaternionf());
    }

    public RigidbodyTransform(Vector3f p, Quaternionf r) {
        // Rigidbodies have no need for scale
        super(p, null, r);
        worldScale = null;
    }

    public Rigidbody getRigidbody() {
        return rigidbody;
    }

    public void setRigidbody(Rigidbody rigidbody) {
        this.rigidbody = rigidbody;
    }

    @Override
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

        transMatrix.translationRotateScale(
                worldPosition.x(), worldPosition.y(), worldPosition.z(),
                worldRotation.x(), worldRotation.y(), worldRotation.z(), worldRotation.w(),
                1, 1, 1); // scaled mat4s ruin collision math
    }

    @Override
    public void updateTransform(Transform parent) {
        worldPosition.set(position);
        worldPosition.mulPosition(parent.getTransMatrix());

        worldRotation.set(parent.getWorldRotation());
        worldRotation.mul(rotation);

        transMatrix.translationRotateScale(
                worldPosition.x(), worldPosition.y(), worldPosition.z(),
                worldRotation.x(), worldRotation.y(), worldRotation.z(), worldRotation.w(),
                1, 1, 1); // scaled mat4s ruin collision math
    }
}
