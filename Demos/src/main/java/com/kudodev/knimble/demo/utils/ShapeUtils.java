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
package com.kudodev.knimble.demo.utils.shapes;

import com.kudodev.knimble.demo.utils.Mesh;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.par.ParShapes;
import org.lwjgl.util.par.ParShapesMesh;

/**
 *
 * @author IkeOTL
 */
public class ShapeUtils {

    public static Mesh createCapsuleMesh(float height, float radius) {
        ParShapesMesh cylinder = createCylinderParShape(height, radius);
//
        ParShapesMesh s0 = ParShapes.par_shapes_create_hemisphere(8, 6);
        ParShapes.par_shapes_scale(s0, radius, radius, radius);
        ParShapes.par_shapes_translate(s0, 0, height * .5f, 0);
        ParShapes.par_shapes_merge_and_free(cylinder, s0);

        ParShapesMesh s1 = ParShapes.par_shapes_create_hemisphere(8, 6);
        ParShapes.par_shapes_scale(s1, radius, radius, radius);
        ParShapes.par_shapes_rotate(s1, (float) Math.toRadians(180), new float[]{0, 0, 1});
        ParShapes.par_shapes_translate(s1, 0, height * -.5f, 0);

        ParShapes.par_shapes_merge_and_free(cylinder, s1);

        short numIndices = (short) (cylinder.ntriangles() * 3);
        int numPoints = cylinder.npoints();
        FloatBuffer points = cylinder.points(numPoints * 3);
        FloatBuffer normals = cylinder.normals(numPoints * 3);
        ShortBuffer indices = cylinder.triangles(numIndices);

        Mesh m = new Mesh(indices, numIndices, interleaveBuffers(numPoints, points, normals));

        ParShapes.par_shapes_free_mesh(cylinder);

        return m;
    }

    public static ParShapesMesh createCylinderParShape(float height, float radius) {
        ParShapesMesh cylinder = ParShapes.par_shapes_create_cylinder(12, 1);
        ParShapes.par_shapes_rotate(cylinder, (float) Math.toRadians(90), new float[]{1, 0, 0});
        ParShapes.par_shapes_scale(cylinder, radius, height, radius);
        ParShapes.par_shapes_translate(cylinder, 0, height * .5f, 0);
        return cylinder;
    }

    public static Mesh createSphereMesh(int level) {
        ParShapesMesh parShape = ParShapes.par_shapes_create_subdivided_sphere(level);
        ParShapes.par_shapes_scale(parShape, .5f, .5f, .5f);

        short numIndices = (short) (parShape.ntriangles() * 3);
        int numPoints = parShape.npoints();
        FloatBuffer points = parShape.points(numPoints * 3);
        FloatBuffer normals = parShape.normals(numPoints * 3);
        ShortBuffer indices = parShape.triangles(numIndices);

        Mesh m = new Mesh(indices, numIndices, interleaveBuffers(numPoints, points, normals));

        ParShapes.par_shapes_free_mesh(parShape);

        return m;
    }

    public static Mesh createCubeMesh() {
        ParShapesMesh parShape = ParShapes.par_shapes_create_cube();
        ParShapes.par_shapes_translate(parShape, -.5f, -.5f, -.5f);

        short numIndices = (short) (parShape.ntriangles() * 3);
        ShortBuffer indices = parShape.triangles(numIndices);

        int numPoints = parShape.npoints();
        FloatBuffer points = parShape.points(numPoints * 3);
        ParShapes.par_shapes_compute_normals(parShape);
        FloatBuffer normals = parShape.normals(numPoints * 3);

        Mesh m = new Mesh(indices, numIndices, interleaveBuffers(numPoints, points, normals));

        ParShapes.par_shapes_free_mesh(parShape);

        return m;
    }

    private static FloatBuffer interleaveBuffers(int numPoints, FloatBuffer pointBuf, FloatBuffer normalBuf) {
        FloatBuffer outBuf = BufferUtils.createFloatBuffer(numPoints * (3 + 3));

        float[] vertArray = new float[numPoints * 3];
        pointBuf.get(vertArray);
        pointBuf.flip();

        float[] normArray = new float[numPoints * 3];
        normalBuf.get(normArray);
        normalBuf.flip();

        for (int i = 0; i < numPoints; i++) {
            outBuf.put(vertArray, i * 3, 3);
            outBuf.put(normArray, i * 3, 3);
        }
        outBuf.flip();
        return outBuf;
    }
}
