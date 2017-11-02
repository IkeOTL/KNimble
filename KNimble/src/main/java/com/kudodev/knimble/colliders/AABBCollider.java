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

import com.kudodev.knimble.JOMLExtra;
import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.RigidbodyTransform;
import com.kudodev.knimble.Transform;
import com.kudodev.knimble.contact.ContactCache;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class AABBCollider extends Collider {

    private final Vector3f defaultExtents = new Vector3f();
    private final Vector3f worldMinimum = new Vector3f();
    private final Vector3f worldMaximum = new Vector3f();

    public AABBCollider(RigidbodyTransform transform, Vector3f extents) {
        super(ColliderType.AABB, transform);
        this.defaultExtents.set(extents);
        recalcuateBounds();
    }

    @Override
    public void recalcuateBounds() {
        worldMinimum.set(defaultExtents).negate();
        worldMaximum.set(defaultExtents);
        getTransform().getTransMatrix().transformAab(worldMinimum, worldMaximum, worldMinimum, worldMaximum);
    }

    @Override
    public void createBoundingCollider() {
    }

    public Vector3f getWorldMinimum() {
        return worldMinimum;
    }

    public Vector3f getWorldMaximum() {
        return worldMaximum;
    }

    protected Vector3f getDefaultExtents() {
        return defaultExtents;
    }

    @Override
    public void updateInertiaTensor() {
    }

    @Override
    public boolean intersectsWith(AABBCollider other) {
        return !((worldMaximum.x < other.worldMinimum.x || worldMinimum.x > other.worldMaximum.x)
                || (worldMaximum.y < other.worldMinimum.y || worldMinimum.y > other.worldMaximum.y)
                || (worldMaximum.z < other.worldMinimum.z || worldMinimum.z > other.worldMaximum.z));
    }

    @Override
    public boolean intersectsWith(CapsuleCollider other) {
        return false;
    }

    @Override
    public void createCollision(CapsuleCollider other, ContactCache contactCache) {
    }

    @Override
    public boolean intersectsWith(SphereCollider other) {
        return false;
    }

    @Override
    public void createCollision(SphereCollider other, ContactCache contactCache) {
    }

    @Override
    public boolean intersectsWith(BoxCollider other) {
        return false;
    }

    @Override
    public void createCollision(BoxCollider other, ContactCache contactCache) {
    }
}
