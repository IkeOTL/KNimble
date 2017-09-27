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
import com.kudodev.knimble.colliders.Collider;
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
public class DemoCubes0 extends RenderLoop {

    public DemoCubes0(String title, PhysicsSpace physicsSpace) {
        super(title, physicsSpace);
    }

    public static void main(String[] args) throws Exception {
        new DemoCubes0("Demo: Cube/Cube Collision", new PhysicsSpace()).start();
    }

    @Override
    protected List<Shape> init(PhysicsSpace physicsSpace) {
        Mesh cube = ShapeUtils.createCubeMesh();
        List<Shape> shapes = new ArrayList<>();
        Rigidbody r0 = new Rigidbody(10);
        Collider c0 = new BoxCollider(r0);
        Shape s0 = new Shape(cube, c0);
        s0.getColor().set(1, 0, 0, 1);
        shapes.add(s0);
        r0.getTransform().setPosition(-0, 0, -5f);
        physicsSpace.addBody(r0, c0);

        Rigidbody r1 = new Rigidbody(1);
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
