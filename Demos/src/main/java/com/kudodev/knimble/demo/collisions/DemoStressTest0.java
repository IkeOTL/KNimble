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
package com.kudodev.knimble.demo.collisions;

import com.kudodev.knimble.demo.utils.RenderLoop;
import com.kudodev.knimble.PhysicsSpace;
import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.colliders.BoxCollider;
import com.kudodev.knimble.colliders.CapsuleCollider;
import com.kudodev.knimble.colliders.Collider;
import com.kudodev.knimble.colliders.SphereCollider;
import com.kudodev.knimble.demo.utils.Mesh;
import com.kudodev.knimble.demo.utils.Shape;
import com.kudodev.knimble.demo.utils.shapes.ShapeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class DemoStressTest0 extends RenderLoop {

    public DemoStressTest0(String title, PhysicsSpace physicsSpace) {
        super(title, physicsSpace);
    }

    public static void main(String[] args) throws Exception {
        new DemoStressTest0("Demo: Sphere/Cube Collision", new PhysicsSpace()).start();
    }

    Mesh cube;
    Mesh sphere;
    Mesh capsule;

    @Override
    protected List<Shape> init(PhysicsSpace physicsSpace) {
        cube = ShapeUtils.createCubeMesh();
        sphere = ShapeUtils.createSphereMesh(2);
        capsule = ShapeUtils.createCapsuleMesh(1.5f, .5f);
        List<Shape> shapes = new ArrayList<>();

        Rigidbody r1 = new Rigidbody(0);
        Collider c1 = new BoxCollider(r1, new Vector3f(50f, 1f, 50f));
        Shape s1 = new Shape(cube, c1);
        s1.getColor().set(0, 0, 1, 1);
        shapes.add(s1);
        r1.getTransform().setPosition(0, -2, -15);
        physicsSpace.addBody(r1, c1);

        return shapes;
    }
    float time = 0;

    @Override
    protected void update(float delta) {
        time += delta;
        if (time < .5f) {
            return;
        }
        Rigidbody r0 = new Rigidbody(1);

        Collider c0;
        Shape s0;

        int i = ThreadLocalRandom.current().nextInt(3);
        switch (i) {
            case 0:
                c0 = new BoxCollider(r0);
                s0 = new Shape(cube, c0);
                break;
            case 1:
                c0 = new CapsuleCollider(r0, 1.5f, .5f);
                s0 = new Shape(capsule, c0);
                break;
            default:
                c0 = new SphereCollider(r0);
                s0 = new Shape(sphere, c0);
                break;
        }

        shapes.add(s0);
        s0.getColor().set((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
        r0.setRestitution((float) Math.random() * .9f);
//        r0.getTransform().setPosition(0, 5, (float) Math.random() - 15f);
        r0.getTransform().setPosition((float) Math.random(), 5, (float) Math.random() - 15f);
        r0.addLinearAcceleration(0, -10, 0);
        physicsSpace.addBody(r0, c0);
        time = 0;
    }
}
