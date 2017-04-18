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
import com.kudodev.knimble.demo.utils.Model;
import com.kudodev.knimble.demo.utils.ShaderProgram;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import java.nio.*;
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
    private int windowWidth = 1600;
    private int windowHeight = 900;

    private long lastFrame;
    private int currFPS, FPS;
    private float FPSTime;

    private final String title;
    private final PhysicsSpace physicsSpace;
    private ShaderProgram shaderProgram;

    public RenderLoop(String title, PhysicsSpace physicsSpace) {
        this.title = title;
        this.physicsSpace = physicsSpace;
    }

    private void loop() throws Exception {
        shaderProgram = new ShaderProgram();

        Model sphereModel = createSphere(2);

        lastFrame = System.nanoTime();
        float delta;
        long newTime;
        while (!glfwWindowShouldClose(window)) {
            newTime = System.nanoTime();
            delta = (newTime - lastFrame) / 1000000000f;
            lastFrame = newTime;

            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            physicsSpace.tick(delta);

            shaderProgram.bind();

            sphereModel.render();

            shaderProgram.unbind();

            // render here
            glfwSwapBuffers(window);
        }

        sphereModel.dispose();
    }

    private Model createSphere(int level) {
        ParShapesMesh parShape = ParShapes.par_shapes_create_subdivided_sphere(level);

        short numIndices = (short) (parShape.ntriangles() * 3);
        FloatBuffer verts = parShape.points(parShape.npoints() * 3);
        ShortBuffer indices = parShape.triangles(numIndices);

        Model m = new Model(numIndices, verts, indices);

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
