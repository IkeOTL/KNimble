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

import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.contact.Contact;
import com.kudodev.knimble.contact.ContactCache;
import org.joml.Intersectionf;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Math;

/**
 *
 * @author IkeOTL
 */
public class BoxCollider extends Collider {

    private final Vector3f extents;

    public BoxCollider(Rigidbody rigidbody, Vector3f halfExtents) {
        super(ColliderType.CUBE, rigidbody);
        this.extents = halfExtents;
    }

    public BoxCollider(Rigidbody rigidbody) {
        this(rigidbody, new Vector3f(.5f));
    }

    public BoxCollider() {
        this(new Vector3f(.5f));
    }

    public BoxCollider(Vector3f halfExtents) {
        this(null, halfExtents);
    }

    @Override
    public boolean intersectsWith(SphereCollider other) {
        return false;
    }

    @Override
    public boolean intersectsWith(BoxCollider other) {
////        Matrix4f m0 = transform.getTransMatrix();
//        Matrix3f m0 = transform.getTransMatrix().get3x3(new Matrix3f());
//        Vector3f v0 = transform.getWorldPosition();
//
////        Matrix4f m1 = other.transform.getTransMatrix();
//        Matrix3f m1 = other.transform.getTransMatrix().get3x3(new Matrix3f());
//        Vector3f v1 = other.transform.getWorldPosition();
//
//        return Intersectionf.testObOb(
//                // this box
//                v0.x, v0.y, v0.z,
//                // change to variable instead of functions
//                m0.m00, m0.m01, m0.m02, // col 0
//                m0.m10, m0.m11, m0.m12, // col 1
//                m0.m20, m0.m21, m0.m22, // col 2
//                extents.x, extents.y, extents.z,
//                // other box
//                v1.x, v1.y, v1.z,
//                m1.m00, m1.m01, m1.m02, // col 0
//                m1.m10, m1.m11, m1.m12, // col 1
//                m1.m20, m1.m21, m1.m22, // col 2
//                other.extents.x, other.extents.y, other.extents.z);

        // Find the vector between the two centres
        Vector3f toCenter = new Vector3f(other.transform.getWorldPosition()).sub(transform.getWorldPosition());
        // TODO: When JOML updates Matrix4f variable access level switch to use the mat4
//        Matrix4f m = transform.getTransMatrix();
//        Matrix4f o = other.transform.getTransMatrix();
        Matrix3f m = transform.getTransMatrix().get3x3(new Matrix3f());
        Matrix3f o = other.transform.getTransMatrix().get3x3(new Matrix3f());
        return (overlapOnAxis(other, toCenter, m.m00, m.m01, m.m02)
                && overlapOnAxis(other, toCenter, m.m10, m.m11, m.m12)
                && overlapOnAxis(other, toCenter, m.m20, m.m21, m.m22)
                && overlapOnAxis(other, toCenter, o.m00, o.m01, o.m02)
                && overlapOnAxis(other, toCenter, o.m10, o.m11, o.m12)
                && overlapOnAxis(other, toCenter, o.m20, o.m21, o.m22)
                && overlapOnAxis(other, toCenter, m.m01 * o.m02 - m.m02 * o.m01, m.m02 * o.m00 - m.m00 * o.m02, m.m00 * o.m01 - m.m01 * o.m00)
                && overlapOnAxis(other, toCenter, m.m01 * o.m12 - m.m02 * o.m11, m.m02 * o.m10 - m.m00 * o.m12, m.m00 * o.m11 - m.m01 * o.m10)
                && overlapOnAxis(other, toCenter, m.m01 * o.m22 - m.m02 * o.m21, m.m02 * o.m20 - m.m00 * o.m22, m.m00 * o.m21 - m.m01 * o.m20)
                && overlapOnAxis(other, toCenter, m.m11 * o.m02 - m.m12 * o.m01, m.m12 * o.m00 - m.m10 * o.m02, m.m10 * o.m01 - m.m11 * o.m00)
                && overlapOnAxis(other, toCenter, m.m11 * o.m12 - m.m12 * o.m11, m.m12 * o.m10 - m.m10 * o.m12, m.m10 * o.m11 - m.m11 * o.m10)
                && overlapOnAxis(other, toCenter, m.m11 * o.m22 - m.m12 * o.m21, m.m12 * o.m20 - m.m10 * o.m22, m.m10 * o.m21 - m.m11 * o.m20)
                && overlapOnAxis(other, toCenter, m.m21 * o.m02 - m.m22 * o.m01, m.m22 * o.m00 - m.m20 * o.m02, m.m20 * o.m01 - m.m21 * o.m00)
                && overlapOnAxis(other, toCenter, m.m21 * o.m12 - m.m22 * o.m11, m.m22 * o.m10 - m.m20 * o.m12, m.m20 * o.m11 - m.m21 * o.m10)
                && overlapOnAxis(other, toCenter, m.m21 * o.m22 - m.m22 * o.m21, m.m22 * o.m20 - m.m20 * o.m22, m.m20 * o.m21 - m.m21 * o.m20));
    }

