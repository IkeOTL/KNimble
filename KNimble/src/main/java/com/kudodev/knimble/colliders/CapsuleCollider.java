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
import org.joml.Vector3f;
import org.joml.Matrix3f;

/**
 *
 * @author IkeOTL
 */
public class CapsuleCollider extends Collider {

    private Vector3f startPoint, endPoint;
    private float length;
    private float radius;

    public CapsuleCollider(Rigidbody rigidbody, float length, float radius) {
        super(ColliderType.CAPSULE, rigidbody);
        this.radius = radius;

        this.length = length;
        this.startPoint = new Vector3f(0, -length * .5f, 0);
        this.endPoint = new Vector3f(0, length * .5f, 0);

        updateInertiaTensor();
    }

    @Override
    public void updateInertiaTensor() {
        if (rigidbody == null) {
            return;
        }

        Matrix3f inertiaTensor = new Matrix3f();
        float density = rigidbody.getMass() / (float) (Math.PI * (radius * radius) * (4 * radius / 3 + length));
        float cM; // cylinder mass
        float hsM; // mass of hemispheres
        float rSq = radius * radius;
        cM = (float) Math.PI * length * rSq * density;
        hsM = (float) (2f * Math.PI) * (1f / 3f) * rSq * radius * density;

        // from cylinder
        inertiaTensor.m11 = rSq * cM * 0.5f;
        inertiaTensor.m00 = inertiaTensor.m22 = inertiaTensor.m11 * 0.5f + cM * length * length * (1f / 12f);

        // from hemispheres
        float temp0 = hsM * 2.0f * rSq / 5.0f;
        inertiaTensor.m11 += temp0 * 2.0f;
        float temp1 = length * 0.5f;
        float temp2 = temp0 + hsM * (temp1 * temp1 + 3.0f * (1f / 8f) * length * radius);
        inertiaTensor.m00 += temp2 * 2.0f;
        inertiaTensor.m22 += temp2 * 2.0f;
        rigidbody.setInertiaTensor(inertiaTensor);
    }

    @Override
    public boolean intersectsWith(SphereCollider other) {
        Vector3f a = new Vector3f(startPoint);
        Vector3f b = new Vector3f(endPoint);
        transform.getTransMatrix().transformPosition(a);
        transform.getTransMatrix().transformPosition(b);

        Vector3f otherPos = other.transform.getWorldPosition();

        Vector3f closestPoint = Intersectionf.findClosestPointOnLineSegment(a.x, a.y, a.z, b.x, b.y, b.z, otherPos.x, otherPos.y, otherPos.z, new Vector3f());

        float distSq = closestPoint.distanceSquared(otherPos) - (other.radius + radius) * (other.radius + radius);

        return distSq <= 0;
    }

    @Override
    public void createCollision(SphereCollider other, ContactCache contactCache) {
        Vector3f a = new Vector3f(startPoint);
        Vector3f b = new Vector3f(endPoint);
        transform.getTransMatrix().transformPosition(a);
        transform.getTransMatrix().transformPosition(b);

        Vector3f otherPos = other.transform.getWorldPosition();

        Vector3f closestPoint = Intersectionf.findClosestPointOnLineSegment(a.x, a.y, a.z, b.x, b.y, b.z, otherPos.x, otherPos.y, otherPos.z, new Vector3f());

        Contact contact = contactCache.getContact();
        contact.penetration = -(closestPoint.distance(otherPos) - (other.radius + radius));
        contact.contactNormal.set(closestPoint).sub(otherPos).normalize();
        contact.contactPoint.set(contact.contactNormal).mul(radius).add(closestPoint);
        contact.setBodyData(this.rigidbody, other.rigidbody);
    }

    @Override
    public boolean intersectsWith(CapsuleCollider other) {
        return false;
    }

    @Override
    public void createCollision(CapsuleCollider other, ContactCache contactCache) {
    }

    @Override
    public boolean intersectsWith(BoxCollider other) {
        return false;
    }

    @Override
    public void createCollision(BoxCollider other, ContactCache contactCache) {

    }
}
