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
package com.kudodev.knimble.colliders;

import com.kudodev.knimble.JOMLExtra;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
class Intersection {

    public static float getDistanceSq(Vector3f a, Vector3f b, Vector3f c) {
        Vector3f ab = new Vector3f(b).sub(a);
        Vector3f ac = new Vector3f(c).sub(a);
        Vector3f bc = new Vector3f(c).sub(b);

        float e = ac.dot(ab);

        // Handle cases where c projects outside ab
        if (e <= 0.0f) {
            return ac.dot(ac);
        }

        float f = ab.dot(ab);
        if (e >= f) {
            return bc.dot(bc);
        }
        // Handle cases where c projects onto ab
        return ac.dot(ac) - e * e / f;
    }

    public static float getDistanceSq(SphereCollider a, SphereCollider b) {
        float distSq = a.getTransform().getWorldPosition()
                .distanceSquared(b.getTransform().getWorldPosition());
        float radiusA = a.getRadius();
        float radiusB = b.getRadius();
        return distSq - (radiusA + radiusB) * (radiusA + radiusB);
    }

    public static float getDistance(SphereCollider a, SphereCollider b) {
        float dist = a.getTransform().getWorldPosition().distance(b.getTransform().getWorldPosition());
        return dist - (a.getRadius() + b.getRadius());
    }

    public static float getDistanceSq(BoxCollider b, Vector3f p, Vector3f out) {
        getClosestPoint(b, p, out);
        Vector3f v = new Vector3f(out).sub(p);
        return v.dot(v);
    }

    public static Vector3f getClosestPoint(BoxCollider b, Vector3f p, Vector3f out) {
        // Start result at center of box; make steps from there
        out.set(b.getTransform().getWorldPosition());
        Vector3f distance = new Vector3f(p).sub(out);

        Matrix4f m = b.getTransform().getTransMatrix();
        Vector3f axis = new Vector3f();
        for (int i = 0; i < 3; i++) {
            JOMLExtra.getColumn(m, i, axis);
            float dist = distance.dot(axis);

            // If distance farther than the box extents, clamp to the box
            float e = b.getExtents().get(i);
            if (dist > e) {
                dist = e;
            }
            if (dist < -e) {
                dist = -e;
            }

            // Step that distance along the axis to get world coordinate
            axis.mul(dist);
            out.add(axis);
        }
        return out;
    }

    public static float getDistanceSq(BoxCollider b, Vector3f p) {
        Vector3f v = new Vector3f(p).sub(b.getTransform().getWorldPosition());
        float sqDist = 0.0f;

        Matrix4f m = b.getTransform().getTransMatrix();
        Vector3f axis = new Vector3f();
        for (int i = 0; i < 3; i++) {
            JOMLExtra.getColumn(m, i, axis);
            // Project vector from box center to p on each axis, getting the distance
            // of p along that axis, and count any excess distance outside box extents
            float d = v.dot(axis);
            float excess = 0.0f;
            float e = b.getExtents().get(i);
            if (d < -e) {
                excess = d + e;
            } else if (d > e) {
                excess = d - e;
            }
            sqDist += excess * excess;
        }
        return sqDist;
    }
}
