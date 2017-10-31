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

import com.kudodev.knimble.anchors.Constraint;
import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class Rigidbody {

    private final RigidbodyTransform transform;

    private final Vector3f angularVelocity = new Vector3f(0);
    private final Vector3f linearVelocity = new Vector3f(0);

    private final Vector3f angularAcceleration = new Vector3f(0);
    private final Vector3f linearAcceleration = new Vector3f(0);
    private final Vector3f lastFrameLinearAcceleration = new Vector3f(0);

    private final Vector3f angularForces = new Vector3f(0);
    private final Vector3f linearForces = new Vector3f(0);

    private Matrix3f inverseInertiaTensor = new Matrix3f();
    private Matrix3f inverseInertiaTensorWorld = new Matrix3f();

    private final List<Constraint> anchors = new ArrayList<>();

    private float mass = 0;
    private float inverseMass = 0;
    private float linearDamping = 0.99f;
    private float angularDamping = 0.8f;

    private float sleepEpsilon = .01f;
    private float motion = sleepEpsilon * 2f;
    private boolean canSleep = true;
    private boolean awake = true;

    // temp material data
    private float friction = .9f;
    private float restitution = .03f;

    public Rigidbody(RigidbodyTransform t, float mass) {
        this.transform = t;
        setMass(mass);
        t.setRigidbody(this);
    }

    public Rigidbody(float mass) {
        this(new RigidbodyTransform(), mass);
    }

    public void integrate(float delta) {
        if (!awake) {
            return;
        }

        for (int i = 0; i < anchors.size(); i++) {
            anchors.get(i).tick(this, delta);
        }

        // update linear velocity
        lastFrameLinearAcceleration.set(linearAcceleration);
        lastFrameLinearAcceleration.fma(inverseMass, linearForces);
        linearVelocity.fma(delta, lastFrameLinearAcceleration);
        linearForces.set(0);

        // update angular velocity
        inverseInertiaTensorWorld.transform(angularForces);
        angularVelocity.fma(inverseMass, angularForces);
        angularForces.set(0);

        // apply damping
        linearVelocity.mul((float) Math.pow(linearDamping, delta));
        angularVelocity.mul((float) Math.pow(angularDamping, delta));

        // apply to transform
        transform.getLocalPosition().fma(delta, linearVelocity);
        transform.getLocalRotation().integrate(delta, angularVelocity.x(), angularVelocity.y(), angularVelocity.z());
        transform.setDirty(true);

        calculateIITWorld();

        // make sleep
        if (canSleep) {
            float currentMotion = linearVelocity.dot(linearVelocity) + angularVelocity.dot(angularVelocity);
            float bias = (float) Math.pow(0.5f, delta);
            motion = bias * motion + (1 - bias) * currentMotion;
            if (motion < sleepEpsilon) {
                setAwake(false);
            } else if (motion > 10 * sleepEpsilon) {
                motion = 10 * sleepEpsilon;
            }
        }
    }

    public boolean isAwake() {
        return awake;
    }

    public void setAwake(boolean b) {
        if (b) {
            awake = true;
            // Add a bit of motion to avoid it falling asleep immediately. 
            motion = sleepEpsilon * 2.0f;
        } else {
            awake = false;
            linearVelocity.set(0, 0, 0);
            angularVelocity.set(0, 0, 0);
        }
    }

    public void addConstraint(Constraint c) {
        anchors.add(c);
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public Transform getTransform() {
        return transform;
    }

    public Vector3f getLinearForces() {
        return linearForces;
    }

    public Vector3f getAcceleration() {
        return linearAcceleration;
    }

    public Vector3f getLastFrameLinearAcceleration() {
        return lastFrameLinearAcceleration;
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

    public void addLinearVelocity(float x, float y, float z) {
        this.linearVelocity.add(x, y, z);
    }

    public Vector3f getLinearAcceleration() {
        return linearAcceleration;
    }

    public void setLinearAcceleration(Vector3f velocity) {
        this.linearAcceleration.set(velocity);
    }

    public void setLinearAcceleration(float x, float y, float z) {
        this.linearAcceleration.set(x, y, z);
    }

    public void addLinearAcceleration(Vector3f velocity) {
        this.linearAcceleration.add(velocity);
    }

    public void addLinearAcceleration(float x, float y, float z) {
        this.linearAcceleration.add(x, y, z);
    }

    public Vector3f getAngularAcceleration() {
        return angularAcceleration;
    }

    public void setAngularAcceleration(Vector3f velocity) {
        this.angularAcceleration.set(velocity);
    }

    public void setAngularAcceleration(float x, float y, float z) {
        this.angularAcceleration.set(x, y, z);
    }

    public void addAngularAcceleration(Vector3f velocity) {
        this.angularAcceleration.add(velocity);
    }

    public void addAngularAcceleration(float x, float y, float z) {
        this.angularAcceleration.add(x, y, z);
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
        inverseMass = mass > 0 ? 1f / mass : 0;
        this.mass = mass;
    }

    public float getMass() {
        return mass;
    }

    public float getInverseMass() {
        return inverseMass;
    }

    public void addLinearForce(Vector3f force) {
        linearForces.add(force);
        awake = true;
    }

    public void addLinearForce(float x, float y, float z) {
        linearForces.add(x, y, z);
        awake = true;
    }

    public void setInertiaTensor(Matrix3f i) {
        i.invert(inverseInertiaTensor);
        calculateIITWorld();
    }

    public Matrix3f getInverseInertiaTensorWorld() {
        return inverseInertiaTensorWorld;
    }

    public void calculateIITWorld() {
        if (getMass() <= 0) {
            inverseInertiaTensor.identity();
            inverseInertiaTensorWorld.identity();
            return;
        }

        Matrix4f trans = transform.getTransMatrix();
        float t4 = trans.m00() * inverseInertiaTensor.m00() + trans.m01() * inverseInertiaTensor.m10() + trans.m02() * inverseInertiaTensor.m20();
        float t9 = trans.m00() * inverseInertiaTensor.m01() + trans.m01() * inverseInertiaTensor.m11() + trans.m02() * inverseInertiaTensor.m21();
        float t14 = trans.m00() * inverseInertiaTensor.m02() + trans.m01() * inverseInertiaTensor.m12() + trans.m02() * inverseInertiaTensor.m22();
        float t28 = trans.m10() * inverseInertiaTensor.m00() + trans.m11() * inverseInertiaTensor.m10() + trans.m12() * inverseInertiaTensor.m20();
        float t33 = trans.m10() * inverseInertiaTensor.m01() + trans.m11() * inverseInertiaTensor.m11() + trans.m12() * inverseInertiaTensor.m21();
        float t38 = trans.m10() * inverseInertiaTensor.m02() + trans.m11() * inverseInertiaTensor.m12() + trans.m12() * inverseInertiaTensor.m22();
        float t52 = trans.m20() * inverseInertiaTensor.m00() + trans.m21() * inverseInertiaTensor.m10() + trans.m22() * inverseInertiaTensor.m20();
        float t57 = trans.m20() * inverseInertiaTensor.m01() + trans.m21() * inverseInertiaTensor.m11() + trans.m22() * inverseInertiaTensor.m21();
        float t62 = trans.m20() * inverseInertiaTensor.m02() + trans.m21() * inverseInertiaTensor.m12() + trans.m22() * inverseInertiaTensor.m22();

        inverseInertiaTensorWorld.m00(t4 * trans.m00() + t9 * trans.m01() + t14 * trans.m02());
        inverseInertiaTensorWorld.m01(t4 * trans.m10() + t9 * trans.m11() + t14 * trans.m12());
        inverseInertiaTensorWorld.m02(t4 * trans.m20() + t9 * trans.m21() + t14 * trans.m22());
        inverseInertiaTensorWorld.m10(t28 * trans.m00() + t33 * trans.m01() + t38 * trans.m02());
        inverseInertiaTensorWorld.m11(t28 * trans.m10() + t33 * trans.m11() + t38 * trans.m12());
        inverseInertiaTensorWorld.m12(t28 * trans.m20() + t33 * trans.m21() + t38 * trans.m22());
        inverseInertiaTensorWorld.m20(t52 * trans.m00() + t57 * trans.m01() + t62 * trans.m02());
        inverseInertiaTensorWorld.m21(t52 * trans.m10() + t57 * trans.m11() + t62 * trans.m12());
        inverseInertiaTensorWorld.m22(t52 * trans.m20() + t57 * trans.m21() + t62 * trans.m22());
    }

}
