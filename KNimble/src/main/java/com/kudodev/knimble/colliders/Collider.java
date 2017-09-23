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
import com.kudodev.knimble.contact.ContactCache;

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
        if (rigidbody != null) {
            this.transform = rigidbody.getTransform();
        } else {
            this.transform = new Transform();
        }
    }

    public Collider(ColliderType type) {
        this(type, new Transform());
    }

    public Rigidbody getRigidbody() {
        return rigidbody;
    }

    public Transform getTransform() {
        return transform;
    }

    public boolean intersectsWith(Collider other) {
        if (other.type == ColliderType.CUBE) {
            return intersectsWith((BoxCollider) other);
        } else if (other.type == ColliderType.SPHERE) {
            return intersectsWith((SphereCollider) other);
        }
        return false;
    }

    public void createCollision(Collider other, ContactCache contactCache) {
        if (other.type == ColliderType.CUBE) {
            createCollision((BoxCollider) other, contactCache);
        } else if (other.type == ColliderType.SPHERE) {
            createCollision((SphereCollider) other, contactCache);
        }
    }

    public abstract void updateInertiaTensor();

    public abstract boolean intersectsWith(SphereCollider other);

    public abstract boolean intersectsWith(BoxCollider other);

    public abstract void createCollision(SphereCollider other, ContactCache contactCache);

    public abstract void createCollision(BoxCollider other, ContactCache contactCache);
}