    private float transformToAxis(BoxCollider box, float x, float y, float z) {
        // TODO: JOML updates Matrix4f variable access level switch to use the mat4
//        Matrix4f m = box.getTransform().getTransMatrix(); 
        Matrix3f m = box.transform.getTransMatrix().get3x3(new Matrix3f());
        return box.extents.x * Math.abs(x * m.m00 + y * m.m01 + z * m.m02)
                + box.extents.y * Math.abs(x * m.m10 + y * m.m11 + z * m.m12)
                + box.extents.z * Math.abs(x * m.m20 + y * m.m21 + z * m.m22);
    }

    private boolean overlapOnAxis(BoxCollider other, Vector3f distanceFromCenters, float x, float y, float z) {
        return penetrationOnAxis(other, distanceFromCenters, x, y, z) >= 0;
    }

    private float penetrationOnAxis(BoxCollider other, Vector3f distanceFromCenters, float x, float y, float z) {
        // normalize
        // TODO: test if necessary
        float mag = new Vector3f(x, y, z).length();
        if (mag != 0) {
            x /= mag;
            y /= mag;
            z /= mag;
        }

        // Project the half-size of one onto axis 
        float oneProject = transformToAxis(this, x, y, z);
        float twoProject = transformToAxis(other, x, y, z);
        // Project this onto the axis
        return oneProject + twoProject - Math.abs(distanceFromCenters.dot(x, y, z));
    }

    @Override
    public void createCollision(SphereCollider other, ContactCache contactCache) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class PenetrationData {

        float penetration;
        int bestAxis;
    }

