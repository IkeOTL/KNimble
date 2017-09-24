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
package com.kudodev.knimble.generators;

import com.kudodev.knimble.Rigidbody;
import org.joml.Vector3f;

/**
 *
 * @author Ike <Admin@KudoDEV.com>
 */
public class GravityForce implements ForceGenerator {

    private Vector3f gravity = new Vector3f();

    public GravityForce(float x, float y, float z) {
        gravity.set(x, y, z);
    }

    @Override
    public void tick(Rigidbody body, float delta) {
        // Apply the mass-scaled force to the body 
        body.addLinearForce(
                gravity.x * body.getMass(),
                gravity.y * body.getMass(),
                gravity.z * body.getMass());
    }

}
