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
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
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
        FloatBuffer normals = generateNormals(points, numPoints, indices, numIndices);

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

    private static FloatBuffer generateNormals(FloatBuffer points, int numPoints, ShortBuffer indices, short numIndices) {
        float[] vertArray = new float[numPoints * 3];
        points.get(vertArray);
        points.flip();

        short[] indicesArray = new short[numIndices];
        indices.get(indicesArray);
        indices.flip();

        Vector3f p1 = new Vector3f();
        Vector3f p2 = new Vector3f();
        Vector3f p3 = new Vector3f();
        Vector3f u = new Vector3f();
        Vector3f v = new Vector3f();

        List<Vector3f> normals = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            normals.add(new Vector3f());
        }
        for (int i = 0; i < numIndices; i += 3) {
            int index1 = indicesArray[i];
            int index2 = indicesArray[i + 1];
            int index3 = indicesArray[i + 2];

            p1.set(vertArray[index1 * 3], vertArray[index1 * 3 + 1], vertArray[index1 * 3 + 2]);
            p2.set(vertArray[index2 * 3], vertArray[index2 * 3 + 1], vertArray[index2 * 3 + 2]);
            p3.set(vertArray[index3 * 3], vertArray[index3 * 3 + 1], vertArray[index3 * 3 + 2]);

            u.set(p2).sub(p1);
            v.set(p3).sub(p1);
            u.cross(v);

            Vector3f n1 = normals.get(index1);
            Vector3f n2 = normals.get(index2);
            Vector3f n3 = normals.get(index3);

            n1.add(u);
            n2.add(u);
            n3.add(u);
        }
        FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(numPoints * 3);
        for (Vector3f n : normals) {
            n.normalize();
            normalsBuffer.put(n.x);
            normalsBuffer.put(n.y);
            normalsBuffer.put(n.z);
        }
        normalsBuffer.flip();
        return normalsBuffer;
    }
}
