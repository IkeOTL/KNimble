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

import java.util.ArrayList;
import java.util.List;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 *
 * @author IkeOTL
 */
public class RigidbodyNodeTransform extends RigidbodyTransform {

    private final List<Transform> children = new ArrayList<>();

    public RigidbodyNodeTransform() {
        this(new Vector3f(0), new Quaternionf());
    }

    public RigidbodyNodeTransform(Vector3f p) {
        this(p, new Quaternionf());
    }

    public RigidbodyNodeTransform(Vector3f p, Quaternionf r) {
        super(p, r);
        worldScale = null;
    }

    public void addChild(Transform t) {
        children.add(t);
        t.setParent(this);
        t.updateTransform(true);
    }

    public void removeChild(Transform t) {
        children.remove(t);
        t.setParent(null);
        t.updateTransform(true);
    }

    @Override
    public void updateTransform(boolean force) {
        if (!dirty && !force) {
            return;
        }

        super.updateTransform(true);
        for (int i = 0; i < children.size(); i++) {
            children.get(i).updateTransform(this);
        }
    }

    @Override
    public void updateTransform(Transform parent) {
        super.updateTransform(parent);
        for (int i = 0; i < children.size(); i++) {
            children.get(i).updateTransform(this);
        }
    }
}
