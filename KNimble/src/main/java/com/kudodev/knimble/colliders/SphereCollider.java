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
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class SphereCollider extends Collider {

    protected float radius = .5f;

    public SphereCollider(Rigidbody rigidbody) {
        this(rigidbody, 0.5f);
    }

    public SphereCollider(Rigidbody rigidbody, float radius) {
        super(ColliderType.SPHERE, rigidbody);
        this.radius = radius;
    }

    @Override
    public boolean intersectsWith(SphereCollider other) {
        return Intersectionf.testSphereSphere(
                transform.getWorldPosition(), radius,
                other.transform.getWorldPosition(), other.radius);
    }

    @Override
    public boolean intersectsWith(BoxCollider other) {

        return false;
    }

}
