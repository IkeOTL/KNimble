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
package com.kudodev.knimble.bvh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class ssBVHNode<A> {
//
//    public SSAABB box;
//
//    public ssBVHNode<A> parent;
//    public ssBVHNode<A> left;
//    public ssBVHNode<A> right;
//
//    public int depth;
//    public int nodeNumber; // for debugging
//
//    public List<A> gobjects;  // only populated in leaf nodes
//
//    public ssBVHNode(ssBVH<A> bvh) {
//        gobjects = new ArrayList<>();
//        left = right = null;
//        parent = null;
//        this.nodeNumber = bvh.nodeCount++;
//    }
//
//    public enum Axis {
//        X, Y, Z,
//    }
//
//    public ssBVHNode(ssBVH<A> bvh, List<A> gobjectlist) {
//        //this(bvh, null, gobjectlist, Axis.X, 0);
//    }
//
//    private Axis pickSplitAxis() {
//        float axis_x = box.Max.X - box.Min.X;
//        float axis_y = box.Max.Y - box.Min.Y;
//        float axis_z = box.Max.Z - box.Min.Z;
//
//        // return the biggest axis
//        if (axis_x > axis_y) {
//            if (axis_x > axis_z) {
//                return Axis.X;
//            } else {
//                return Axis.Z;
//            }
//        } else {
//            if (axis_y > axis_z) {
//                return Axis.Y;
//            } else {
//                return Axis.Z;
//            }
//        }
//    }
//
//    public boolean IsLeaf() {
//        boolean isLeaf = (this.gobjects != null);
//
//        // if we're a leaf, then both left and right should be null..
//        if (isLeaf && ((right != null) || (left != null))) {
//            throw new RuntimeException("ssBVH Leaf has objects and left/right pointers!");
//        }
//
//        return isLeaf;
//    }
//
//    private Axis NextAxis(Axis cur) {
//        switch (cur) {
//            case X:
//                return Axis.Y;
//            case Y:
//                return Axis.Z;
//            case Z:
//                return Axis.X;
//
//            default:
//                throw new RuntimeException();
//        }
//    }
//
//    public void refit_ObjectChanged(BVHNodeAdaptor<A> nAda, A obj) {
//        if (gobjects == null) {
//            throw new RuntimeException("dangling leaf!");
//        }
//
//        if (refitVolume(nAda)) {
//            // add our parent to the optimize list...
//            if (parent != null) {
//                nAda.BVH.refitNodes.add(parent);
//                // you can force an optimize every time something moves, but it's not very efficient
//                // instead we do this per-frame after a bunch of updates.
//                // nAda.BVH.optimize();                    
//            }
//        }
//    }
//
//    private void expandVolume(BVHNodeAdaptor<A> nAda, Vector3f objectpos, float radius) {
//        boolean expanded = false;
//
//        // test min X and max X against the current bounding volume
//        if ((objectpos.x() - radius) < box.Min.X) {
//            box.Min.x(objectpos.x() - radius);
//            expanded = true;
//        }
//
//        if ((objectpos.x() + radius) > box.Max.X) {
//            box.Max.x(objectpos.x() + radius);
//            expanded = true;
//        }
//
//        // test min Y and max Y against the current bounding volume
//        if ((objectpos.Y - radius) < box.Min.Y) {
//            box.Min.Y = (objectpos.Y - radius);
//            expanded = true;
//        }
//
//        if ((objectpos.Y + radius) > box.Max.Y) {
//            box.Max.Y = (objectpos.Y + radius);
//            expanded = true;
//        }
//
//        // test min Z and max Z against the current bounding volume
//        if ((objectpos.Z - radius) < box.Min.Z) {
//            box.Min.Z = (objectpos.Z - radius);
//            expanded = true;
//        }
//
//        if ((objectpos.Z + radius) > box.Max.Z) {
//            box.Max.Z = (objectpos.Z + radius);
//            expanded = true;
//        }
//
//        if (expanded && parent != null) {
//            parent.childExpanded(nAda, this);
//        }
//    }
//
//    private void assignVolume(Vector3f objectpos, float radius) {
//        box.Min.X = objectpos.X - radius;
//        box.Max.X = objectpos.X + radius;
//        box.Min.Y = objectpos.Y - radius;
//        box.Max.Y = objectpos.Y + radius;
//        box.Min.Z = objectpos.Z - radius;
//        box.Max.Z = objectpos.Z + radius;
//    }
//
//    void computeVolume(BVHNodeAdaptor<A> nAda) {
//        assignVolume(nAda.objectpos(gobjects.get(0)), nAda.radius(gobjects.get(0)));
//        for (int i = 1; i < gobjects.size(); i++) {
//            expandVolume(nAda, nAda.objectpos(gobjects.get(i)), nAda.radius(gobjects.get(i)));
//        }
//    }
//
//    boolean refitVolume(BVHNodeAdaptor<A> nAda) {
//        if (gobjects.size() == 0) {
//            throw new RuntimeException();
//        }  // TODO: fix this... we should never get called in this case...
//
//        SSAABB oldbox = box;
//        computeVolume(nAda);
//        if (!box.Equals(oldbox)) {
//            if (parent != null) {
//                parent.childRefit(nAda, true);
//            }
//            return true;
//        }
//
//        return false;
//    }
//
//    float SA(SSAABB box) {
//        float x_size = box.Max.X - box.Min.X;
//        float y_size = box.Max.Y - box.Min.Y;
//        float z_size = box.Max.Z - box.Min.Z;
//
//        return 2.0f * ((x_size * y_size) + (x_size * z_size) + (y_size * z_size));
//    }
//
//    float SA(SSAABB box) {
//        float x_size = box.Max.X - box.Min.X;
//        float y_size = box.Max.Y - box.Min.Y;
//        float z_size = box.Max.Z - box.Min.Z;
//
//        return 2.0f * ((x_size * y_size) + (x_size * z_size) + (y_size * z_size));
//    }
//
//    float SA(ssBVHNode<A> node) {
//        float x_size = node.box.Max.X - node.box.Min.X;
//        float y_size = node.box.Max.Y - node.box.Min.Y;
//        float z_size = node.box.Max.Z - node.box.Min.Z;
//
//        return 2.0f * ((x_size * y_size) + (x_size * z_size) + (y_size * z_size));
//    }
//
//    float SA(BVHNodeAdaptor<A> nAda, A obj) {
//        float radius = nAda.radius(obj);
//        float size = radius * 2;
//
//        return 6.0f * (size * size);
//    }
//
//    SSAABB AABBofPair(ssBVHNode<A> nodea, ssBVHNode<A> nodeb) {
//        SSAABB box = nodea.box;
//        box.ExpandToFit(nodeb.box);
//
//        return box;
//    }
//
//    float SAofPair(ssBVHNode<A> nodea, ssBVHNode<A> nodeb) {
//        SSAABB box = nodea.box;
//        box.ExpandToFit(nodeb.box);
//
//        return SA(box);
//    }
//
//    float SAofPair(SSAABB boxa, SSAABB boxb) {
//        SSAABB pairbox = boxa;
//        pairbox.ExpandToFit(boxb);
//
//        return SA(pairbox);
//    }
//
//    SSAABB AABBofOBJ(BVHNodeAdaptor<A> nAda, A obj) {
//        float radius = nAda.radius(obj);
//        SSAABB box;
//
//        box.Min.X = -radius;
//        box.Max.X = radius;
//
//        box.Min.Y = -radius;
//        box.Max.Y = radius;
//
//        box.Min.Z = -radius;
//        box.Max.Z = radius;
//
//        return box;
//    }
//
//    float SAofList(BVHNodeAdaptor<A> nAda, List<A> list) {
//        SSAABB box = AABBofOBJ(nAda, list.get(0));
////        list.ToList<A>().GetRange(1, list.size() - 1).ForEach(obj -> {
////            SSAABB newbox = AABBofOBJ(nAda, obj);
////            box.ExpandBy(newbox);
////        });
//
//        return SA(box);
//    }
//
//    // The list of all candidate rotations, from "Fast, Effective BVH Updates for Animated Scenes", Figure 1.
//    enum Rot {
//        NONE, L_RL, L_RR, R_LL, R_LR, LL_RR, LL_RL,
//    }
//
//    class rotOpt implements Comparable<rotOpt> { // rotation option
//
//        public float SAH;
//        public Rot rot;
//
//        rotOpt(float SAH, Rot rot) {
//            this.SAH = SAH;
//            this.rot = rot;
//        }
//
//        @Override
//        public int compareTo(rotOpt other) {
//            return new Float(SAH).compareTo(other.SAH);
//        }
//    }
//
//    private  List<Rot> eachRot() {
//        return Arrays.asList(Rot.values());
//    }
//
//    void tryRotate(ssBVH<A> bvh) {
////        BVHNodeAdaptor<A> nAda = bvh.nAda;
////
////        // if we are not a grandparent, then we can't rotate, so queue our parent and bail out
////        if (left.IsLeaf() && right.IsLeaf()) {
////            if (parent != null) {
////                bvh.refitNodes.add(parent);
////                return;
////            }
////        }
////
////        // for each rotation, check that there are grandchildren as necessary (aka not a leaf)
////        // then compute total SAH cost of our branches after the rotation.
////        float mySA = SA(left) + SA(right);
////
////        rotOpt bestRot = eachRot().min((rot) -> {
////            switch (rot) {
////                case NONE:
////                    return new rotOpt(mySA, Rot.NONE);
////
////                // child to grandchild rotations
////                case L_RL:
////                    if (right.IsLeaf) {
////                        return new rotOpt(float.MaxValue, Rot.NONE);
////                    } else {
////                        return new rotOpt(SA(right.left) + SA(AABBofPair(left, right.right)), rot);
////                    }
////
////                case L_RR:
////                    if (right.IsLeaf) {
////                        return new rotOpt(float.MaxValue, Rot.NONE);
////                    } else {
////                        return new rotOpt(SA(right.right) + SA(AABBofPair(left, right.left)), rot);
////                    }
////
////                case R_LL:
////                    if (left.IsLeaf) {
////                        return new rotOpt(float.MaxValue, Rot.NONE);
////                    } else {
////                        return new rotOpt(SA(AABBofPair(right, left.right)) + SA(left.left), rot);
////                    }
////
////                case R_LR:
////                    if (left.IsLeaf) {
////                        return new rotOpt(float.MaxValue, Rot.NONE);
////                    } else {
////                        return new rotOpt(SA(AABBofPair(right, left.left)) + SA(left.right), rot);
////                    }
////
////                // grandchild to grandchild rotations
////                case LL_RR:
////                    if (left.IsLeaf || right.IsLeaf) {
////                        return new rotOpt(float.MaxValue, Rot.NONE);
////                    } else {
////                        return new rotOpt(SA(AABBofPair(right.right, left.right)) + SA(AABBofPair(right.left, left.left)), rot);
////                    }
////
////                case LL_RL:
////                    if (left.IsLeaf || right.IsLeaf) {
////                        return new rotOpt(float.MaxValue, Rot.NONE);
////                    } else {
////                        return new rotOpt(SA(AABBofPair(right.left, left.right)) + SA(AABBofPair(left.left, right.right)), rot);
////                    }
////
////                // unknown...
////                default:
////                    throw new RuntimeException("missing implementation for BVH Rotation SAH Computation .. " + rot.ToString());
////
////            }
////        });
////
////        // perform the best rotation...            
////        if (bestRot.rot != Rot.NONE) {
////            // if the best rotation is no-rotation... we check our parents anyhow..                
////            if (parent != null) {
////                // but only do it some random percentage of the time.
////                if ((DateTime.Now.Ticks % 100) < 2) {
////                    bvh.refitNodes.add(parent);
////                }
////            }
////        } else {
////            if (parent != null) {
////                bvh.refitNodes.add(parent);
////            }
////            if (((mySA - bestRot.SAH) / mySA) < 0.3f) {
////                return; // the benefit is not worth the cost
////            }
////            // in order to swap we need to:
////            //  1. swap the node locations
////            //  2. update the depth (if child-to-grandchild)
////            //  3. update the parent pointers
////            //  4. refit the boundary box
////            ssBVHNode<A> swap = null;
////
////            switch (bestRot.rot) {
////                case NONE:
////                    break;
////
////                // child to grandchild rotations
////                case L_RL:
////                    swap = left;
////                    left = right.left;
////                    left.parent = this;
////                    right.left = swap;
////                    swap.parent = right;
////                    right.childRefit(nAda, false);
////                    break;
////
////                case L_RR:
////                    swap = left;
////                    left = right.right;
////                    left.parent = this;
////                    right.right = swap;
////                    swap.parent = right;
////                    right.childRefit(nAda, false);
////                    break;
////
////                case R_LL:
////                    swap = right;
////                    right = left.left;
////                    right.parent = this;
////                    left.left = swap;
////                    swap.parent = left;
////                    left.childRefit(nAda, false);
////                    break;
////
////                case R_LR:
////                    swap = right;
////                    right = left.right;
////                    right.parent = this;
////                    left.right = swap;
////                    swap.parent = left;
////                    left.childRefit(nAda, false);
////                    break;
////
////                // grandchild to grandchild rotations
////                case LL_RR:
////                    swap = left.left;
////                    left.left = right.right;
////                    right.right = swap;
////                    left.left.parent = left;
////                    swap.parent = right;
////                    left.childRefit(nAda, false);
////                    right.childRefit(nAda, false);
////                    break;
////
////                case LL_RL:
////                    swap = left.left;
////                    left.left = right.left;
////                    right.left = swap;
////                    left.left.parent = left;
////                    swap.parent = right;
////                    left.childRefit(nAda, false);
////                    right.childRefit(nAda, false);
////                    break;
////
////                // unknown...
////                default:
////                    throw new RuntimeException("missing implementation for BVH Rotation .. " + bestRot.rot);
////
////            }
////
////            // fix the depths if necessary....
////            switch (bestRot.rot) {
////                case L_RL:
////                case L_RR:
////                case R_LL:
////                case R_LR:
////                    this.setDepth(nAda, this.depth);
////                    break;
////            }
////        }
//    }
//
//    private static List<Axis> eachAxis() {
//        return Arrays.asList(Axis.values());
//    }
//
//    class SplitAxisOpt<A> implements Comparable<SplitAxisOpt<A>> {
//
//        public float SAH;
//        public Axis axis;
//        public List<A> left, right;
//
//        SplitAxisOpt(float SAH, Axis axis, List<A> left, List<A> right) {
//            this.SAH = SAH;
//            this.axis = axis;
//            this.left = left;
//            this.right = right;
//        }
//
//        @Override
//        public int compareTo(SplitAxisOpt<A> other) {
//            return new Float(SAH).compareTo(other.SAH);
//        }
//    }
//
//    void splitNode(BVHNodeAdaptor<A> nAda) {
////        // second, decide which axis to split on, and sort..
////        List<A> splitlist = gobjects;
////        splitlist.ForEach(o -> nAda.unmapObject(o));
////        int center = (int) (splitlist.size() / 2); // find the center object
////        SplitAxisOpt<A> bestSplit = eachAxis.Min((axis) -> {
////            List orderedlist = new ArrayList<A>(splitlist);
////            switch (axis) {
////                case Axis.X:
////                    orderedlist.Sort(delegate(A go1, A go2 ) { 
////                        return nAda.objectpos(go1).X.CompareTo(nAda.objectpos(go2).X);
////                    });
////                    break;
////
////                case Axis.Y:
////                    orderedlist.Sort(delegate(A go1, A go2) { 
////                        return nAda.objectpos(go1).Y.CompareTo(nAda.objectpos(go2).Y);
////                    });
////                    break;
////
////                case Axis.Z:
////                    orderedlist.Sort(delegate(A go1, A go2) { 
////                        return nAda.objectpos(go1).Z.CompareTo(nAda.objectpos(go2).Z);
////                    });
////                    break;
////
////                default:
////                    throw new RuntimeException("unknown split axis: " + axis.ToString());
////            }
////
////            var left_s = orderedlist.GetRange(0, center);
////            var right_s = orderedlist.GetRange(center, splitlist.Count - center);
////            float SAH = SAofList(nAda, left_s) * left_s.Count + SAofList(nAda, right_s) * right_s.Count;
////
////            return new SplitAxisOpt<A>(SAH, axis, left_s, right_s);
////        });
////
////        // perform the split
////        gobjects = null;
////        this.left = new ssBVHNode<A>(nAda.BVH, this, bestSplit.left, bestSplit.axis, this.depth + 1); // Split the Hierarchy to the left
////        this.right = new ssBVHNode<A>(nAda.BVH, this, bestSplit.right, bestSplit.axis, this.depth + 1); // Split the Hierarchy to the right 
//    }
//
//    void splitIfNecessary(BVHNodeAdaptor<A> nAda) {
//        if (gobjects.size() > nAda.BVH.LEAF_OBJ_MAX) {
//            splitNode(nAda);
//        }
//    }
//
//    void addObject(BVHNodeAdaptor<A> nAda, A newOb, SSAABB newObBox, float newObSAH) {
//        addObject(nAda, this, newOb, newObBox, newObSAH);
//    }
//
//    void addObject_Pushdown(BVHNodeAdaptor<A> nAda, ssBVHNode<A> curNode, A newOb) {
//        ssBVHNode<A> left = curNode.left;
//        ssBVHNode<A> right = curNode.right;
//
//        // merge and pushdown left and right as a new node..
//        ssBVHNode<A> mergedSubnode = new ssBVHNode<A>(nAda.BVH);
//
//        mergedSubnode.left = left;
//        mergedSubnode.right = right;
//        mergedSubnode.parent = curNode;
//
//        mergedSubnode.gobjects = null; // we need to be an interior node... so null out our object list..
//
//        left.parent = mergedSubnode;
//        right.parent = mergedSubnode;
//
//        mergedSubnode.childRefit(nAda, false);
//
//        // make new subnode for obj
//        ssBVHNode newSubnode = new ssBVHNode<A>(nAda.BVH);
//        newSubnode.parent = curNode;
//        List<A> l = new ArrayList<A>();
//        l.add(newOb);
//        newSubnode.gobjects = l;
//
//        nAda.mapObjectToBVHLeaf(newOb, newSubnode);
//
//        newSubnode.computeVolume(nAda);
//
//        // make assignments..
//        curNode.left = mergedSubnode;
//        curNode.right = newSubnode;
//        curNode.setDepth(nAda, curNode.depth); // propagate new depths to our children.
//        curNode.childRefit(nAda, true);
//
//    }
//
//    void addObject(BVHNodeAdaptor<A> nAda, ssBVHNode<A> curNode, A newOb, SSAABB newObBox, float newObSAH) {
//        // 1. first we traverse the node looking for the best leaf
//        while (curNode.gobjects == null) {
//
//            // find the best way to add this object.. 3 options..
//            // 1. send to left node  (L+N,R)
//            // 2. send to right node (L,R+N)
//            // 3. merge and pushdown left-and-right node (L+R,N)
//            ssBVHNode<A> left = curNode.left;
//            ssBVHNode<A> right = curNode.right;
//
//            float leftSAH = SA(left);
//            float rightSAH = SA(right);
//            float sendLeftSAH = rightSAH + SA(left.box.ExpandedBy(newObBox));    // (L+N,R)
//            float sendRightSAH = leftSAH + SA(right.box.ExpandedBy(newObBox));   // (L,R+N)
//            float mergedLeftAndRightSAH = SA(AABBofPair(left, right)) + newObSAH; // (L+R,N)
//
//            // Doing a merge-and-pushdown can be expensive, so we only do it if it's notably better
//            float MERGE_DISCOUNT = 0.3f;
//
//            if (mergedLeftAndRightSAH < (Math.min(sendLeftSAH, sendRightSAH)) * MERGE_DISCOUNT) {
//                addObject_Pushdown(nAda, curNode, newOb);
//                return;
//            } else {
//                if (sendLeftSAH < sendRightSAH) {
//                    curNode = left;
//                } else {
//                    curNode = right;
//                }
//            }
//
//        }
//
//        // 2. then we add the object and map it to our leaf
//        curNode.gobjects.add(newOb);
//        nAda.mapObjectToBVHLeaf(newOb, curNode);
//        curNode.refitVolume(nAda);
//
//        // split if necessary...
//        curNode.splitIfNecessary(nAda);
//    }
//
//    int countBVHNodes() {
//        if (gobjects != null) {
//            return 1;
//        } else {
//            return left.countBVHNodes() + right.countBVHNodes();
//        }
//    }
//
//    void removeObject(BVHNodeAdaptor<A> nAda, A newOb) {
//        if (gobjects == null) {
//            throw new RuntimeException("removeObject() called on nonLeaf!");
//        }
//
//        nAda.unmapObject(newOb);
//        gobjects.remove(newOb);
//        if (gobjects.size() > 0) {
//            refitVolume(nAda);
//        } else {
//            // our leaf is empty, so collapse it if we are not the root...
//            if (parent != null) {
//                gobjects = null;
//                parent.removeLeaf(nAda, this);
//                parent = null;
//            }
//        }
//    }
//
//    void setDepth(BVHNodeAdaptor<A> nAda, int newdepth) {
//        this.depth = newdepth;
//        if (newdepth > nAda.BVH.maxDepth) {
//            nAda.BVH.maxDepth = newdepth;
//        }
//
//        if (gobjects == null) {
//            left.setDepth(nAda, newdepth + 1);
//            right.setDepth(nAda, newdepth + 1);
//        }
//    }
//
//    void removeLeaf(BVHNodeAdaptor<A> nAda, ssBVHNode<A> removeLeaf) {
//        if (left == null || right == null) {
//            throw new RuntimeException("bad intermediate node");
//        }
//
//        ssBVHNode<A> keepLeaf;
//        if (removeLeaf == left) {
//            keepLeaf = right;
//        } else if (removeLeaf == right) {
//            keepLeaf = left;
//        } else {
//            throw new RuntimeException("removeLeaf doesn't match any leaf!");
//        }
//
//        // "become" the leaf we are keeping.
//        box = keepLeaf.box;
//
//        left = keepLeaf.left;
//        right = keepLeaf.right;
//        gobjects = keepLeaf.gobjects;
//
//        // clear the leaf..
//        // keepLeaf.left = null; keepLeaf.right = null; keepLeaf.gobjects = null; keepLeaf.parent = null; 
//        if (gobjects == null) {
//            left.parent = this;
//            right.parent = this;  // reassign child parents..
//            this.setDepth(nAda, this.depth); // this reassigns depth for our children
//        } else {
//            // map the objects we adopted to us...                                                
//            gobjects.forEach(o -> {
//                nAda.mapObjectToBVHLeaf(o, this);
//            });
//        }
//
//        // propagate our new volume..
//        if (parent != null) {
//            parent.childRefit(nAda, true);
//        }
//    }
//
//    ssBVHNode<A> rootNode() {
//        ssBVHNode<A> cur = this;
//        while (cur.parent != null) {
//            cur = cur.parent;
//        }
//        return cur;
//    }
//
//    void findOverlappingLeaves(BVHNodeAdaptor<A> nAda, Vector3f origin, float radius, List<ssBVHNode<A>> overlapList) {
//        if (toAABB().IntersectsSphere(origin, radius)) {
//            if (gobjects != null) {
//                overlapList.add(this);
//            } else {
//                left.findOverlappingLeaves(nAda, origin, radius, overlapList);
//                right.findOverlappingLeaves(nAda, origin, radius, overlapList);
//            }
//        }
//    }
//
//    void findOverlappingLeaves(BVHNodeAdaptor<A> nAda, SSAABB aabb, List<ssBVHNode<A>> overlapList) {
//
//        if (toAABB().IntersectsAABB(aabb)) {
//            if (gobjects != null) {
//                overlapList.add(this);
//            } else {
//                left.findOverlappingLeaves(nAda, aabb, overlapList);
//                right.findOverlappingLeaves(nAda, aabb, overlapList);
//            }
//        }
//    }
//
//    SSAABB toAABB() {
//        SSAABB aabb = new SSAABB();
//        aabb.Min.X = box.Min.X;
//        aabb.Min.Y = box.Min.Y;
//        aabb.Min.Z = box.Min.Z;
//        aabb.Max.X = box.Max.X;
//        aabb.Max.Y = box.Max.Y;
//        aabb.Max.Z = box.Max.Z;
//        return aabb;
//    }
//
//    void childExpanded(BVHNodeAdaptor<A> nAda, ssBVHNode<A> child) {
//        boolean expanded = false;
//        if (child.box.Min.X < box.Min.X) {
//            box.Min.X = child.box.Min.X;
//            expanded = true;
//        }
//
//        if (child.box.Max.X > box.Max.X) {
//            box.Max.X = child.box.Max.X;
//            expanded = true;
//        }
//
//        if (child.box.Min.Y < box.Min.Y) {
//            box.Min.Y = child.box.Min.Y;
//            expanded = true;
//        }
//
//        if (child.box.Max.Y > box.Max.Y) {
//            box.Max.Y = child.box.Max.Y;
//            expanded = true;
//        }
//
//        if (child.box.Min.Z < box.Min.Z) {
//            box.Min.Z = child.box.Min.Z;
//            expanded = true;
//        }
//
//        if (child.box.Max.Z > box.Max.Z) {
//            box.Max.Z = child.box.Max.Z;
//            expanded = true;
//        }
//
//        if (expanded && parent != null) {
//            parent.childExpanded(nAda, this);
//        }
//    }
//
//    void childRefit(BVHNodeAdaptor<A> nAda, boolean propagate) {
//        childRefit(nAda, this, propagate);
//    }
//
//    void childRefit(BVHNodeAdaptor<A> nAda, ssBVHNode<A> curNode, boolean propagate) {
//        do {
//            SSAABB oldbox = curNode.box;
//            ssBVHNode<A> left = curNode.left;
//            ssBVHNode<A> right = curNode.right;
//            // start with the left box
//            SSAABB newBox = left.box;
//
//            // expand any dimension bigger in the right node
//            if (right.box.Min.X < newBox.Min.X) {
//                newBox.Min.X = right.box.Min.X;
//            }
//
//            if (right.box.Min.Y < newBox.Min.Y) {
//                newBox.Min.Y = right.box.Min.Y;
//            }
//
//            if (right.box.Min.Z < newBox.Min.Z) {
//                newBox.Min.Z = right.box.Min.Z;
//            }
//
//            if (right.box.Max.X > newBox.Max.X) {
//                newBox.Max.X = right.box.Max.X;
//            }
//
//            if (right.box.Max.Y > newBox.Max.Y) {
//                newBox.Max.Y = right.box.Max.Y;
//            }
//
//            if (right.box.Max.Z > newBox.Max.Z) {
//                newBox.Max.Z = right.box.Max.Z;
//            }
//
//            // now set our box to the newly created box
//            curNode.box = newBox;
//
//            // and walk up the tree
//            curNode = curNode.parent;
//
//        } while (propagate && curNode != null);
//
//    }
//
//    private ssBVHNode(ssBVH<A> bvh, ssBVHNode<A> lparent, List<A> gobjectlist, Axis lastSplitAxis, int curdepth) {
//        BVHNodeAdaptor<A> nAda = bvh.nAda;
//
//        this.nodeNumber = bvh.nodeCount++;
//        this.parent = lparent; // save off the parent BVHGObj Node
//        this.depth = curdepth;
//
//        if (bvh.maxDepth < curdepth) {
//            bvh.maxDepth = curdepth;
//        }
//
//        // Early out check due to bad data
//        // If the list is empty then we have no BVHGObj, or invalid parameters are passed in
//        if (gobjectlist == null || gobjectlist.size() < 1) {
//            throw new RuntimeException("ssBVHNode constructed with invalid paramaters");
//        }
//
//        // Check if we’re at our LEAF node, and if so, save the objects and stop recursing.  Also store the min/max for the leaf node and update the parent appropriately
//        if (gobjectlist.size() <= bvh.LEAF_OBJ_MAX) {
//            // once we reach the leaf node, we must set prev/next to null to signify the end
//            left = null;
//            right = null;
//
//            // at the leaf node we store the remaining objects, so initialize a list
//            gobjects = gobjectlist;
//            gobjects.forEach(o -> nAda.mapObjectToBVHLeaf(o, this));
//
//            computeVolume(nAda);
//            splitIfNecessary(nAda);
//        } else {
//            // --------------------------------------------------------------------------------------------
//            // if we have more than (bvh.LEAF_OBJECT_COUNT) objects, then compute the volume and split
//            gobjects = gobjectlist;
//            computeVolume(nAda);
//            splitNode(nAda);
//            childRefit(nAda, false);
//        }
//    }
}
