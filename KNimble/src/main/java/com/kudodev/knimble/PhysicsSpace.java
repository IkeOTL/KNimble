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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IkeOTL
 */
public class PhysicsSpace {

    private List<Rigidbody> simpleBodies = new ArrayList<>();
    private List<Collider> colliders = new ArrayList<>();

    public void tick(float delta) {
        for (Rigidbody r : simpleBodies) {
            r.integrate(delta);
        }

        for (int i = 0; i < colliders.size(); i++) {
            for (int j = i + 1; j < colliders.size(); j++) {
                Collider c1 = colliders.get(i);
                Collider c2 = colliders.get(j);
                if (!c1.intersectsWith(c2)) {
                    continue;
                }

                Rigidbody r1 = c1.getRigidbody();
                Rigidbody r2 = c2.getRigidbody();
                if (r1 == null || r2 == null) {
                    continue;
                }

                r1.setVelocity(0, 0, 0);
                r2.setVelocity(0, 0, 0);
            }
        }
    }

    public List<Collider> getColliders() {
        return colliders;
    }

    public void addRigidbody(Rigidbody r) {

    }
}
