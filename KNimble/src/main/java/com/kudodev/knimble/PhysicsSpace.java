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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IkeOTL
 */
public class PhysicsSpace {

    private List<Rigidbody> simpleBodies = new ArrayList<>();
    private List<Rigidbody> colliderBodies = new ArrayList<>();

    public void tick(float delta) {
        for (Rigidbody r : simpleBodies) {
            r.integrate(delta);
        }

        for (int i = 0; i < colliderBodies.size(); i++) {
            for (int j = i + 1; j < colliderBodies.size(); j++) {
                Rigidbody r1 = colliderBodies.get(i);
                Rigidbody r2 = colliderBodies.get(j);
                if (!r1.getCollider().intersectsWith(r2.getCollider())) {
                    continue;
                }
                r1.setVelocity(0, 0, 0);
                r2.setVelocity(0, 0, 0);
            }
        }
    }

}
