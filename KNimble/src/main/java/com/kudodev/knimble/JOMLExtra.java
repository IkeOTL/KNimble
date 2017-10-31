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
package com.kudodev.knimble;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class JOMLExtra {

    public static void setVec3Component(Vector3f v, int component, float f) throws IllegalArgumentException {
        switch (component) {
            case 0:
                v.x = f;
                break;
            case 1:
                v.y = f;
                break;
            case 2:
                v.z = f;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Vector3f getColumn(Matrix4f m, int i, Vector3f dest) {
        switch (i) {
            case 0:
                dest.set(m.m00(), m.m01(), m.m02());
                break;
            case 1:
                dest.set(m.m10(), m.m11(), m.m12());
                break;
            case 2:
                dest.set(m.m20(), m.m21(), m.m22());
                break;
            case 3:
                dest.set(m.m30(), m.m31(), m.m32());
                break;
        }
        return dest;
    }

    public static Matrix3f setColumn(Matrix3f dest, int i, Vector3f v) {
        for (int j = 0; j < 3; j++) {
            dest.set(i, j, v.get(j));
        }
        return dest;
    }
}
