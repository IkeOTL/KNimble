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

import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.Transform;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class Contact {

    /**
     * Holds the bodies that are involved in the contact. The second of these
     * can be NULL, for contacts with the scenery.
     */
    public final Rigidbody[] body = new Rigidbody[2];

    /**
     * Holds the lateral friction coefficient at the contact.
     */
    public float friction;

    /**
     * Holds the normal restitution coefficient at the contact.
     */
    public float restitution;

    /**
     * Holds the position of the contact in world coordinates.
     */
    public final Vector3f contactPoint = new Vector3f();

    /**
     * Holds the direction of the contact in world coordinates.
     */
    public final Vector3f contactNormal = new Vector3f();

    /**
     * Holds the depth of penetration at the contact point. If both bodies are
     * specified then the contact point should be midway between the
     * inter-penetrating points.
     */
    public float penetration;

    /**
     * A transform matrix that converts co-ordinates in the contact's frame of
     * reference to world co-ordinates. The columns of this matrix form an
     * orthonormal set of vectors.
     */
    protected final Matrix3f contactToWorld = new Matrix3f();

    /**
     * Holds the closing velocity at the point of contact. This is set when the
     * calculateInternals function is run.
     */
    protected final Vector3f contactVelocity = new Vector3f();

    /**
     * Holds the required change in velocity for this contact to be resolved.
     */
    protected float desiredDeltaVelocity;

    /**
     * Holds the world space position of the contact point relative to center of
     * each body. This is set when the calculateInternals function is run.
     */
    protected final Vector3f[] relativeContactPosition = {new Vector3f(), new Vector3f()};

    /**
     * Sets the data that doesn't normally depend on the position of the contact
     * (i.e. the bodies, and their material properties).
     */
    public void setBodyData(Rigidbody a, Rigidbody b) {
        setBodyData(a, b,
                (a.getFriction() + b.getFriction()) * .5f,
                (a.getRestitution() + b.getRestitution()) * .5f);
    }

    public void setBodyData(Rigidbody a, Rigidbody b, float friction, float restitution) {
        body[0] = a;
        body[1] = b;
        this.friction = friction;
        this.restitution = restitution;
    }

    /**
     * Calculates internal data from state data. This is called before the
     * resolution algorithm tries to do any resolution. It should never need to
     * be called manually.
     */
    protected void calculateInternals(float duration) {
        // Check if the first object is NULL, and swap if it is.
        if (body[0] == null) {
            swapBodies();
        }
        if (body[0] == null) {
            return;
        }

        // Calculate an set of axis at the contact point.
        calculateContactBasis();

        // Store the relative position of the contact relative to each body
        relativeContactPosition[0].set(contactPoint).sub(body[0].transform.getWorldPosition());
        if (body[1] != null) {
            relativeContactPosition[1].set(contactPoint).sub(body[1].transform.getWorldPosition());
        }

        // Find the relative velocity of the bodies at the contact point.
        contactVelocity.set(calculateLocalVelocity(0, duration));
        if (body[1] != null) {
            contactVelocity.sub(calculateLocalVelocity(1, duration));
        }
        // Calculate the desired change in velocity for resolution
        calculateDesiredDeltaVelocity(duration);
    }

    /**
     * Reverses the contact. This involves swapping the two rigid bodies and
     * reversing the contact normal. The internal values should then be
     * recalculated using calculateInternals (this is not done automatically).
     */
    protected void swapBodies() {
        contactNormal.mul(-1);

        Rigidbody temp = body[0];
        body[0] = body[1];
        body[1] = temp;
    }

    /**
     * Updates the awake state of rigid bodies that are taking place in the
     * given contact. A body will be made awake if it is in contact with a body
     * that is awake.
     */
    protected void matchAwakeState() {
        // Collisions with the world never cause a body to wake up. 
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

    /**
     * Calculates and sets the internal value for the desired delta velocity.
     */
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
        if (Math.abs(contactVelocity.x) < velocityLimit) {
            thisRestitution = 0.0f;
        }

        // Combine the bounce velocity with the removed
        // acceleration velocity.
        desiredDeltaVelocity = -contactVelocity.x - thisRestitution * (contactVelocity.x - velocityFromAcc);
    }

    /**
     * Calculates and returns the velocity of the contact point on the given
     * body.
     */
    private Vector3f calculateLocalVelocity(int bodyIndex, float duration) {
        Rigidbody thisBody = body[bodyIndex];

        Vector3f contactVelocity = new Vector3f();
        // Work out the velocity of the contact point.
        contactVelocity.set(thisBody.getAngularVelocity()).cross(relativeContactPosition[bodyIndex]);
        contactVelocity.add(thisBody.getLinearVelocity());

        // Turn the velocity into contact-coordinates.
        //CHECK: mulTranspose?
        contactVelocity.mulTranspose(contactToWorld);

        // Calculate the ammount of velocity that is due to forces without
        // reactions.
        Vector3f accVelocity = new Vector3f(thisBody.getLastFrameLinearAcceleration()).mul(duration);
        // Calculate the velocity in contact-coordinates.
        //CHECK: mulTranspose?
        accVelocity.mulTranspose(contactToWorld);
        // We ignore any component of acceleration in the contact normal
        // direction, we are only interested in planar acceleration
        accVelocity.x = 0;

        // Add the planar velocities - if there's enough friction they will
        // be removed during velocity resolution
        contactVelocity.add(accVelocity);
        return contactVelocity;
    }

    /**
     * Calculates an orthonormal basis for the contact point, based on the
     * primary friction direction (for anisotropic friction) or a random
     * orientation (for isotropic friction).
     */
    private Vector3f[] contactTangent = {new Vector3f(), new Vector3f()};

    protected void calculateContactBasis() {

        // Check whether the Z-axis is nearer to the X or Y axis
        if (Math.abs(contactNormal.x) > Math.abs(contactNormal.y)) {
            // Scaling factor to ensure the results are normalised
            float s = 1.0f / (float) Math.sqrt(contactNormal.z * contactNormal.z
                    + contactNormal.x * contactNormal.x);

            // The new X-axis is at right angles to the world Y-axis
            contactTangent[0].x = contactNormal.z * s;
            contactTangent[0].y = 0;
            contactTangent[0].z = -contactNormal.x * s;

            // The new Y-axis is at right angles to the new X- and Z- axes
            //  contactTangent[1].x = contactNormal.y * contactTangent[0].x; //original
            contactTangent[1].x = contactNormal.y * contactTangent[0].z;
            contactTangent[1].y = contactNormal.z * contactTangent[0].x - contactNormal.x * contactTangent[0].z;
            contactTangent[1].z = -contactNormal.y * contactTangent[0].x;
        } else {
            // Scaling factor to ensure the results are normalised
            float s = 1.0f / (float) Math.sqrt(contactNormal.z * contactNormal.z
                    + contactNormal.y * contactNormal.y);

            // The new X-axis is at right angles to the world X-axis
            contactTangent[0].x = 0;
            contactTangent[0].y = -contactNormal.z * s;
            contactTangent[0].z = contactNormal.y * s;

            // The new Y-axis is at right angles to the new X- and Z- axes
            contactTangent[1].x = contactNormal.y * contactTangent[0].z - contactNormal.z * contactTangent[0].y;
            contactTangent[1].y = -contactNormal.x * contactTangent[0].z;
            contactTangent[1].z = contactNormal.x * contactTangent[0].y;
        }

        // Make a matrix from the three vectors.
        //CHECK: MIGHT HAVE TO FLIP ROWS/COLUMNS
        contactToWorld.set(contactNormal, contactTangent[0], contactTangent[1]);

    }

    /**
     * Performs an inertia-weighted impulse based resolution of this contact
     * alone.
     */
    protected void applyVelocityChange(Vector3f[] velocityChange, Vector3f[] rotationChange) {
        // Get hold of the inverse mass and inverse inertia tensor, both in
        // world coordinates.
        Matrix3f[] inverseInertiaTensor = new Matrix3f[2];
        inverseInertiaTensor[0] = body[0].getInverseInertiaTensorWorld();
        if (body[1] != null) {
            inverseInertiaTensor[1] = body[1].getInverseInertiaTensorWorld();
        }

        // We will calculate the impulse for each contact axis
        Vector3f impulse;

        if (friction == 0.0f) {
            // Use the short format for frictionless contacts
            impulse = calculateFrictionlessImpulse(inverseInertiaTensor);
        } else {
            // Otherwise we may have impulses that aren't in the direction of the
            // contact, so we need the more complex version.
            impulse = calculateFrictionImpulse(inverseInertiaTensor);
        }

        // Convert impulse to world coordinates
        impulse.mul(contactToWorld);

        // Split in the impulse into linear and rotational components
        Vector3f impulsiveTorque = new Vector3f(relativeContactPosition[0]).cross(impulse);
        rotationChange[0].set(impulsiveTorque).mul(inverseInertiaTensor[0]);
        velocityChange[0].set(impulse).mul(body[0].getInverseMass());

        // Apply the changes
        body[0].addLinearVelocity(velocityChange[0]);
        body[0].addAngularVelocity(rotationChange[0]);

        if (body[1] != null) {
            // Work out body one's linear and angular changes
            impulsiveTorque.set(impulse).cross(relativeContactPosition[1]);
            rotationChange[1].set(impulsiveTorque).mul(inverseInertiaTensor[1]);
            velocityChange[1].set(impulse).mul(-body[1].getInverseMass());

            // And apply them.
            body[1].addLinearVelocity(velocityChange[1]);
            body[1].addAngularVelocity(rotationChange[1]);
        }
    }

    /**
     * Performs an inertia weighted penetration resolution of this contact
     * alone.
     */
    protected void applyPositionChange(Vector3f[] linearChange, Vector3f[] angularChange, float penetration) {

        float totalInertia = 0;
        float[] linearInertia = {0, 0};
        float[] angularInertia = {0, 0};
        // We need to work out the inertia of each object in the direction
        // of the contact normal, due to angular inertia only.
        Vector3f angularInertiaWorld = new Vector3f();
        for (int i = 0; i < 2; i++) {
            if (body[i] == null) {
                continue;
            }
            // Use the same procedure as for calculating frictionless
            // velocity change to work out the angular inertia.
            angularInertiaWorld.set(relativeContactPosition[i]).cross(contactNormal);
            angularInertiaWorld.mul(body[i].getInverseInertiaTensorWorld());
            angularInertiaWorld.cross(relativeContactPosition[i]);
            angularInertia[i] = angularInertiaWorld.dot(contactNormal);

            // The linear component is simply the inverse mass
            linearInertia[i] = body[i].getInverseMass();

            // Keep track of the total inertia from all components
            totalInertia += linearInertia[i] + angularInertia[i];

            // We break the loop here so that the totalInertia value is
            // completely calculated (by both iterations) before
            // continuing.
        }

        // Loop through again calculating and applying the changes
        float angularLimit = 0.2f;
        float[] angularMove = {0, 0};
        float[] linearMove = {0, 0};
        for (int i = 0; i < 2; i++) {
            if (body[i] == null) {
                continue;
            }
            // The linear and angular movements required are in proportion to
            // the two inverse inertias.
            float sign = (i == 0) ? 1 : -1;
            angularMove[i] = sign * penetration * (angularInertia[i] / totalInertia);
            linearMove[i] = sign * penetration * (linearInertia[i] / totalInertia);

            // To avoid angular projections that are too great (when mass is large
            // but inertia tensor is small) limit the angular move.
            Vector3f projection = new Vector3f(relativeContactPosition[i]);
            projection.fma(-relativeContactPosition[i].dot(contactNormal), contactNormal);

            // Use the small angle approximation for the sine of the angle (i.e.
            // the magnitude would be sine(angularLimit) * projection.magnitude
            // but we approximate sine(angularLimit) to angularLimit).
            float maxMagnitude = angularLimit * projection.length();

            if (angularMove[i] < -maxMagnitude) {
                angularMove[i] = -maxMagnitude;
                linearMove[i] = angularMove[i] + linearMove[i];
                linearMove[i] -= angularMove[i];
            } else if (angularMove[i] > maxMagnitude) {
                angularMove[i] = maxMagnitude;
                linearMove[i] = angularMove[i] + linearMove[i];
                linearMove[i] -= angularMove[i];
            }

            // We have the linear amount of movement required by turning
            // the rigid body (in angularMove[i]). We now need to
            // calculate the desired rotation to achieve that.
            if (angularMove[i] < .00001f) {
                // Easy case - no angular movement means no rotation.
                angularChange[i].set(0, 0, 0);
            } else {
                // Work out the direction we'd like to rotate in.
                angularChange[i].set(relativeContactPosition[i]);
                angularChange[i].cross(contactNormal);

                // Work out the direction we'd need to rotate to achieve that
                angularChange[i].mul(body[i].getInverseInertiaTensorWorld());
                angularChange[i].mul(angularMove[i] / angularInertia[i]);
            }

            // Velocity change is easier - it is just the linear movement
            // along the contact normal.
            linearChange[i].set(contactNormal).mul(linearMove[i]);

            Transform transform = body[i].getTransform();

            transform.rotation.integrate(1, angularChange[i].x, angularChange[i].y, angularChange[i].z);

            // Apply the linear movement
            transform.position.fma(linearMove[i], contactNormal);
            transform.setDirty();

            // We need to calculate the derived data for any body that is
            // asleep, so that the changes are reflected in the object's
            // data. Otherwise the resolution will not change the position
            // of the object, and the next collision detection round will
            // have the same penetration.
            if (!body[i].isAwake()) {
                body[i].calculateIITWorld();
            }
        }
    }

    /**
     * Calculates the impulse needed to resolve this contact, given that the
     * contact has no friction. A pair of inertia tensors - one for each contact
     * object - is specified to save calculation time: the calling function has
     * access to these anyway.
     */
    protected Vector3f calculateFrictionlessImpulse(Matrix3f[] inverseInertiaTensor) {
        Vector3f impulseContact = new Vector3f();

        // Build a vector that shows the change in velocity in
        // world space for a unit impulse in the direction of the contact
        // normal.
        Vector3f deltaVelWorld = new Vector3f(relativeContactPosition[0]).cross(contactNormal);
        deltaVelWorld.mul(inverseInertiaTensor[0]);
        deltaVelWorld.cross(relativeContactPosition[0]);

        // Work out the change in velocity in contact coordiantes.
        float deltaVelocity = deltaVelWorld.dot(contactNormal);
        // Add the linear component of velocity change
        deltaVelocity += body[0].getInverseMass();

        // Check if we need to the second body's data
        if (body[1] != null) {
            // Go through the same transformation sequence again
            deltaVelWorld.set(relativeContactPosition[1]).cross(contactNormal);
            deltaVelWorld.mul(inverseInertiaTensor[1]);
            deltaVelWorld.cross(relativeContactPosition[1]);

            // Add the change in velocity due to rotation
            deltaVelocity += deltaVelWorld.dot(contactNormal);

            // Add the change in velocity due to linear motion
            deltaVelocity += body[1].getInverseMass();
        }

        // Calculate the required size of the impulse
        impulseContact.x = desiredDeltaVelocity / deltaVelocity;
        impulseContact.y = 0;
        impulseContact.z = 0;
        return impulseContact;

    }

    /**
     * Calculates the impulse needed to resolve this contact, given that the
     * contact has a non-zero coefficient of friction. A pair of inertia tensors
     * - one for each contact object - is specified to save calculation time:
     * the calling function has access to these anyway.
     */
    protected Vector3f calculateFrictionImpulse(Matrix3f[] inverseInertiaTensor) {

        // The equivalent of a cross product in matrices is multiplication
        // by a skew symmetric matrix - we build the matrix for converting
        // between linear and angular quantities.
        Matrix3f impulseToTorque = new Matrix3f().setSkewSymmetric(
                relativeContactPosition[0].z,
                relativeContactPosition[0].y,
                relativeContactPosition[0].x);

        // Build the matrix to convert contact impulse to change in velocity
        // in world coordinates.
        Matrix3f deltaVelWorld = new Matrix3f(impulseToTorque);
        deltaVelWorld.mul(inverseInertiaTensor[0]);
        deltaVelWorld.mul(impulseToTorque);
        deltaVelWorld.scale(-1);

        float inverseMass = body[0].getInverseMass();
        // Check if we need to add body two's data
        if (body[1] != null) {
            // Set the cross product matrix

            impulseToTorque.setSkewSymmetric(
                    relativeContactPosition[1].z,
                    relativeContactPosition[1].y,
                    relativeContactPosition[1].x);

            // Calculate the velocity change matrix
            Matrix3f deltaVelWorld2 = new Matrix3f(impulseToTorque);
            deltaVelWorld2.mul(inverseInertiaTensor[1]);
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
        Vector3f impulseContact = new Vector3f(desiredDeltaVelocity, -contactVelocity.y, -contactVelocity.z);
        // Find the impulse to kill target velocities        
        // Invert to get the impulse needed per unit velocity
        impulseContact.mul(deltaVelocity.invert(new Matrix3f()));

        // Check for exceeding friction
        float planarImpulse = (float) Math.sqrt(impulseContact.y * impulseContact.y + impulseContact.z * impulseContact.z);
        if (planarImpulse > impulseContact.x * friction) {
            // We need to use dynamic friction
            impulseContact.y /= planarImpulse;
            impulseContact.z /= planarImpulse;

            impulseContact.x = desiredDeltaVelocity / (deltaVelocity.m00
                    + deltaVelocity.m01 * friction * impulseContact.y
                    + deltaVelocity.m02 * friction * impulseContact.z);
            impulseContact.y *= friction * impulseContact.x;
            impulseContact.z *= friction * impulseContact.x;
        }
        return impulseContact;
    }

}
