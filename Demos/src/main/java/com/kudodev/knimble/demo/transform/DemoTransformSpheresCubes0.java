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
package com.kudodev.knimble.demo.transform;

import com.kudodev.knimble.RigidbodyNodeTransform;
import com.kudodev.knimble.demo.utils.RenderLoop;
import com.kudodev.knimble.PhysicsSpace;
import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.RigidbodyTransform;
import com.kudodev.knimble.Transform;
import com.kudodev.knimble.colliders.BoxCollider;
import com.kudodev.knimble.colliders.Collider;
import com.kudodev.knimble.colliders.SphereCollider;
import com.kudodev.knimble.demo.utils.Mesh;
import com.kudodev.knimble.demo.utils.Shape;
import com.kudodev.knimble.demo.utils.shapes.ShapeUtils;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class DemoTransformSpheresCubes0 extends RenderLoop {

    public DemoTransformSpheresCubes0(String title, PhysicsSpace physicsSpace) {
        super(title, physicsSpace);
    }

    public static void main(String[] args) throws Exception {
        new DemoTransformSpheresCubes0("Demo: Sphere/Cube Collision", new PhysicsSpace()).start();
    }

    @Override
    protected List<Shape> init(PhysicsSpace physicsSpace) {
        Mesh cube = ShapeUtils.createCubeMesh();
        Mesh sphere = ShapeUtils.createSphereMesh(2);
        List<Shape> shapes = new ArrayList<>();

        RigidbodyNodeTransform n0 = new RigidbodyNodeTransform();
        Rigidbody r0 = new Rigidbody(n0, 100);
        Collider c0 = new SphereCollider(r0);
        Shape s0 = new Shape(sphere, c0);
        r0.setFriction(.8f);
        shapes.add(s0);
        s0.getColor().set(1, 0, 0, 1);
        r0.setAngularVelocity(0, 0, 15);
        r0.getTransform().setPosition(0, 0, -5f);
        physicsSpace.addBody(r0, c0);

        RigidbodyNodeTransform n1 = new RigidbodyNodeTransform();
        Rigidbody rx0 = new Rigidbody(n1, 100);
        Collider cx0 = new SphereCollider(rx0);
        Shape child0 = new Shape(sphere, cx0);
        child0.getCollider().getTransform().setPosition(2, 0, 0);
        n0.addChild(child0.getCollider().getTransform());
        shapes.add(child0);

        Rigidbody rx1 = new Rigidbody(100);
        Collider cx1 = new SphereCollider(rx1);
        Shape child1 = new Shape(sphere, cx1);
        child1.getCollider().getTransform().setPosition(0, 1, 0);
        n1.addChild(child1.getCollider().getTransform());
        shapes.add(child1);

        Rigidbody r1 = new Rigidbody(100);
        Collider c1 = new BoxCollider(r1);
        Shape s1 = new Shape(cube, c1);
        s1.getColor().set(0, 0, 1, 1);
        shapes.add(s1);
        r1.getTransform().setPosition(2f, 0, -5);
        r1.setLinearVelocity(-1f, 0, 0);
        r1.getTransform().rotate((float) Math.toRadians(-45), new Vector3f(0, 1, 0));
        physicsSpace.addBody(r1, c1);

        return shapes;
    }

    @Override
    protected void update(float delta) {
    }
}
