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
import com.kudodev.knimble.contact.Contact;
import com.kudodev.knimble.contact.ContactCache;
import org.joml.Intersectionf;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 *
 * @author IkeOTL
 */
public class SphereCollider extends Collider {

    public SphereCollider(Rigidbody rigidbody) {
        this(rigidbody, 0.5f);
    }

    public SphereCollider(Rigidbody rigidbody, float radius) {
        super(ColliderType.SPHERE, rigidbody);
        transform.setScale(radius * 2, radius * 2, radius * 2);
        updateInertiaTensor();
    }

    public float getRadius() {
        return .5f * transform.getWorldScale().x;
    }

    @Override
    public void updateInertiaTensor() {
        if (rigidbody == null) {
            return;
        }

        float radius = getRadius();
        float moment = .4f * rigidbody.getMass() * (radius * radius);
        Matrix3f inertiaTensor = new Matrix3f();
        inertiaTensor.identity();
        inertiaTensor.m00 = moment;
        inertiaTensor.m11 = moment;
        inertiaTensor.m22 = moment;
        rigidbody.setInertiaTensor(inertiaTensor);
    }

    @Override
    public boolean intersectsWith(SphereCollider other) {
        return Intersection.getDistanceSq(this, other) <= 0;
    }

    @Override
    public boolean intersectsWith(BoxCollider other) {
        float radius = getRadius();
        return Intersection.getDistanceSq(other, transform.getWorldPosition())
                <= radius * radius;
    }

    @Override
    public boolean intersectsWith(CapsuleCollider other) {
        return false;
    }

    @Override
    public void createCollision(CapsuleCollider other, ContactCache contactCache) {

    }

    @Override
    public void createCollision(SphereCollider other, ContactCache contactCache) {
        Contact contact = contactCache.getContact();

        Vector3f pos0 = transform.getWorldPosition();
        Vector3f pos1 = other.transform.getWorldPosition();

        contact.penetration = -Intersection.getDistance(this, other);
        contact.contactNormal.set(pos0).sub(pos1).normalize();
        contact.contactPoint.set(contact.contactNormal).mul(getRadius()).add(pos1);

        contact.setBodyData(this.rigidbody, other.rigidbody);
    }

    @Override
    public void createCollision(BoxCollider other, ContactCache contactCache) {
        Contact contact = contactCache.getContact();

        Vector3f closestPoint = new Vector3f();
        Vector3f pos = transform.getWorldPosition();
        float radius = getRadius();
        contact.penetration = radius * radius - Intersection.getDistanceSq(other, pos, closestPoint);

        contact.contactNormal.set(pos).sub(closestPoint).normalize();
        contact.contactPoint.set(closestPoint);
        contact.setBodyData(this.rigidbody, other.rigidbody);
    }

}
