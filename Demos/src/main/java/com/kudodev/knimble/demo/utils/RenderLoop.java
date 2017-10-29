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
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;
import static org.lwjgl.nanovg.NanoVG.nvgCreateFont;
import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import org.lwjgl.nanovg.NanoVGGL3;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_ANTIALIAS;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;
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
    private boolean vsync;
    private final int windowWidth = 1600;
    private final int windowHeight = 900;
    private final float FOV = (float) Math.toRadians(60.0f);
    private final float Z_NEAR = 0.01f;
    private final float Z_FAR = 1000.f;

    private double lastFrame;
    private int currFPS, FPS;
    private float fpsTime;

    private boolean wireframe = true;

    private final String title;
    protected Camera camera = new Camera();
    protected final PhysicsSpace physicsSpace;
    protected List<Shape> shapes;

    public RenderLoop(String title, PhysicsSpace physicsSpace) {
        this(title, physicsSpace, true);
    }

    public RenderLoop(String title, PhysicsSpace physicsSpace, boolean vsync) {
        this.title = title;
        this.vsync = vsync;
        this.physicsSpace = physicsSpace;
    }

    protected abstract List<Shape> init(PhysicsSpace physicsSpace);

    protected abstract void update(float delta);

    private void loop() throws Exception {
        ShaderProgram shaderProgram = new ShaderProgram();

        float aspectRatio = (float) windowWidth / windowHeight;
        Matrix4f projMat = new Matrix4f().perspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
        Matrix4f viewMat = new Matrix4f();
        Matrix4f projViewMat = new Matrix4f();

        Matrix4f modelMat = new Matrix4f();

        shapes = init(physicsSpace);

        long vg = NanoVGGL3.nvgCreate(0);
        NVGColor color = NVGColor.create().r(1).g(1).b(1).a(1);
        nvgCreateFont(vg, "mono", "./assets/fonts/MOZART_0.ttf");
        NanoVG.nvgFontFace(vg, "mono");

        glfwSetTime(0);
        lastFrame = glfwGetTime();
        float delta;
        double newTime;
        while (!glfwWindowShouldClose(window)) {
            newTime = glfwGetTime();
            delta = (float) (newTime - lastFrame);
            lastFrame = newTime;

            updateFPS(delta);

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

            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            NanoVG.nvgBeginFrame(vg, windowWidth, windowHeight, 1);
            NanoVG.nvgFontSize(vg, 20.0f);
            NanoVG.nvgFillColor(vg, color);
            NanoVG.nvgText(vg, 5, 15, String.format("FPS: %d", FPS));
            NanoVG.nvgEndFrame(vg);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        // dispose
    }

    private void updateFPS(float delta) {
        fpsTime += delta;
        ++currFPS;
        if (fpsTime >= 1) {
            FPS = currFPS;
            currFPS = 0;
            fpsTime = 0;
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
        glfwSwapInterval(vsync ? GLFW_TRUE : GLFW_FALSE); // vsync
        glfwShowWindow(window);

        GL.createCapabilities();
    }
}
