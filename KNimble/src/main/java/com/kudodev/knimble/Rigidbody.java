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
package com.kudodev.knimble;

import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class Rigidbody {

    private final Transform transform;

    private Vector3f velocity = new Vector3f(0);

    public Rigidbody() {
        this.transform = new Transform();
    }

    public Rigidbody(Transform transform) {
        this.transform = transform;
    }

    public void integrate(float delta) {
        transform.position.fma(delta, velocity);
        transform.setDirty();
    }

    public Transform getTransform() {
        return transform;
    }

    public Vector3f getPosition() {
        return transform.position;
    }

    public void setPosition(Vector3f position) {
        transform.position.set(position);
        transform.setDirty();
    }

    public void setPosition(float x, float y, float z) {
        transform.position.set(x, y, z);
        transform.setDirty();
    }

    // should return new vec3?
    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity.set(velocity);
    }

    public void setVelocity(float x, float y, float z) {
        this.velocity.set(x, y, z);
    }
}
