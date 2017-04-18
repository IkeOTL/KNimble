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

/**
 *
 * @author KudoD
 */
public abstract class Collider {

    protected final Rigidbody rigidbody;

    public Collider(Rigidbody rigidbody) {
        this.rigidbody = rigidbody;
        rigidbody.setCollider(this);
    }

    public boolean intersectsWith(Collider other) {
        if (other instanceof AABBCollider) {
            return intersectsWith((AABBCollider) other);
        } else if (other instanceof SphereCollider) {
            return intersectsWith((SphereCollider) other);
        }
        return false;
    }

    public abstract boolean intersectsWith(SphereCollider other);

    public abstract boolean intersectsWith(AABBCollider other);
}
