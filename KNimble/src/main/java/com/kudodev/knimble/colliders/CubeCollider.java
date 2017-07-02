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
package com.kudodev.knimble.colliders;

import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.contact.Contact;

/**
 *
 * @author IkeOTL
 */
public class CubeCollider extends Collider {

    public CubeCollider(Rigidbody rigidbody) {
        super(ColliderType.CUBE, rigidbody);
    }

    @Override
    public Contact intersectsWith(SphereCollider other) {

        return null;
    }

    @Override
    public Contact intersectsWith(CubeCollider other) {

        return null;
    }

}
