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

import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class ContactResolver {

    private int velocityIterations;
    private int positionIterations;

    private float velocityEpsilon = 0.001f;
    private float positionEpsilon = 0.001f;

    private int velocityIterationsUsed = 0;
    private int positionIterationsUsed = 0;

    public ContactResolver() {
    }

    public ContactResolver(int iterations) {
        setIterations(iterations);
    }

    /**
     * Creates a new contact resolver with the given number of iterations per
     * resolution call, and optional epsilon values.
     */
    public ContactResolver(int iterations, float velocityEpsilon, float positionEpsilon) {
        setIterations(iterations);
        setEpsilon(velocityEpsilon, positionEpsilon);
    }

    /**
     * Creates a new contact resolver with the given number of iterations for
     * each kind of resolution, and optional epsilon values.
     */
    public ContactResolver(int velocityIterations, int positionIterations, float velocityEpsilon, float positionEpsilon) {
        setIterations(velocityIterations, positionIterations);
        setEpsilon(velocityEpsilon, positionEpsilon);
    }

    /**
     * Sets the number of iterations for each resolution stage.
     */
    public void setIterations(int velocityIterations, int positionIterations) {
        this.velocityIterations = velocityIterations;
        this.positionIterations = positionIterations;
    }

    /**
     * Sets the number of iterations for both resolution stages.
     */
    public void setIterations(int iterations) {
        setIterations(iterations, iterations);
    }

    /**
     * Sets the tolerance value for both velocity and position.
     */
    public void setEpsilon(float velocityEpsilon, float positionEpsilon) {
        this.velocityEpsilon = velocityEpsilon;
        this.positionEpsilon = positionEpsilon;
    }

    /**
     * Resolves a setup of contacts for both penetration and velocity.
     *
     * Contacts that cannot interact with each other should be passed to
     * separate calls to resolveContacts, as the resolution algorithm takes much
     * longer for lots of contacts than it does for the same number of contacts
     * in small sets.
     *
     * @param contactArray Pointer to an array of contact objects.
     *
     * @param numContacts The number of contacts in the array to resolve.
     *
     * @param numIterations The number of iterations through the resolution
     * algorithm. This should be at least the number of contacts (otherwise some
     * constraints will not be resolved - although sometimes this is not
     * noticable). If the iterations are not needed they will not be used, so
     * adding more iterations may not make any difference. In some cases you
     * would need millions of iterations. Think about the number of iterations
     * as a bound: if you specify a large number, sometimes the algorithm WILL
     * use it, and you may drop lots of frames.
     *
     * @param delta The duration of the previous integration step. This is used
     * to compensate for forces applied.
     */
    public void resolveContacts(ContactCache contactCache, float delta) {
        int numContacts = contactCache.getContactCount();
        Contact[] contacts = contactCache.getContacts();
        // Make sure we have something to do. 
        if (numContacts == 0) {
            return;
        }
        if (!(velocityIterations > 0 && positionIterations > 0
                && positionEpsilon >= 0.0f && positionEpsilon >= 0.0f)) {
            return;
        }
        // Prepare the contacts for processing 
        for (int i = 0; i < numContacts; i++) {
            // Calculate the internal contact data (inertia, basis, etc). 
            contacts[i].tick(delta);
        }

        // Resolve the interpenetration problems with the contacts. 
        adjustPositions(contacts, numContacts, delta);

        // Resolve the velocity problems with the contacts. 
        adjustVelocities(contacts, numContacts, delta);
    }

    /**
     * Resolves the positional issues with the given array of constraints, using
     * the given number of iterations.
     */
    protected void adjustPositions(Contact[] c, int numContacts, float duration) {

        Vector3f[] linearChange = {new Vector3f(), new Vector3f()};
        Vector3f[] angularChange = {new Vector3f(), new Vector3f()};

        int i, index;
        float max;
        // iteratively resolve interpenetrations in order of severity.
        positionIterationsUsed = 0;
        Vector3f deltaPosition = new Vector3f();
        while (positionIterationsUsed < positionIterations) {
            positionIterationsUsed++;

            // Find biggest penetration
            max = positionEpsilon;
            index = numContacts;
            for (i = 0; i < numContacts; i++) {
                if (c[i].getPenetration() > max) {
                    max = c[i].getPenetration();
                    index = i;
                }
            }

            if (index == numContacts) {
                break;
            }

            // Match the awake state at the contact
            c[index].matchAwakeState();

            // Resolve the penetration.
            c[index].applyPositionChange(linearChange, angularChange, max);
            // Again this action may have changed the penetration of other
            // bodies, so we update contacts.
            for (i = 0; i < numContacts; i++) {
                // Check each getBody in the contact
                for (int b = 0; b < 2; b++) {
                    if (c[i].getBody(b) == null) {
                        continue;
                    }
                    // Check for a match with each getBody in the newly
                    // resolved contact
                    for (int d = 0; d < 2; d++) {
                        if (c[i].getBody(b) != c[index].getBody(d)) {
                            continue;
                        }
                        deltaPosition.set(angularChange[d]);
                        deltaPosition.cross(c[i].getRelativeContactPosition(b));
                        deltaPosition.add(linearChange[d]);

                        // The sign of the change is positive if we're
                        // dealing with the second getBody in a contact
                        // and negative otherwise (because we're
                        // subtracting the resolution)..
                        c[i].addPenetration(deltaPosition.dot(c[i].getContactNormal()) * (b != 0 ? 1 : -1));
                    }
                }
            }
        }
    }

    /**
     * Resolves the velocity issues with the given array of constraints, using
     * the given number of iterations.
     */
    protected void adjustVelocities(Contact[] c, int numContacts, float duration) {

        Vector3f[] velocityChange = {new Vector3f(), new Vector3f()};
        Vector3f[] rotationChange = {new Vector3f(), new Vector3f()};

        // iteratively handle impacts in order of severity.
        velocityIterationsUsed = 0;
        Vector3f deltaVel = new Vector3f();
        while (velocityIterationsUsed < velocityIterations) {
            velocityIterationsUsed++;
            // Find contact with maximum magnitude of probable velocity change.
            float max = velocityEpsilon;
            int index = numContacts;
            for (int i = 0; i < numContacts; i++) {
                if (c[i].desiredDeltaVelocity() > max) {
                    max = c[i].desiredDeltaVelocity();
                    index = i;
                }
            }

            if (index >= numContacts) {
                break;
            }

            // Match the awake state at the contact
            c[index].matchAwakeState();
            // Do the resolution on the contact that came out top.
            c[index].applyVelocityChange(velocityChange, rotationChange);

            // With the change in velocity of the two bodies, the update of
            // contact velocities means that some of the relative closing
            // velocities need recomputing.
            for (int i = 0; i < numContacts; i++) {
                // Check each getBody in the contact
                for (int b = 0; b < 2; b++) {
                    if (c[i].getBody(b) == null) {
                        continue;
                    }
                    // Check for a match with each getBody in the newly
                    // resolved contact
                    for (int d = 0; d < 2; d++) {
                        if (c[i].getBody(b) != c[index].getBody(d)) {
                            continue;
                        }
                        deltaVel.set(rotationChange[d]);
                        deltaVel.cross(c[i].getRelativeContactPosition(b));
                        deltaVel.add(velocityChange[d]);
                        // The sign of the change is negative if we're dealing
                        // with the second getBody in a contact.
                        deltaVel.mulTranspose(c[i].getContactToWorld()).mul(b != 0 ? -1 : 1);
                        c[i].getContactVelocity().add(deltaVel);
                        c[i].calculateDesiredDeltaVelocity(duration);
                    }
                }
            }
        }
    }
}
