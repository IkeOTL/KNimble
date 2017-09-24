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
package com.kudodev.knimble.constraints;

import com.kudodev.knimble.Rigidbody;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class BungeeConstraint extends Constraint {

    private Vector3f anchor;

    private float springConstant;
    private float restLength;

    public BungeeConstraint(Rigidbody rigidbody, Vector3f anchor, float springConstant, float restLength) {
        this.springConstant = springConstant;
        this.restLength = restLength;
        this.anchor = anchor;
    }

    @Override
    public void tick(Rigidbody rigidbody, float delta) {
        // Calculate the vector of the spring

        Vector3f force = new Vector3f(anchor);

        force.sub(rigidbody.transform.getWorldPosition());

        // Check if the bungee is compressed
        float magnitude = force.length();

        if (magnitude <= restLength) {
            return;
        }

        // Calculate the magnitude of the force
        magnitude = springConstant * (restLength - magnitude);

        // Calculate the final force and apply it
        force.normalize();

        force.mul(-magnitude);
        force.mul(rigidbody.getMass());
        rigidbody.addLinearForce(force);
    }

}
