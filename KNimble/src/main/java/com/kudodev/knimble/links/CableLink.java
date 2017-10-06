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
package com.kudodev.knimble.links;

import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.contact.Contact;
import com.kudodev.knimble.contact.ContactCache;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class CableLink extends RigidbodyLink {

    private float length;
    private float restitution = 0;

    public CableLink(Rigidbody a, Rigidbody b, float length, float restitution) {
        super(a, b);
        this.length = length;
        this.restitution = restitution;
    }

    public CableLink(Rigidbody a, Rigidbody b, float length) {
        this(a, b, length, 0);
    }

    public CableLink(Rigidbody a, Rigidbody b) {
        super(a, b);
        length = a.getTransform().getWorldPosition()
                .distance(b.getTransform().getWorldPosition());
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    @Override
    public void tick(ContactCache cData) {

        // Calculate the length of the link
        Vector3f d = new Vector3f(rigidbodies[1].getTransform().getWorldPosition())
                .sub(rigidbodies[0].getTransform().getWorldPosition());
        float currentLength = d.length();

        // Check if it is violated
        if (currentLength - length < .0001f) {
            return;
        }

        Contact contact = cData.getContact();
        contact.contactNormal.set(d).normalize();
        contact.contactPoint.set(rigidbodies[0].getTransform().getWorldPosition())
                .add(rigidbodies[1].getTransform().getWorldPosition()).mul(0.5f);
        contact.penetration = currentLength - length;
        contact.setup(rigidbodies[0], rigidbodies[1], 1, restitution);
    }
}
