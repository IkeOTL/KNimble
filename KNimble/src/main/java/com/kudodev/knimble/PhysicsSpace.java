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

import com.kudodev.knimble.colliders.Collider;
import com.kudodev.knimble.links.RigidbodyLink;
import com.kudodev.knimble.contact.ContactCache;
import com.kudodev.knimble.contact.ContactResolver;
import com.kudodev.knimble.generators.ForceGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IkeOTL
 */
public class PhysicsSpace {

    private final List<Rigidbody> rigidbodies = new ArrayList<>();
    private final List<Collider> colliders = new ArrayList<>();

    private final ContactCache contactCache = new ContactCache(512);
    private final ContactResolver contactResolver = new ContactResolver();
    private final List<RigidbodyLink> rigidbodyLinks = new ArrayList<>();
    private final List<ForceGenerator> forceGenerators = new ArrayList<>();

    private float frameAccum = 0;
    private final float stepSize = 1f / 60f;

    public void tick(float delta) {
        frameAccum += delta;
//        if (frameAccum > 0.05f) {
//            frameAccum = 0.05f;
//        }

        while (frameAccum >= stepSize) {
            frameAccum -= stepSize;

            for (int i = 0; i < rigidbodies.size(); i++) {
                Rigidbody r = rigidbodies.get(i);

                for (int j = 0; j < forceGenerators.size(); j++) {
                    forceGenerators.get(j).tick(r, stepSize);
                }

                r.integrate(stepSize);
            }

            outer:
            for (int i = 0; i < colliders.size(); i++) {
                for (int j = i + 1; j < colliders.size(); j++) {

                    Collider c0 = colliders.get(i);
                    Collider c1 = colliders.get(j);

                    boolean intersects = c0.intersectsWith(c1);
                    if (!intersects) {
                        continue;
                    }

                    if (c0.getRigidbody() == null || c1.getRigidbody() == null) {
                        continue;
                    }

                    if (!contactCache.hasMoreContacts()) {
                        break outer;
                    }
                    c0.createCollision(c1, contactCache);
                }
            }

            for (RigidbodyLink c : rigidbodyLinks) {
                if (!contactCache.hasMoreContacts()) {
                    break;
                }
                c.tick(contactCache);
            }

            contactResolver.setIterations(contactCache.getContactCount() * 2);
            contactResolver.resolveContacts(contactCache, stepSize);
            contactCache.reset();
        }
    }

    public void addBody(Rigidbody r, Collider c) {
        rigidbodies.add(r);
        colliders.add(c);
    }

    public void addRigidbody(Rigidbody r) {
        rigidbodies.add(r);
    }

    public void addCollider(Collider c) {
        colliders.add(c);
    }

    public void addRigidbodyLink(RigidbodyLink c) {
        rigidbodyLinks.add(c);
    }

    public void addForceGenerator(ForceGenerator g) {
        forceGenerators.add(g);
    }
}
