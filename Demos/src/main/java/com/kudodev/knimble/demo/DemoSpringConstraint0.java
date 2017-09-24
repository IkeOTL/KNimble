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
package com.kudodev.knimble.demo;

import com.kudodev.knimble.demo.utils.RenderLoop;
import com.kudodev.knimble.PhysicsSpace;
import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.colliders.BoxCollider;
import com.kudodev.knimble.colliders.Collider;
import com.kudodev.knimble.colliders.SphereCollider;
import com.kudodev.knimble.constraints.BungeeConstraint;
import com.kudodev.knimble.constraints.DampedSpringConstraint;
import com.kudodev.knimble.links.CableLink;
import com.kudodev.knimble.links.RigidbodyLink;
import com.kudodev.knimble.links.RodLink;
import com.kudodev.knimble.demo.utils.Mesh;
import com.kudodev.knimble.demo.utils.Shape;
import com.kudodev.knimble.demo.utils.ShapeUtils;
import com.kudodev.knimble.generators.GravityForce;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class DemoSpringConstraint0 extends RenderLoop {

    public DemoSpringConstraint0(String title, PhysicsSpace physicsSpace) {
        super(title, physicsSpace);
    }

    public static void main(String[] args) throws Exception {
        new DemoSpringConstraint0("Display Test", new PhysicsSpace()).start();
    }

    @Override
    protected List<Shape> initShapes(PhysicsSpace physicsSpace) {
        Mesh sphere = ShapeUtils.createSphereMesh(2);
        List<Shape> shapes = new ArrayList<>();

        Rigidbody r0 = new Rigidbody(10);
        Collider c0 = new SphereCollider(r0, 2);
        Shape s0 = new Shape(sphere, c0);
        shapes.add(s0);
        s0.getColor().set(1, 0, 0, 1);
//        r0.setAngularVelocity(0, 0, 15);
//        r1.getTransform().setPosition(0, 0, -5);
        r0.getTransform().setPosition(0, 5f, -15f);
        r0.setLinearVelocity(0f, 0, 0);
//        r0.setAngularVelocity(0, 0, 10);
//        r1.addLinearAcceleration(.5f, 0, 0);
//        r1.getTransform().rotate((float) Math.toRadians(5), new Vector3f(0, 1, 0));
//        r0.getTransform().rotate((float) Math.toRadians(45), new Vector3f(0, 1, 0));
        physicsSpace.addBody(r0, c0);

        DampedSpringConstraint con0 = new DampedSpringConstraint(r0, new Vector3f(0, 5f, -15f), 5f, 2f);

        r0.addConstraint(con0);

        physicsSpace.addForceGenerator(new GravityForce(0, -9, 0));

        return shapes;
    }

    @Override
    protected void update(float delta) {
    }
}
