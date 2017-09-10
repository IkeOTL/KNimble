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

import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.colliders.SphereCollider;
import org.joml.Intersectionf;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import static org.lwjgl.system.MemoryUtil.NULL;
import org.lwjgl.util.par.ParShapesMesh;

/**
 *
 * @author IkeOTL
 */
public class Test {

    public static void main(String[] args) {
//        Rigidbody r1 = new Rigidbody();
//        r1.getTransform().setPosition(0, 0, 0);
//        SphereCollider a = new SphereCollider(r1, 10);
//
//        Rigidbody r2 = new Rigidbody();
//        r2.getTransform().setPosition(20f, 0, 0);
//        SphereCollider b = new SphereCollider(r2, 10);
//        ParShapesMesh.create(NULL);
//        System.out.println("result: " + a.intersectsWith(b));

        float a = (float) (Math.sqrt(2.0 * 2.0 + 2.0 * 2.0) + Math.sqrt(0.5 * 0.5 + 0.5 * 0.5));
        float EPSILON = 1E-4f;
        Vector3f c0 = new Vector3f(0, 0, a - EPSILON);
        Vector3f hs0 = new Vector3f(0.5f, 0.5f, 0.5f);
        Vector3f c1 = new Vector3f(0, 0, 0);
        Vector3f hs1 = new Vector3f(2, 0.5f, 2);
        Matrix3f m = new Matrix3f();//.rotateY((float) Math.toRadians(0));
        Vector3f ux0 = m.getColumn(0, new Vector3f());
        Vector3f uy0 = m.getColumn(1, new Vector3f());
        Vector3f uz0 = m.getColumn(2, new Vector3f());
        Vector3f ux1 = m.getColumn(0, new Vector3f());
        Vector3f uy1 = m.getColumn(1, new Vector3f());
        Vector3f uz1 = m.getColumn(2, new Vector3f());
        boolean intersects = Intersectionf.testObOb(c0, ux0, uy0, uz0, hs0, c1, ux1, uy1, uz1, hs1);
        System.out.println(intersects); // <- they DO intersect
        c0 = new Vector3f(0, 0, a + EPSILON);
        intersects = Intersectionf.testObOb(c0, ux0, uy0, uz0, hs0, c1, ux1, uy1, uz1, hs1);
        System.out.println(intersects); // <- they do not intersect
    }
}
