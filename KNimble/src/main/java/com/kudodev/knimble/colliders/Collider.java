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
import com.kudodev.knimble.RigidbodyTransform;
import com.kudodev.knimble.contact.ContactCache;
import com.kudodev.knimble.Transform;

/**
 *
 * @author KudoD
 */
public abstract class Collider {

    public enum ColliderType {
        SPHERE, CUBE, CAPSULE
    };

    private final ColliderType type;
    private final Rigidbody rigidbody;
    private final Transform transform;

    public Collider(ColliderType type, RigidbodyTransform transform) {
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
            this.transform = new RigidbodyTransform();
        }
    }

    public Collider(ColliderType type) {
        this(type, new RigidbodyTransform());
    }

    public Rigidbody getRigidbody() {
        return rigidbody;
    }

    public Transform getTransform() {
        return transform;
    }

    public ColliderType getType() {
        return type;
    }

    public boolean intersectsWith(Collider other) {
        switch (other.type) {
            case CUBE:
                return intersectsWith((BoxCollider) other);
            case SPHERE:
                return intersectsWith((SphereCollider) other);
            case CAPSULE:
                return intersectsWith((CapsuleCollider) other);
            default:
                return false;
        }
    }

    public void createCollision(Collider other, ContactCache contactCache) {
        switch (other.type) {
            case CUBE:
                createCollision((BoxCollider) other, contactCache);
                break;
            case SPHERE:
                createCollision((SphereCollider) other, contactCache);
                break;
            case CAPSULE:
                createCollision((CapsuleCollider) other, contactCache);
                break;
            default:
                break;
        }
    }

    public abstract void updateInertiaTensor();

    public abstract boolean intersectsWith(SphereCollider other);

    public abstract boolean intersectsWith(BoxCollider other);

    public abstract boolean intersectsWith(CapsuleCollider other);

    public abstract void createCollision(SphereCollider other, ContactCache contactCache);

    public abstract void createCollision(BoxCollider other, ContactCache contactCache);

    public abstract void createCollision(CapsuleCollider other, ContactCache contactCache);
}
