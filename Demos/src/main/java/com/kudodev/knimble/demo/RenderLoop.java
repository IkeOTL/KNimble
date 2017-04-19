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
package com.kudodev.knimble.demo;

import com.kudodev.knimble.PhysicsSpace;
import com.kudodev.knimble.Rigidbody;
import com.kudodev.knimble.colliders.Collider;
import com.kudodev.knimble.colliders.CubeCollider;
import com.kudodev.knimble.colliders.SphereCollider;
import com.kudodev.knimble.demo.utils.Camera;
import com.kudodev.knimble.demo.utils.Mesh;
import com.kudodev.knimble.demo.utils.ShaderProgram;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;
import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.lwjgl.util.par.ParShapes;
import org.lwjgl.util.par.ParShapesMesh;

/**
 *
 * @author IkeOTL
 */
public class RenderLoop {

    // The window handle
    private long window;
    private final int windowWidth = 1600;
    private final int windowHeight = 900;
    private final float FOV = (float) Math.toRadians(60.0f);
    private final float Z_NEAR = 0.01f;
    private final float Z_FAR = 1000.f;

    private long lastFrame;
    private int currFPS, FPS;
    private float FPSTime;

    private final String title;
    private final PhysicsSpace physicsSpace;

    public RenderLoop(String title, PhysicsSpace physicsSpace) {
        this.title = title;
        this.physicsSpace = physicsSpace;
    }

    private void loop() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();
        Camera camera = new Camera();

        Mesh sphereModel = createSphere(2);

        float aspectRatio = (float) windowWidth / windowHeight;
        Matrix4f projMat = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);

        Matrix4f viewMat = new Matrix4f();
        Matrix4f projViewMat = new Matrix4f();

        List<Collider> colliders = new ArrayList<>();

        Rigidbody r1 = new Rigidbody();
        Collider c1 = new SphereCollider(r1, 1);
        colliders.add(c1);
        r1.setPosition(-2, 0, -5);
        r1.setVelocity(.5f, 0, 0);
        physicsSpace.addBody(r1, c1);
        
        Rigidbody r2 = new Rigidbody();
        Collider c2 = new SphereCollider(r2, 1);
        colliders.add(c2);
        r2.setPosition(2, 0, -5);
        r2.setVelocity(-.5f, 0, 0);
        physicsSpace.addBody(r2, c2);

        lastFrame = System.nanoTime();
        float delta;
        long newTime;
        while (!glfwWindowShouldClose(window)) {
            newTime = System.nanoTime();
            delta = (newTime - lastFrame) / 1000000000f;
            lastFrame = newTime;

            glfwPollEvents();

            physicsSpace.tick(delta);

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shaderProgram.bind();

            camera.getViewMatrix(viewMat);
            shaderProgram.setUniform("projViewMat", projMat.mul(viewMat, projViewMat));

            for (Collider r : colliders) {
                shaderProgram.setUniform("modelMat", r.getTransform().getTransMatrix());
                sphereModel.render();
            }

            shaderProgram.unbind();

            // render here
            glfwSwapBuffers(window);
        }

        sphereModel.dispose();
    }

    private Mesh createSphere(int level) {
        ParShapesMesh parShape = ParShapes.par_shapes_create_subdivided_sphere(level);

        short numIndices = (short) (parShape.ntriangles() * 3);
        FloatBuffer verts = parShape.points(parShape.npoints() * 3);
        ShortBuffer indices = parShape.triangles(numIndices);

        Mesh m = new Mesh(numIndices, verts, indices);

        ParShapes.par_shapes_free_mesh(parShape);

        return m;
    }

    public void start() throws Exception {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        // Create the window
        window = glfwCreateWindow(windowWidth, windowHeight, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        });

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(GLFW_TRUE); // vsync
        glfwShowWindow(window);

        GL.createCapabilities();
    }
}
