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
import com.kudodev.knimble.anchors.Constraint;
import com.kudodev.knimble.anchors.SpringAnchor;
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
public class DemoSpringAnchor0 extends RenderLoop {

    public DemoSpringAnchor0(String title, PhysicsSpace physicsSpace) {
        super(title, physicsSpace);
    }

    public static void main(String[] args) throws Exception {
        new DemoSpringAnchor0("Demo: Spring Anchor", new PhysicsSpace()).start();
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
        r0.getTransform().setPosition(0, 5f, -15f);
        physicsSpace.addBody(r0, c0);

        Constraint con0 = new SpringAnchor(r0, new Vector3f(0, 5f, -15f), 10f, 2f);
        r0.addConstraint(con0);

        physicsSpace.addForceGenerator(new GravityForce(0, -9, 0));

        return shapes;
    }

    @Override
    protected void update(float delta) {
    }
}
