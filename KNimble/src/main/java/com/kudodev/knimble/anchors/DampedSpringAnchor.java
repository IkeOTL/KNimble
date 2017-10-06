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
package com.kudodev.knimble.anchors;

import com.kudodev.knimble.Rigidbody;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class DampedSpringAnchor extends Constraint {

    private Vector3f anchor;

    private float springConstant;
    private float damping;

    public DampedSpringAnchor(Rigidbody rigidbody, Vector3f anchor, float springConstant, float damping) {
        this.springConstant = springConstant;
        this.damping = damping;
        this.anchor = anchor;
    }

    @Override
    public void tick(Rigidbody rigidbody, float delta) {
        // Calculate the relative position of the particle to the anchor
        Vector3f position = new Vector3f(rigidbody.getTransform().getWorldPosition());
        position.sub(anchor);

        // Calculate the constants and check they are in bounds.
        float gamma = 0.5f * (float) Math.sqrt(4 * springConstant - damping * damping);

        if (gamma == 0.0f) {
            return;
        }

        Vector3f velocity = new Vector3f(rigidbody.getLinearVelocity());
        Vector3f c = new Vector3f(position);
        c.mul(damping / (2.0f * gamma));
        c.fma((1.0f / gamma), velocity);

        // Calculate the target position
        Vector3f target = new Vector3f(position);
        target.mul((float) Math.cos(gamma * delta));
        target.fma((float) Math.sin(gamma * delta), c);
        target.mul((float) Math.exp(-0.5f * delta * damping));

        // Calculate the resulting acceleration and therefore the force
        Vector3f accel = new Vector3f(target).sub(position);
        accel.mul(1.0f / (delta * delta));
        velocity.mul(1f / delta);
        accel.sub(velocity);

        rigidbody.addLinearForce(accel.mul(rigidbody.getMass()));
    }

}