    @Override
    public void createCollision(BoxCollider other, ContactCache contactCache) {
//        if (!intersectsWith(other, null)) {
//            return;
//        }
        // Find the vector between the two centres
        Vector3f toCenter = new Vector3f(other.transform.getWorldPosition()).sub(transform.getWorldPosition());
        PenetrationData penData = new PenetrationData();
        penData.penetration = Float.MAX_VALUE;
        penData.bestAxis = 0xFFFFFF;

        // Now we check each axes, returning if it gives us
        // a separating axis, and keeping track of the axis with
        // the smallest penetration otherwise.
        // Store the best axis-major, in case we run into almost
        // parallel edge collisions later
        // We start assuming there is no contact
        // TODO: When JOML updates Matrix4f variable access level switch to use the mat4
//        Matrix4f m = transform.getTransMatrix();
//        Matrix4f o = other.transform.getTransMatrix();
        Matrix3f m = transform.getTransMatrix().get3x3(new Matrix3f());
        Matrix3f o = other.transform.getTransMatrix().get3x3(new Matrix3f());
        testAxis(other, toCenter, m.m00, m.m01, m.m02, 0, penData);
        testAxis(other, toCenter, m.m10, m.m11, m.m12, 1, penData);
        testAxis(other, toCenter, m.m20, m.m21, m.m22, 2, penData);
        testAxis(other, toCenter, o.m00, o.m01, o.m02, 3, penData);
        testAxis(other, toCenter, o.m10, o.m11, o.m12, 4, penData);
        testAxis(other, toCenter, o.m20, o.m21, o.m22, 5, penData);

        int bestSingleAxis = penData.bestAxis;

        testAxis(other, toCenter, m.m01 * o.m02 - m.m02 * o.m01, m.m02 * o.m00 - m.m00 * o.m02, m.m00 * o.m01 - m.m01 * o.m00, 6, penData);
        testAxis(other, toCenter, m.m01 * o.m12 - m.m02 * o.m11, m.m02 * o.m10 - m.m00 * o.m12, m.m00 * o.m11 - m.m01 * o.m10, 7, penData);
        testAxis(other, toCenter, m.m01 * o.m22 - m.m02 * o.m21, m.m02 * o.m20 - m.m00 * o.m22, m.m00 * o.m21 - m.m01 * o.m20, 8, penData);
        testAxis(other, toCenter, m.m11 * o.m02 - m.m12 * o.m01, m.m12 * o.m00 - m.m10 * o.m02, m.m10 * o.m01 - m.m11 * o.m00, 9, penData);
        testAxis(other, toCenter, m.m11 * o.m12 - m.m12 * o.m11, m.m12 * o.m10 - m.m10 * o.m12, m.m10 * o.m11 - m.m11 * o.m10, 10, penData);
        testAxis(other, toCenter, m.m11 * o.m22 - m.m12 * o.m21, m.m12 * o.m20 - m.m10 * o.m22, m.m10 * o.m21 - m.m11 * o.m20, 11, penData);
        testAxis(other, toCenter, m.m21 * o.m02 - m.m22 * o.m01, m.m22 * o.m00 - m.m20 * o.m02, m.m20 * o.m01 - m.m21 * o.m00, 12, penData);
        testAxis(other, toCenter, m.m21 * o.m12 - m.m22 * o.m11, m.m22 * o.m10 - m.m20 * o.m12, m.m20 * o.m11 - m.m21 * o.m10, 13, penData);
        testAxis(other, toCenter, m.m21 * o.m22 - m.m22 * o.m21, m.m22 * o.m20 - m.m20 * o.m22, m.m20 * o.m21 - m.m21 * o.m20, 14, penData);

        // Make sure we've got a result.
        if (penData.bestAxis == 0xFFFFFF) {
            return;
        }

        // We now know there's a collision, and we know which
        // of the axes gave the smallest penetration. We now
        // can deal with it in different ways depending on
        // the case.
        if (penData.bestAxis < 3) {
            // We've got a vertex of box two on a face of box one.
            fillPointFace(this, other, toCenter, contactCache, penData.bestAxis, penData.penetration);
            return;
        } else if (penData.bestAxis < 6) {
            // We've got a vertex of box one on a face of box two.
            // We use the same algorithm as above, but swap around
            // one and two (and therefore also the vector between their
            // centres).
            fillPointFace(other, this, toCenter.mul(-1.0f), contactCache, penData.bestAxis - 3, penData.penetration);
            return;
        }
        // We've got an edge-edge contact. Find out which axes
        penData.bestAxis -= 6;
        int oneAxisIndex = penData.bestAxis / 3;
        int twoAxisIndex = penData.bestAxis % 3;
        Vector3f oneAxis = m.getColumn(oneAxisIndex, new Vector3f());
        Vector3f twoAxis = o.getColumn(twoAxisIndex, new Vector3f());
        Vector3f axis = new Vector3f(oneAxis).cross(twoAxis);
        axis.normalize();

        // The axis should point from box one to box two.
        if (axis.dot(toCenter) > 0) {
            axis.mul(-1.0f);
        }

        // We have the axes, but not the edges: each axis has 4 edges parallel
        // to it, we need to find which of the 4 for each object. We do
        // that by finding the point in the centre of the edge. We know
        // its component in the direction of the box's collision axis is zero
        // (its a mid-point) and we determine which of the extremes in each
        // of the other axes is closest.
        Vector3f ptOnOneEdge = new Vector3f(extents);
        Vector3f ptOnTwoEdge = new Vector3f(other.extents);
        Vector3f temp = new Vector3f();
        for (int i = 0; i < 3; i++) {
            float onOne = 0;
            float onTwo = 0;
            if (i == oneAxisIndex) {
                onOne = 0;
            } else if (m.getColumn(i, temp).dot(axis) > 0) {
                onOne = -ptOnOneEdge.get(i);
            }
            o.getColumn(i, temp);
            if (i == twoAxisIndex) {
                onTwo = 0;
            } else if (o.getColumn(i, temp).dot(axis) < 0) {
                onTwo = -ptOnTwoEdge.get(i);
            }
            switch (i) {
                case 0:
                    ptOnOneEdge.x = onOne;
                    ptOnTwoEdge.x = onTwo;
                    break;
                case 1:
                    ptOnOneEdge.y = onOne;
                    ptOnTwoEdge.y = onTwo;
                    break;
                case 2:
                    ptOnOneEdge.x = onOne;
                    ptOnTwoEdge.x = onTwo;
                    break;
            }
        }

        // Move them into world coordinates (they are already oriented
        // correctly, since they have been derived from the axes).
        ptOnOneEdge = ptOnOneEdge.mul(m);
        ptOnTwoEdge = ptOnTwoEdge.mul(o);

        // So we have a point and a direction for the colliding edges.
        // We need to find out point of closest approach of the two
        // line-segments.
        Vector3f vertex = contactPoint(ptOnOneEdge, oneAxis, extents.get(oneAxisIndex),
                ptOnTwoEdge, twoAxis, other.extents.get(twoAxisIndex),
                bestSingleAxis > 2
        );

        // We can fill the contact.
        Contact contact = contactCache.getContact();

        contact.penetration = penData.penetration;
        contact.contactNormal.set(axis);
        contact.contactPoint.set(vertex);
        contact.setBodyData((Rigidbody) this.rigidbody, (Rigidbody) other.rigidbody, contactCache.friction, contactCache.restitution);
    }

