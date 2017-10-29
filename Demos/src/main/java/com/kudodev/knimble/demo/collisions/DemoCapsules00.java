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
import com.kudodev.knimble.generators.GravityForce;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class DemoCapsules00 extends RenderLoop {

    public DemoCapsules00(String title, PhysicsSpace physicsSpace) {
        super(title, physicsSpace);
    }

    public static void main(String[] args) throws Exception {
        new DemoCapsules00("Demo: Capsule/Sphere Collision", new PhysicsSpace()).start();
    }

    @Override
    protected List<Shape> init(PhysicsSpace physicsSpace) {

//        setWireframe(false);
        Mesh capsule = ShapeUtils.createCapsuleMesh(4, .5f);
        List<Shape> shapes = new ArrayList<>();

        physicsSpace.addForceGenerator(new GravityForce(0, -10, 0));

        Rigidbody r1 = new Rigidbody(1);
        Collider c1 = new CapsuleCollider(r1, 4, .5f);
        Shape s1 = new Shape(capsule, c1);
        s1.getColor().set(0, 0, 1, 1);
        shapes.add(s1);
        r1.getTransform().setPosition(0, 2, -5.1f);
        r1.getTransform().rotate((float)Math.toRadians(90), new Vector3f(0, 0, 1));
//        r1.getTransform().rotate((float)Math.toRadians(90), new Vector3f(1, 1, 0));
        physicsSpace.addBody(r1, c1);

        Rigidbody r2 = new Rigidbody(0);
        Collider c2 = new CapsuleCollider(r2, 4, .5f);
        Shape s2 = new Shape(capsule, c2);
        s2.getColor().set(0, 1, 0, 1);
        shapes.add(s2);
        r2.getTransform().setPosition(0, 0, -5);
        r2.getTransform().rotate((float)Math.toRadians(90), new Vector3f(0, 0, 1));
        physicsSpace.addBody(r2, c2);

        return shapes;
    }

    @Override
    protected void update(float delta) {
    }
}
