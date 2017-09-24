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
import com.kudodev.knimble.contact.ContactCache;

/**
 *
 * @author IkeOTL
 */
public abstract class RigidbodyLink {

    protected final Rigidbody[] rigidbodies = new Rigidbody[2];

    public RigidbodyLink(Rigidbody a, Rigidbody b) {
        rigidbodies[0] = a;
        rigidbodies[1] = b;
    }

    public abstract void tick(ContactCache cData);
}
