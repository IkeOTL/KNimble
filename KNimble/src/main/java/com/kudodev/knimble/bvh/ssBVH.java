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
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author IkeOTL
 */
public abstract class ssBVH<A> {

    public ssBVHNode<A> rootBVH;
    public BVHNodeAdaptor<A> nAda;

    public int LEAF_OBJ_MAX = 1;
    public int nodeCount = 0;
    public int maxDepth = 0;

    public HashSet<ssBVHNode<A>> refitNodes = new HashSet<>();

    public ssBVH(BVHNodeAdaptor<A> nodeAdaptor, List<A> objects) {
//        nodeAdaptor.setBVH(this);
//
//        this.nAda = nodeAdaptor;
//
//        if (objects.size() > 0) {
//            rootBVH = new ssBVHNode<>(this, objects);
//        } else {
//            rootBVH = new ssBVHNode<A>(this);
//            rootBVH.gobjects = new ArrayList<A>(); // it's a leaf, so give it an empty object list
//        }
    }

//    public abstract boolean NodeTest(SSAABB box);
//
//    // internal functional traversal...
//    private void _traverse(ssBVHNode<A> curNode, NodeTest hitTest, List<ssBVHNode<A>> hitlist) {
//
//        if (curNode == null) {
//            return;
//        }
//
//        if (hitTest(curNode.box)) {
//            hitlist.add(curNode);
//            _traverse(curNode.left, hitTest, hitlist);
//            _traverse(curNode.right, hitTest, hitlist);
//        }
//
//    }
//
//    // public interface to traversal..
//    public List<ssBVHNode<A>> traverse(NodeTest hitTest) {
//        List<ssBVHNode<A>> hits = new ArrayList<>();
//        this._traverse(rootBVH, hitTest, hits);
//        return hits;
//    }
//
//    // left in for compatibility..
//    public List<ssBVHNode<A>> traverseRay(SSRay ray) {
//        float tnear = 0f, tfar = 0f;
//        return traverse(box -> OpenTKHelper.intersectRayAABox1(ray, box, tnear, tfar));
//    }
//
//    public List<ssBVHNode<A>> traverse(SSRay ray) {
//        float tnear = 0f, tfar = 0f;
//        return traverse(box -> OpenTKHelper.intersectRayAABox1(ray, box, tnear, tfar));
//    }
//
//    public List<ssBVHNode<A>> traverse(SSAABB volume) {
//        return traverse(box -> box.IntersectsAABB(volume));
//    }

    public void optimize() {

        if (LEAF_OBJ_MAX != 1) {
            throw new RuntimeException("In order to use optimize, you must set LEAF_OBJ_MAX=1");
        }

        while (refitNodes.size() > 0) {
//            int maxdepth = refitNodes.Max(n -> n.depth);
//            var sweepNodes = refitNodes.Where(n -> n.depth == maxdepth).ToList();
//            sweepNodes.ForEach(n -> refitNodes.Remove(n));
//            sweepNodes.ForEach(n -> n.tryRotate(this));
        }

    }

    public void addObject(A newOb) {
//        SSAABB box = SSAABB.FromSphere(nAda.objectpos(newOb), nAda.radius(newOb));
//        float boxSAH = ssBVHNode<A>.SA( box );
//        rootBVH.addObject(nAda, newOb, box, boxSAH);
    }

    public void removeObject(A newObj) {
//        var leaf = nAda.getLeaf(newObj);
//        leaf.removeObject(nAda, newObj);
    }
//
//    public int countBVHNodes() {
//        return rootBVH.countBVHNodes();
//    }

}
