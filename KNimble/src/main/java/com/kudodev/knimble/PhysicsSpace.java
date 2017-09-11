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
import com.kudodev.knimble.contact.Contact;
import com.kudodev.knimble.contact.ContactCache;
import com.kudodev.knimble.contact.ContactResolver;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IkeOTL
 */
public class PhysicsSpace {

    private List<Rigidbody> rigidbodies = new ArrayList<>();
    private List<Collider> colliders = new ArrayList<>();

    private ContactCache contactCache = new ContactCache(256);
    private ContactResolver contactResolver = new ContactResolver();

    public void tick(float delta) {
        for (Rigidbody r : rigidbodies) {
            r.integrate(delta);
        }

        for (int i = 0; i < colliders.size(); i++) {
            for (int j = i + 1; j < colliders.size(); j++) {
                Collider c0 = colliders.get(i);
                Collider c1 = colliders.get(j);

                long l = System.nanoTime();
                boolean intersects = c0.intersectsWith(c1);
                System.out.println("elapsed: " + (System.nanoTime() - l) + " nano");

                if (!intersects) {
                    continue;
                }
                System.out.println("Intersects!");

                Rigidbody r0 = c0.getRigidbody();
                Rigidbody r1 = c1.getRigidbody();
                if (r0 == null || r1 == null) {
                    continue;
                }

                c0.createCollision(c1, contactCache);
            }
        }
        
        // contactResolver
        contactResolver.setIterations(contactCache.getContactCount() * 2);
        contactResolver.resolveContacts(contactCache, delta);
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
}
