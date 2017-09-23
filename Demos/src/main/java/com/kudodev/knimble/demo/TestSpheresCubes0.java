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

import com.kudodev.knimble.PhysicsSpace;
import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.colliders.BoxCollider;
import com.kudodev.knimble.colliders.Collider;
import com.kudodev.knimble.colliders.SphereCollider;
import com.kudodev.knimble.demo.utils.Mesh;
import com.kudodev.knimble.demo.utils.Shape;
import com.kudodev.knimble.demo.utils.ShapeUtils;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class TestSpheresCubes0 extends RenderLoop {

    public TestSpheresCubes0(String title, PhysicsSpace physicsSpace) {
        super(title, physicsSpace);
    }

    public static void main(String[] args) throws Exception {
        new TestSpheresCubes0("Display Test", new PhysicsSpace()).start();
    }

    Rigidbody r1;

    @Override
    protected List<Shape> initShapes(PhysicsSpace physicsSpace) {
        Mesh cube = ShapeUtils.createCubeMesh();
        Mesh sphere = ShapeUtils.createSphereMesh(2);
        List<Shape> shapes = new ArrayList<>();

        Rigidbody r0 = new Rigidbody(1000);
        Collider c0 = new SphereCollider(r0);
        Shape s0 = new Shape(sphere, c0);
        r0.setFriction(.8f);
        shapes.add(s0);
        s0.getColor().set(1, 0, 0, 1);
        r0.setAngularVelocity(0, 0, 15);
//        r1.getTransform().setPosition(0, 0, -5);
        r0.getTransform().setPosition(-0, .3f, -5f);
        r0.setLinearVelocity(.0f, 0, 0);
//        r0.setAngularVelocity(0, 0, 10);
//        r1.addLinearAcceleration(.5f, 0, 0);
//        r1.getTransform().rotate((float) Math.toRadians(5), new Vector3f(0, 1, 0));
//        r0.getTransform().rotate((float) Math.toRadians(45), new Vector3f(0, 1, 0));
        physicsSpace.addBody(r0, c0);

        r1 = new Rigidbody(1);
        Collider c1 = new BoxCollider(r1);
        Shape s1 = new Shape(cube, c1);
        s1.getColor().set(0, 0, 1, 1);
        shapes.add(s1);
        r1.getTransform().setPosition(2f, 0, -5);
        r1.setLinearVelocity(-1f, 0, 0);
//        r1.setAngularVelocity(0, 0, 15);
        r1.getTransform().rotate((float) Math.toRadians(-45), new Vector3f(0, 1, 0));
//        r1.getTransform().rotate((float) Math.toRadians(-45), new Vector3f(0, 0, 1));
        physicsSpace.addBody(r1, c1);

        // idle boxes
        Collider i0 = new BoxCollider();
        i0.getTransform().setScale(10, 10, 1);
        i0.getTransform().setPosition(0, 0, -10);
//        shapes.add(new Shape(cube, i0));

        return shapes;
    }

    float time = 0;

    @Override
    protected void update(float delta) {
        time += delta;
        if (time >= 20) {
//            r1.getTransform().setPosition(0, 3, -5);
        }
    }
}
