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

import com.kudodev.knimble.PhysicsSpace;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;
import java.util.List;
import org.joml.Matrix4f;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 *
 * @author IkeOTL
 */
public abstract class RenderLoop {

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

    private boolean wireframe = true;

    private final String title;
    protected final PhysicsSpace physicsSpace;
    protected List<Shape> shapes;

    public RenderLoop(String title, PhysicsSpace physicsSpace) {
        this.title = title;
        this.physicsSpace = physicsSpace;
    }

    protected abstract List<Shape> init(PhysicsSpace physicsSpace);

    protected abstract void update(float delta);

    private void loop() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();
        Camera camera = new Camera();

//        camera.getPosition().set(0, 5, 5f);
//        camera.getRotation().rotate((float) Math.toRadians(90), 0, 0);
        float aspectRatio = (float) windowWidth / windowHeight;
        Matrix4f projMat = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
//        Matrix4f projMat = new Matrix4f().ortho(0, windowWidth, windowHeight, 0, -1, 1);

        Matrix4f viewMat = new Matrix4f();
        Matrix4f projViewMat = new Matrix4f();

        shapes = init(physicsSpace);

        lastFrame = System.nanoTime();
        float delta;
        long newTime;
        Matrix4f modelMat = new Matrix4f();
        while (!glfwWindowShouldClose(window)) {
            newTime = System.nanoTime();
            delta = (newTime - lastFrame) / 1000000000f;
            lastFrame = newTime;

            glfwPollEvents();

            update(delta);
            physicsSpace.tick(delta);

            glEnable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

            if (wireframe) {
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            }

            shaderProgram.bind();

            camera.getViewMatrix(viewMat);
            shaderProgram.setUniform("projViewMat", projMat.mul(viewMat, projViewMat));
            for (Shape s : shapes) {
                shaderProgram.setUniform("modelMat", s.getTransMatrix(modelMat));
                shaderProgram.setUniform("outColor", s.getColor());
                s.getMesh().render();
            }

            shaderProgram.unbind();

            // render here
            glfwSwapBuffers(window);
        }
    }

    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
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
