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
import com.kudodev.knimble.colliders.Collider;
import com.kudodev.knimble.colliders.SphereCollider;
import com.kudodev.knimble.demo.utils.Mesh;
import com.kudodev.knimble.demo.utils.Shape;
import com.kudodev.knimble.demo.utils.ShapeUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author IkeOTL
 */
public class DemoSpheres0 extends RenderLoop {

    public DemoSpheres0(String title, PhysicsSpace physicsSpace) {
        super(title, physicsSpace);
    }

    public static void main(String[] args) throws Exception {
        new DemoSpheres0("Demo: Sphere/Sphere Collision", new PhysicsSpace()).start();
    }

    @Override
    protected List<Shape> initShapes(PhysicsSpace physicsSpace) {
        Mesh sphere = ShapeUtils.createSphereMesh(2);
        List<Shape> shapes = new ArrayList<>();

        Rigidbody r0 = new Rigidbody(80);
        Collider c0 = new SphereCollider(r0, 2);
        Shape s0 = new Shape(sphere, c0);
        shapes.add(s0);
        s0.getColor().set(1, 0, 0, 1);
        r0.getTransform().setPosition(-2, 0f, -5f);
        physicsSpace.addBody(r0, c0);

        Rigidbody r1 = new Rigidbody(1);
        Collider c1 = new SphereCollider(r1, .5f);
        Shape s1 = new Shape(sphere, c1);
        s1.getColor().set(0, 0, 1, 1);
        shapes.add(s1);
        r1.getTransform().setPosition(2f, 0, -5);
        r1.setLinearVelocity(-1f, 0, 0);
        r1.setAngularVelocity(0, 0, 15);
        physicsSpace.addBody(r1, c1);

        Rigidbody r2 = new Rigidbody(1);
        Collider c2 = new SphereCollider(r2, .5f);
        Shape s2 = new Shape(sphere, c2);
        s2.getColor().set(0, 0, 1, 1);
        shapes.add(s2);
        r2.getTransform().setPosition(.5f, 1.2f, -5f);
        physicsSpace.addBody(r2, c2);

        return shapes;
    }

    @Override
    protected void update(float delta) {
    }
}
