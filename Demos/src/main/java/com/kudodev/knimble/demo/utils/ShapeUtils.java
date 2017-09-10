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

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.util.par.ParShapes;
import org.lwjgl.util.par.ParShapesMesh;

/**
 *
 * @author IkeOTL
 */
public class ShapeUtils {

    public static Mesh createSphereMesh(int level) {
        ParShapesMesh parShape = ParShapes.par_shapes_create_subdivided_sphere(level);
        ParShapes.par_shapes_scale(parShape, .5f, .5f, .5f);

        short numIndices = (short) (parShape.ntriangles() * 3);
        FloatBuffer verts = parShape.points(parShape.npoints() * 3);
        ShortBuffer indices = parShape.triangles(numIndices);

        Mesh m = new Mesh(numIndices, verts, indices);

        ParShapes.par_shapes_free_mesh(parShape);

        return m;
    }

    public static Mesh createCubeMesh() {
        ParShapesMesh parShape = ParShapes.par_shapes_create_cube();
        ParShapes.par_shapes_translate(parShape, -.5f, -.5f, -.5f);

        short numIndices = (short) (parShape.ntriangles() * 3);
        FloatBuffer verts = parShape.points(parShape.npoints() * 3);
        ShortBuffer indices = parShape.triangles(numIndices);

        Mesh m = new Mesh(numIndices, verts, indices);

        ParShapes.par_shapes_free_mesh(parShape);

        return m;
    }
}
