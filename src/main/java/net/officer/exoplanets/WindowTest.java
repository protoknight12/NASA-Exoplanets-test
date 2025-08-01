package net.officer.exoplanets;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL11.*;

public class WindowTest {
    public static void main(String[] args) {
        if(!GLFW.glfwInit()) throw new IllegalStateException("Unable to init GLFW");

        long window = GLFW.glfwCreateWindow(800, 600, "Testing window", 0, 0);
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();

        while (!GLFW.glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor(.1f, .1f, .2f, 1.0f);

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }

        GLFW.glfwTerminate();
    }
}