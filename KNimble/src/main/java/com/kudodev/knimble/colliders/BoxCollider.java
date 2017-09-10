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
import org.joml.Intersectionf;
import org.joml.Matrix3f;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class BoxCollider extends Collider {

    private final Vector3f halfExtents;

    public BoxCollider(Rigidbody rigidbody, Vector3f halfExtents) {
        super(ColliderType.CUBE, rigidbody);
        this.halfExtents = halfExtents;
    }

    public BoxCollider(Rigidbody rigidbody) {
        this(rigidbody, new Vector3f(.5f));
    }

    public BoxCollider(Vector3f halfExtents) {
        this(null, halfExtents);
    }

    @Override
    public boolean intersectsWith(SphereCollider other) {
        return false;
    }

    @Override
    public boolean intersectsWith(BoxCollider other) {
        Matrix3f m0 = transform.getWorldRotation().get(new Matrix3f());
        Vector3f v0 = transform.getWorldPosition();

        Matrix3f m1 = other.transform.getWorldRotation().get(new Matrix3f());
        Vector3f v1 = other.transform.getWorldPosition();

        return Intersectionf.testObOb(v0.x, v0.y, v0.z, m0.m00, m0.m01, m0.m02, m0.m10, m0.m11, m0.m12, m0.m20, m0.m21, m0.m22, halfExtents.x, halfExtents.y, halfExtents.z,
                v1.x, v1.y, v1.z, m1.m00, m1.m01, m1.m02, m1.m10, m1.m11, m1.m12, m1.m20, m1.m21, m1.m22, other.halfExtents.x, other.halfExtents.y, other.halfExtents.z);
    }

}
