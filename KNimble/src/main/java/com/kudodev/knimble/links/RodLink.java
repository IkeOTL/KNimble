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
public class RodLink extends RigidbodyLink {

    private float length;

    public RodLink(Rigidbody a, Rigidbody b, float length) {
        super(a, b);
        this.length = length;
    }

    public RodLink(Rigidbody a, Rigidbody b) {
        super(a, b);
        length = a.transform.getWorldPosition()
                .distance(b.transform.getWorldPosition());
    }

    @Override
    public void tick(ContactCache cData) {
        // Find the length of the rod
        Vector3f d = new Vector3f(rigidbodies[1].transform.getWorldPosition())
                .sub(rigidbodies[0].transform.getWorldPosition());

        float currentLength = d.length();

        // Check if we're over-extended
        if (Math.abs(currentLength - length) <= .0001f) {
            return;
        }

        Contact contact = cData.getContact();
        contact.setBodyData(rigidbodies[0], rigidbodies[1], 1, 0);

        // The contact normal depends on whether we're extending or compressing
        if (currentLength > length) {
            contact.contactNormal.set(d).normalize();
            contact.contactPoint.set(rigidbodies[0].transform.getWorldPosition())
                    .add(rigidbodies[1].transform.getWorldPosition()).mul(0.5f);
            contact.penetration = currentLength - length;
        } else {
            contact.contactNormal.set(d).normalize().mul(-1);
            contact.contactPoint.set(rigidbodies[0].transform.getWorldPosition())
                    .add(rigidbodies[1].transform.getWorldPosition()).mul(0.5f);
            contact.penetration = length - currentLength;
        }
    }
}
