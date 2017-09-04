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

    private float mass = 0;
    private float inverseMass = 0;

    private boolean awake = true;

    private Vector3f angularVelocity = new Vector3f(0);
    private Vector3f linearVelocity = new Vector3f(0);

    public Rigidbody() {
        this.transform = new Transform();
    }

    public Rigidbody(Transform transform) {
        this.transform = transform;
    }

    public void integrate(float delta) {
        transform.position.fma(delta, linearVelocity);
        transform.setDirty();
    }

    public Transform getTransform() {
        return transform;
    }

    // should return new vec3?
    public Vector3f getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(Vector3f velocity) {
        this.linearVelocity.set(velocity);
    }

    public void setLinearVelocity(float x, float y, float z) {
        this.linearVelocity.set(x, y, z);
    }

    public void addLinearVelocity(Vector3f velocity) {
        this.linearVelocity.add(velocity);
    }

    public Vector3f getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(Vector3f velocity) {
        this.angularVelocity.set(velocity);
    }

    public void addAngularVelocity(Vector3f velocity) {
        this.angularVelocity.add(velocity);
    }

    public void setAngularVelocity(float x, float y, float z) {
        this.angularVelocity.set(x, y, z);
    }

    public void setMass(float mass) {
        inverseMass = mass > 0 ? 1 / mass : 0;
        this.mass = mass;
    }

    public float getMass() {
        return mass;
    }

    public float getInverseMass() {
        return inverseMass;
    }

    public boolean isAwake() {
        return awake;
    }

    public void setAwake(boolean awake) {
        this.awake = awake;
    }

}
