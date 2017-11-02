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
package com.kudodev.knimble.contact;

import com.kudodev.knimble.JOMLExtra;
import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.Transform;
import org.joml.Matrix3f;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class Contact {

    private final Rigidbody[] body = new Rigidbody[2];
    private final Vector3f[] localContactPoint = {new Vector3f(), new Vector3f()};

    private final Matrix3f contactToWorld = new Matrix3f();
    private final Vector3f localContactVelocity = new Vector3f();
    private float desiredDeltaVelocity;

    private float friction;
    private float restitution;

    private final Vector3f worldContactPoint = new Vector3f();
    private final Vector3f contactNormal = new Vector3f();
    private float penetration;

    protected Rigidbody getBody(int i) {
        return body[i];
    }

    protected Vector3f getRelativeContactPosition(int i) {
        return localContactPoint[i];
    }

    protected Vector3f getContactVelocity() {
        return localContactVelocity;
    }

    protected Matrix3f getContactToWorld() {
        return contactToWorld;
    }

    protected float desiredDeltaVelocity() {
        return desiredDeltaVelocity;
    }

    public Vector3f getContactPoint() {
        return worldContactPoint;
    }

    public Vector3f setContactPoint(Vector3f v) {
        return worldContactPoint.set(v);
    }

    public Vector3f getContactNormal() {
        return contactNormal;
    }

    public Vector3f setContactNormal(Vector3f v) {
        return contactNormal.set(v);
    }

    public float getPenetration() {
        return penetration;
    }

    public void addPenetration(float p) {
        penetration += p;
    }

    public void setPenetration(float penetration) {
        this.penetration = penetration;
    }

    public void setup(Rigidbody a, Rigidbody b) {
        setup(a, b,
                (float) Math.sqrt(a.getFriction() * b.getFriction()),
                Math.max(a.getRestitution(), b.getRestitution()));
    }

    public void setup(Rigidbody a, Rigidbody b, float friction, float restitution) {
        body[0] = a.getMass() > 0 ? a : null;
        body[1] = b.getMass() > 0 ? b : null;
        this.friction = friction;
        this.restitution = restitution;
    }

    protected void tick(float delta) {
        // Check if the first object is NULL, and swap if it is.
        if (body[0] == null) {
            swapBodies();
        }
        if (body[0] == null) {
            return;
        }

        // Calculate an setup of axis at the contact point.
        calculateContactBasis();

        // Store the relative position of the contact relative to each getBody
        calculateLocal(delta);

        // Calculate the desired change in velocity for resolution
        calculateDesiredDeltaVelocity(delta);
    }

    private void calculateLocal(float delta) {
        Vector3f out = new Vector3f();
        localContactPoint[0].set(worldContactPoint).sub(body[0].getTransform().getWorldPosition());

        calculateLocalVelocity(body[0], localContactPoint[0], delta, out);
        localContactVelocity.set(out);

        if (body[1] != null) {
            localContactPoint[1].set(worldContactPoint).sub(body[1].getTransform().getWorldPosition());

            calculateLocalVelocity(body[1], localContactPoint[1], delta, out);
            localContactVelocity.sub(out);
        }
    }

    private void calculateLocalVelocity(Rigidbody thisBody, Vector3f contactPoint, float duration, Vector3f contactVelocity) {
        contactVelocity.set(thisBody.getAngularVelocity()).cross(contactPoint);
        contactVelocity.add(thisBody.getLinearVelocity());
        contactVelocity.mulTranspose(contactToWorld);

        Vector3f accVelocity = new Vector3f(thisBody.getLastFrameLinearAcceleration()).mul(duration);
        accVelocity.mulTranspose(contactToWorld);
        accVelocity.x = 0;

        contactVelocity.add(accVelocity);
    }

    private void swapBodies() {
        contactNormal.mul(-1);

        Rigidbody temp = body[0];
        body[0] = body[1];
        body[1] = temp;
    }

    protected void matchAwakeState() {
        // Collisions with the world never cause a getBody to wake up. 
        if (body[1] == null) {
            return;
        }
        boolean body0awake = body[0].isAwake();
        boolean body1awake = body[1].isAwake();

        // Wake up only the sleeping one 
        if (body0awake ^ body1awake) {
            if (body0awake) {
                body[1].setAwake(true);
            } else {
                body[0].setAwake(true);
            }
        }
    }

    protected void calculateDesiredDeltaVelocity(float duration) {
        float velocityLimit = (float) 0.25f;

        // Calculate the acceleration induced velocity accumulated this frame
        float velocityFromAcc = 0;

        Vector3f temp = new Vector3f();
        if (body[0].isAwake()) {
            velocityFromAcc += temp.set(body[0].getLastFrameLinearAcceleration()).mul(duration).dot(contactNormal);
        }

        if (body[1] != null && body[1].isAwake()) {
            velocityFromAcc -= temp.set(body[1].getLastFrameLinearAcceleration()).mul(duration).dot(contactNormal);
        }

        // If the velocity is very slow, limit the restitution
        float thisRestitution = restitution;
        if (Math.abs(localContactVelocity.x()) < velocityLimit) {
            thisRestitution = 0.0f;
        }

        // Combine the bounce velocity with the removed
        // acceleration velocity.
        desiredDeltaVelocity = -localContactVelocity.x() - thisRestitution * (localContactVelocity.x() - velocityFromAcc);
    }

    private void calculateContactBasis() {
        contactToWorld.m00(contactNormal.x());
        contactToWorld.m01(contactNormal.y());
        contactToWorld.m02(contactNormal.z());

        // Check whether the Z-axis is nearer to the X or Y axis
        if (Math.abs(contactNormal.x()) > Math.abs(contactNormal.y())) {
            // Scaling factor to ensure the results are normalised
            float s = 1.0f / (float) Math.sqrt(contactNormal.z() * contactNormal.z()
                    + contactNormal.x() * contactNormal.x());

            contactToWorld.m10(contactNormal.z() * s);
            contactToWorld.m11(0);
            contactToWorld.m12(-contactNormal.x() * s);

            contactToWorld.m20(contactNormal.y() * contactToWorld.m12());
            contactToWorld.m21(contactNormal.z() * contactToWorld.m10() - contactNormal.x() * contactToWorld.m12());
            contactToWorld.m22(-contactNormal.y() * contactToWorld.m10());
        } else {
            // Scaling factor to ensure the results are normalised
            float s = 1.0f / (float) Math.sqrt(contactNormal.z() * contactNormal.z()
                    + contactNormal.y() * contactNormal.y());

            contactToWorld.m10(0);
            contactToWorld.m11(-contactNormal.z() * s);
            contactToWorld.m12(contactNormal.y() * s);

            contactToWorld.m20(contactNormal.y() * contactToWorld.m12() - contactNormal.z() * contactToWorld.m11());
            contactToWorld.m21(-contactNormal.x() * contactToWorld.m12());
            contactToWorld.m22(contactNormal.x() * contactToWorld.m11());
        }
    }

    protected void applyVelocityChange(Vector3f[] velocityChange, Vector3f[] rotationChange) {
        // We will calculate the impulse for each contact axis
        Vector3f impulse = new Vector3f();

        if (friction == 0.0f) {
            // Use the short format for frictionless contacts
            calculateFrictionlessImpulse(impulse);
        } else {
            // Otherwise we may have impulses that aren't in the direction of the
            // contact, so we need the more complex version.
            calculateFrictionImpulse(impulse);
        }

        // Convert impulse to world coordinates
        impulse.mul(contactToWorld);

        // Split in the impulse into linear and rotational components
        rotationChange[0].set(localContactPoint[0]).cross(impulse);
        rotationChange[0].mul(body[0].getInverseInertiaTensorWorld());
        velocityChange[0].set(impulse).mul(body[0].getInverseMass());

        // Apply the changes
        body[0].addLinearVelocity(velocityChange[0]);
        body[0].addAngularVelocity(rotationChange[0]);

        if (body[1] != null) {
            // Work out getBody one's linear and angular changes
            rotationChange[1].set(impulse).cross(localContactPoint[1]);
            rotationChange[1].mul(body[1].getInverseInertiaTensorWorld());
            velocityChange[1].set(impulse).mul(-body[1].getInverseMass());

            // And apply them.
            body[1].addLinearVelocity(velocityChange[1]);
            body[1].addAngularVelocity(rotationChange[1]);
        }
    }

    protected void applyPositionChange(Vector3f[] linearChange, Vector3f[] angularChange, float penetration) {
        float totalInertia = 0;
        float[] angularInertia = {0, 0};

        Vector3f angularInertiaWorld = new Vector3f();
        for (int i = 0; i < 2; i++) {
            if (body[i] == null) {
                continue;
            }
            // Use the same procedure as for calculating frictionless
            // velocity change to work out the angular inertia.
            angularInertiaWorld.set(localContactPoint[i]).cross(contactNormal);
            angularInertiaWorld.mul(body[i].getInverseInertiaTensorWorld());
            angularInertiaWorld.cross(localContactPoint[i]);
            angularInertia[i] = angularInertiaWorld.dot(contactNormal);

            // Keep track of the total inertia from all components
            totalInertia += body[i].getInverseMass() + angularInertia[i];
        }

        // Loop through again calculating and applying the changes
        float angularLimit = 0.2f;
        for (int i = 0; i < 2; i++) {
            if (body[i] == null) {
                continue;
            }
            // The linear and angular movements required are in proportion to
            // the two inverse inertias.
            float sign = (i == 0) ? 1 : -1;
            float angularMove = sign * penetration * (angularInertia[i] / totalInertia);
            float linearMove = sign * penetration * (body[i].getInverseMass() / totalInertia);

            // To avoid angular projections that are too great (when mass is large
            // but inertia tensor is small) limit the angular move.
            Vector3f projection = new Vector3f(localContactPoint[i]);
            projection.fma(-localContactPoint[i].dot(contactNormal), contactNormal);

            // Use the small angle approximation for the sine of the angle (i.e.
            // the magnitude would be sine(angularLimit) * projection.magnitude
            // but we approximate sine(angularLimit) to angularLimit).
            float maxMagnitude = angularLimit * projection.length();

            if (angularMove < -maxMagnitude) {
                angularMove = -maxMagnitude;
                linearMove += angularMove;
                linearMove -= angularMove;
            } else if (angularMove > maxMagnitude) {
                angularMove = maxMagnitude;
                linearMove += angularMove;
                linearMove -= angularMove;
            }

            // We have the linear amount of movement required by turning
            // the rigid getBody (in angularMove[i]). We now need to
            // calculate the desired rotation to achieve that.
            if (angularMove < .00001f) {
                // Easy case - no angular movement means no rotation.
                angularChange[i].set(0, 0, 0);
            } else {
                // Work out the direction we'd like to rotate in.
                angularChange[i].set(localContactPoint[i]);
                angularChange[i].cross(contactNormal);

                // Work out the direction we'd need to rotate to achieve that
                angularChange[i].mul(body[i].getInverseInertiaTensorWorld());
                angularChange[i].mul(angularMove / angularInertia[i]);
            }

            // Velocity change is easier - it is just the linear movement
            // along the contact normal.
            linearChange[i].set(contactNormal).mul(linearMove);

            Transform transform = body[i].getTransform();
            transform.getLocalRotation().integrate(1, angularChange[i].x(), angularChange[i].y(), angularChange[i].z());
            transform.getLocalPosition().fma(linearMove, contactNormal);
            transform.setDirty(true);

            if (!body[i].isAwake()) {
                body[i].calculateIITWorld();
            }
        }
    }

    protected void calculateFrictionlessImpulse(Vector3f impulseContact) {
        // Build a vector that shows the change in velocity in
        // world space for a unit impulse in the direction of the contact
        // normal.
        Vector3f deltaVelWorld = new Vector3f(localContactPoint[0]).cross(contactNormal);
        deltaVelWorld.mul(body[0].getInverseInertiaTensorWorld());
        deltaVelWorld.cross(localContactPoint[0]);

        // Work out the change in velocity in contact coordiantes.
        float deltaVelocity = deltaVelWorld.dot(contactNormal);
        // Add the linear component of velocity change
        deltaVelocity += body[0].getInverseMass();

        // Check if we need to the second getBody's data
        if (body[1] != null) {
            // Go through the same transformation sequence again
            deltaVelWorld.set(localContactPoint[1]).cross(contactNormal);
            deltaVelWorld.mul(body[1].getInverseInertiaTensorWorld());
            deltaVelWorld.cross(localContactPoint[1]);

            // Add the change in velocity due to rotation
            deltaVelocity += deltaVelWorld.dot(contactNormal);

            // Add the change in velocity due to linear motion
            deltaVelocity += body[1].getInverseMass();
        }

        // Calculate the required size of the impulse
        impulseContact.x = desiredDeltaVelocity / deltaVelocity;
        impulseContact.y = 0;
        impulseContact.z = 0;

    }

    protected void calculateFrictionImpulse(Vector3f impulseContact) {

        // The equivalent of a cross product in matrices is multiplication
        // by a skew symmetric matrix - we build the matrix for converting
        // between linear and angular quantities.
        Matrix3f impulseToTorque = new Matrix3f().setSkewSymmetric(
                localContactPoint[0].z(),
                localContactPoint[0].y(),
                localContactPoint[0].x());

        // Build the matrix to convert contact impulse to change in velocity
        // in world coordinates.
        Matrix3f deltaVelWorld = new Matrix3f(impulseToTorque);
        deltaVelWorld.mul(body[0].getInverseInertiaTensorWorld());
        deltaVelWorld.mul(impulseToTorque);
        deltaVelWorld.scale(-1);

        float inverseMass = body[0].getInverseMass();
        // Check if we need to add getBody two's data
        if (body[1] != null) {
            // Set the cross product matrix

            impulseToTorque.setSkewSymmetric(
                    localContactPoint[1].z(),
                    localContactPoint[1].y(),
                    localContactPoint[1].x());

            // Calculate the velocity change matrix
            Matrix3f deltaVelWorld2 = new Matrix3f(impulseToTorque);
            deltaVelWorld2.mul(body[1].getInverseInertiaTensorWorld());
            deltaVelWorld2.mul(impulseToTorque);
            deltaVelWorld2.scale(-1);

            // Add to the total delta velocity.
            deltaVelWorld.add(deltaVelWorld2);

            // Add to the inverse mass
            inverseMass += body[1].getInverseMass();
        }

        // Do a change of basis to convert into contact coordinates.
        Matrix3f deltaVelocity = new Matrix3f(contactToWorld).transpose();
        deltaVelocity.mul(deltaVelWorld);
        deltaVelocity.mul(contactToWorld);

        // Add in the linear velocity change
        deltaVelocity.m00 += inverseMass;
        deltaVelocity.m11 += inverseMass;
        deltaVelocity.m22 += inverseMass;

        // Find the target velocities to kill
        impulseContact.set(desiredDeltaVelocity, -localContactVelocity.y(), -localContactVelocity.z());
        // Find the impulse to kill target velocities        
        // Invert to get the impulse needed per unit velocity
        impulseContact.mul(deltaVelocity.invert(new Matrix3f()));

        // Check for exceeding friction
        float planarImpulse = (float) Math.sqrt(impulseContact.y() * impulseContact.y() + impulseContact.z() * impulseContact.z());
        if (planarImpulse > impulseContact.x() * friction) {
            // We need to use dynamic friction
            impulseContact.y /= planarImpulse;
            impulseContact.z /= planarImpulse;

            impulseContact.x = desiredDeltaVelocity / (deltaVelocity.m00
                    + deltaVelocity.m01 * friction * impulseContact.y()
                    + deltaVelocity.m02 * friction * impulseContact.z());
            impulseContact.y *= friction * impulseContact.x();
            impulseContact.z *= friction * impulseContact.x();
        }
    }

}
