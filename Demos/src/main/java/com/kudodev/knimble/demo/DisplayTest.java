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
import com.kudodev.knimble.colliders.Collider;
import com.kudodev.knimble.colliders.SphereCollider;
import com.kudodev.knimble.demo.utils.Mesh;
import com.kudodev.knimble.demo.utils.Shape;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.par.ParShapes;
import org.lwjgl.util.par.ParShapesMesh;

/**
 *
 * @author IkeOTL
 */
public class DisplayTest extends RenderLoop {

    public DisplayTest(String title, PhysicsSpace physicsSpace) {
        super(title, physicsSpace);
    }

    public static void main(String[] args) throws Exception {
        new DisplayTest("Display Test", new PhysicsSpace()).start();
    }

    @Override
    protected List<Shape> initShapes(PhysicsSpace physicsSpace) {
        Mesh sphere = createSphere(2);
        List<Shape> shapes = new ArrayList<>();
        Rigidbody r1 = new Rigidbody();
        Collider c1 = new SphereCollider(r1, 1);
        shapes.add(new Shape(sphere, c1));
        r1.setPosition(-2, 0, -5);
        r1.setVelocity(.5f, 0, 0);
        physicsSpace.addBody(r1, c1);

        Rigidbody r2 = new Rigidbody();
        Collider c2 = new SphereCollider(r2, 1);
        shapes.add(new Shape(sphere, c2));
        r2.setPosition(2, 0, -5);
        r2.setVelocity(-.5f, 0, 0);
        physicsSpace.addBody(r2, c2);
        return shapes;
    }

    private Mesh createSphere(int level) {
        ParShapesMesh parShape = ParShapes.par_shapes_create_subdivided_sphere(level);

        short numIndices = (short) (parShape.ntriangles() * 3);
        FloatBuffer verts = parShape.points(parShape.npoints() * 3);
        ShortBuffer indices = parShape.triangles(numIndices);

        Mesh m = new Mesh(numIndices, verts, indices);

        ParShapes.par_shapes_free_mesh(parShape);

        return m;
    }
}
