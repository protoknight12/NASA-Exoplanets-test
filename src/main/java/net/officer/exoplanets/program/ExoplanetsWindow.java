package net.officer.exoplanets.program;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.officer.exoplanets.measurement.Kelvin;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class ExoplanetsWindow {
    public final ImGuiImplGlfw imGuiGLFW = new ImGuiImplGlfw();
    public final ImGuiImplGl3 imGuiGL3 = new ImGuiImplGl3();
    private String glslVersion = null;
    public long window;
    public ExoplanetImGuiLayer layer;

    public ExoplanetsWindow(ExoplanetImGuiLayer layer) {
        this.layer = layer;
    }

    public void init() {
        initWindow();
        initImGui();
        imGuiGLFW.init(this.window, true);
        imGuiGL3.init(glslVersion);
    }

    public void destroy() {
        imGuiGLFW.shutdown();
        imGuiGL3.shutdown();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(this.window);
        glfwDestroyWindow(this.window);
        glfwTerminate();
    }

    public void initWindow() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()) throw new IllegalStateException("Unable to init GLFW");

        glslVersion = "#version 330";
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

        this.window = GLFW.glfwCreateWindow(1920, 1080, "Testing window", NULL, NULL);
        if (this.window == NULL) throw new RuntimeException("Failed to create the GLFW window");

        glfwMakeContextCurrent(this.window);
        glfwSwapInterval(1);
        glfwShowWindow(this.window);

        GL.createCapabilities();
    }

    public void initImGui() {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();

        io.setIniFilename(null);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        io.getFonts().addFontFromFileTTF("src/main/resources/assets/fonts/arial.ttf", 20f);
        io.getFonts().build();
        //io.getFonts().addFontDefault();
    }

    public void run() {
        while (!GLFW.glfwWindowShouldClose(this.window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            float[] color = new Kelvin(10000).toColorFloat4();

            //glClearColor(.1f, .1f, .15f, 1.0f);
            glClearColor(color[0], color[1], color[2], color[3]);

            imGuiGLFW.newFrame();
            imGuiGL3.newFrame();
            ImGui.newFrame();

            layer.imGui();

            ImGui.render();
            imGuiGL3.renderDrawData(ImGui.getDrawData());

            if(ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                glfwMakeContextCurrent(backupWindowPtr);
            }

            GLFW.glfwSwapBuffers(this.window);
            GLFW.glfwPollEvents();
        }
    }
}