    private void testAxis(BoxCollider other, Vector3f distanceFromCenters, float x, float y, float z, int index, PenetrationData penetrationData) {
        // don't check almost parallel axes
        // Vector3f lengthSquared. Currently not in JOML
        if ((x * x + y * y + z * z) < .0001f) {
            return;
        }

        float penetration = penetrationOnAxis(other, distanceFromCenters, x, y, z);

        if (penetration > 0 && penetration < penetrationData.penetration) {
            penetrationData.penetration = penetration;
            penetrationData.bestAxis = index;
        }
    }

    private void fillPointFace(BoxCollider one, BoxCollider two, Vector3f toCenter, ContactCache data, int best, float pen) {
        // This method is called when we know that a vertex from
        // box two is in contact with box one.

        Contact contact = data.getContact();

        // We know which axis the collision is on (i.e. best),
        // but we need to work out which of the two faces on
        // this axis.
        // TODO: When JOML updates Matrix4f variable access level switch to use the mat4
//        Matrix4f o = one.transform.getTransMatrix();
//        Matrix4f t = two.transform.getTransMatrix();
        Matrix3f o = one.transform.getTransMatrix().get3x3(new Matrix3f());
        Matrix3f t = two.transform.getTransMatrix().get3x3(new Matrix3f());
        Vector3f normal = o.getColumn(best, new Vector3f());
        if (normal.x * toCenter.x + normal.y * toCenter.y + normal.z * toCenter.z > 0) {
            normal.mul(-1.0f);
        }

        // Work out which vertex of box two we're colliding with.
        // Using toCentre doesn't work!   
        // Create the contact data
        contact.contactPoint.set(two.extents);
        if (t.m00 * normal.x + t.m01 * normal.y + t.m02 * normal.z < 0) {
            contact.contactPoint.x = -contact.contactPoint.x;
        }
        if (t.m10 * normal.x + t.m11 * normal.y + t.m12 * normal.z < 0) {
            contact.contactPoint.y = -contact.contactPoint.y;
        }
        if (t.m20 * normal.x + t.m21 * normal.y + t.m22 * normal.z < 0) {
            contact.contactPoint.z = -contact.contactPoint.z;
        }
        contact.contactPoint.mul(t);

        contact.contactNormal.set(normal);
        contact.penetration = pen;
        contact.setBodyData((Rigidbody) one.rigidbody, (Rigidbody) two.rigidbody, data.friction, data.restitution);
    }

    /**
     *
     * @param pOne
     * @param dOne
     * @param oneSize
     * @param pTwo
     * @param dTwo
     * @param twoSize
     * @param useOne If this is true, and the contact point is outside the edge
     * (in the case of an edge-face contact) then we use one's midpoint,
     * otherwise we use two's.
     * @return
     */
    Vector3f contactPoint(Vector3f pOne, Vector3f dOne, float oneSize, Vector3f pTwo, Vector3f dTwo, float twoSize, boolean useOne) {
        float dpStaOne, dpStaTwo, dpOneTwo, smOne, smTwo;
        float denom;

        smOne = dOne.lengthSquared();
        smTwo = dTwo.lengthSquared();
        dpOneTwo = dTwo.dot(dOne);

        Vector3f toSt = new Vector3f(pOne).sub(pTwo);
        dpStaOne = dOne.dot(toSt);
        dpStaTwo = dTwo.dot(toSt);

        denom = smOne * smTwo - dpOneTwo * dpOneTwo;

        // Zero denominator indicates parrallel lines
        if (Math.abs(denom) < .0001f) {
            return useOne ? pOne : pTwo;
        }

        float mua, mub;
        mua = (dpOneTwo * dpStaTwo - smTwo * dpStaOne) / denom;
        mub = (smOne * dpStaTwo - dpOneTwo * dpStaOne) / denom;

        // If either of the edges has the nearest point out
        // of bounds, then the edges aren't crossed, we have
        // an edge-face contact. Our point is on the edge, which
        // we know from the useOne parameter.
        if (mua > oneSize || mua < -oneSize || mub < -twoSize) {
            return useOne ? pOne : pTwo;
        } else {
            Vector3f cOne = new Vector3f(dOne).mul(mua).add(pOne).mul(.5f);
            Vector3f cTwo = new Vector3f(dTwo).mul(mub).add(pTwo).mul(.5f);
            return cOne.add(cTwo);
        }
    }
}
