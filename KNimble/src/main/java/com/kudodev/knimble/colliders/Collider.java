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
import com.kudodev.knimble.contact.Contact;

/**
 *
 * @author KudoD
 */
public abstract class Collider {

    public enum ColliderType {
        SPHERE, CUBE
    };

    protected final ColliderType type;
    protected final Rigidbody rigidbody;
    protected final Transform transform;

    public Collider(ColliderType type, Transform transform) {
        this.type = type;
        rigidbody = null;
        this.transform = transform;
    }

    public Collider(ColliderType type, Rigidbody rigidbody) {
        this.type = type;
        this.rigidbody = rigidbody;
        this.transform = rigidbody.getTransform();
    }

    public Rigidbody getRigidbody() {
        return rigidbody;
    }

    public Transform getTransform() {
        return transform;
    }

    public Contact intersectsWith(Collider other) {
        if (other.type == ColliderType.CUBE) {
            return intersectsWith((CubeCollider) other);
        } else if (other.type == ColliderType.SPHERE) {
            return intersectsWith((SphereCollider) other);
        }
        return null;
    }

    public abstract Contact intersectsWith(SphereCollider other);

    public abstract Contact intersectsWith(CubeCollider other);
}
