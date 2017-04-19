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
package com.kudodev.knimble.colliders;

import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.Transform;

/**
 *
 * @author KudoD
 */
public abstract class Collider {

    protected final Rigidbody rigidbody;

    protected final Transform transform;

    public Collider(Transform transform) {
        rigidbody = null;
        this.transform = transform;
    }

    public Collider(Rigidbody rigidbody) {
        this.transform = rigidbody.getTransform();
        this.rigidbody = rigidbody;
    }

    public Rigidbody getRigidbody() {
        return rigidbody;
    }

    public Transform getTransform() {
        return transform;
    }

    public boolean intersectsWith(Collider other) {
        if (other instanceof CubeCollider) {
            return intersectsWith((CubeCollider) other);
        } else if (other instanceof SphereCollider) {
            return intersectsWith((SphereCollider) other);
        }
        return false;
    }

    public abstract boolean intersectsWith(SphereCollider other);

    public abstract boolean intersectsWith(CubeCollider other);
}
