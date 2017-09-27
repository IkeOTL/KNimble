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
import com.kudodev.knimble.colliders.CapsuleCollider;
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
public class DemoCapsuleSpheres1 extends RenderLoop {

    public DemoCapsuleSpheres1(String title, PhysicsSpace physicsSpace) {
        super(title, physicsSpace);
    }

    public static void main(String[] args) throws Exception {
        new DemoCapsuleSpheres1("Demo: Capsule/Sphere Collision", new PhysicsSpace()).start();
    }

    @Override
    protected List<Shape> init(PhysicsSpace physicsSpace) {

//        setWireframe(false);
        Mesh sphere = ShapeUtils.createSphereMesh(2);
        Mesh capsule = ShapeUtils.createCapsuleMesh(4, .5f);
        List<Shape> shapes = new ArrayList<>();

        Rigidbody r0 = new Rigidbody(100);
        Collider c0 = new CapsuleCollider(r0, 4, .5f);
        Shape s0 = new Shape(capsule, c0);
        shapes.add(s0);
        s0.getColor().set(1, 0, 0, 1);
        r0.getTransform().setPosition(0, -1f, -5f);
        r0.transform.rotate((float) Math.toRadians(80), new Vector3f(0, 0, 1));
        physicsSpace.addBody(r0, c0);

        Rigidbody r1 = new Rigidbody(1);
        Collider c1 = new SphereCollider(r1);
        Shape s1 = new Shape(sphere, c1);
        s1.getColor().set(0, 0, 1, 1);
        shapes.add(s1);
        r1.getTransform().setPosition(-1.5f, 2, -5);
        r1.addLinearAcceleration(0, -10, 0);
        physicsSpace.addBody(r1, c1);
        
        Rigidbody r2 = new Rigidbody(999999);
        Collider c2 = new SphereCollider(r2);
        Shape s2 = new Shape(sphere, c2);
        s2.getColor().set(0, 1, 0, 1);
        shapes.add(s2);
        r2.getTransform().setPosition(1.5f, 0, -5);
        r2.setAngularVelocity(0, 5, 0);
        physicsSpace.addBody(r2, c2);

        return shapes;
    }

    @Override
    protected void update(float delta) {
    }
}
