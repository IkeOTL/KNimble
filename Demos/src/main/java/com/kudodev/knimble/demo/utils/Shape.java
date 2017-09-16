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
package com.kudodev.knimble.demo.utils;

import com.kudodev.knimble.colliders.Collider;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 *
 * @author IkeOTL
 */
public class Shape {

    protected final Mesh mesh;
    protected final Collider collider;
    protected Vector4f color = new Vector4f(1);

    public Shape(Mesh mesh, Collider collider) {
        this.mesh = mesh;
        this.collider = collider;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Collider getCollider() {
        return collider;
    }

    public Vector4f getColor() {
        return color;
    }
}
