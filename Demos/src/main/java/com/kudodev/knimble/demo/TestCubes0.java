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
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;
import org.lwjgl.util.par.ParShapes;
import org.lwjgl.util.par.ParShapesMesh;

/**
 *
 * @author IkeOTL
 */
public class TestCubes0 extends RenderLoop {

    public TestCubes0(String title, PhysicsSpace physicsSpace) {
        super(title, physicsSpace);
    }

    public static void main(String[] args) throws Exception {
        new TestCubes0("Display Test", new PhysicsSpace()).start();
    }

    @Override
    protected List<Shape> initShapes(PhysicsSpace physicsSpace) {
        Mesh cube = ShapeUtils.createCubeMesh();
        List<Shape> shapes = new ArrayList<>();
        Rigidbody r1 = new Rigidbody();
        Collider c1 = new BoxCollider(r1);
        shapes.add(new Shape(cube, c1));
//        r1.getTransform().setPosition(0, 0, -5);
        r1.getTransform().setPosition(-0, 0, -5);
//        r1.setLinearVelocity(.5f, 0, 0);
        r1.getTransform().rotate((float) Math.toRadians(45), new Vector3f(0, 1, 0));
        r1.getTransform().rotate((float) Math.toRadians(45), new Vector3f(0, 0, 1));
        physicsSpace.addBody(r1, c1);

        Rigidbody r2 = new Rigidbody();
        Collider c2 = new BoxCollider(r2);
        shapes.add(new Shape(cube, c2));
        r2.getTransform().setPosition(2, 0, -5);
        r2.setLinearVelocity(-.5f, 0, 0);
//        r2.getTransform().rotate((float) Math.toRadians(135), new Vector3f(0, 0, 1));
        physicsSpace.addBody(r2, c2);
        return shapes;
    }

    @Override
    protected void update(float delta) {

    }
}
