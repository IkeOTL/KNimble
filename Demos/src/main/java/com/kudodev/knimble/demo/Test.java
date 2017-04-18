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
import com.kudodev.knimble.colliders.CubeCollider;
import com.kudodev.knimble.colliders.Collider;
import com.kudodev.knimble.colliders.SphereCollider;
import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.system.MemoryUtil.NULL;
import org.lwjgl.util.par.ParShapesMesh;

/**
 *
 * @author IkeOTL
 */
public class Test {

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            for (int j = i + 1; j < 5; j++) {
                System.out.print(i + " " + j + ", ");
            }
            System.out.println("");
        }
        Rigidbody r1 = new Rigidbody();
        r1.setPosition(0, 0, 0);
        SphereCollider a = new SphereCollider(r1, 10);

        Rigidbody r2 = new Rigidbody();
        r2.setPosition(20f, 0, 0);
        SphereCollider b = new SphereCollider(r2, 10);
ParShapesMesh.create(NULL);
        System.out.println(a.intersectsWith(b));
    }
}
