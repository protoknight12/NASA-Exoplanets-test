package net.officer.exoplanets.program;

import imgui.ImGui;

public class BaseLayer extends ExoplanetImGuiLayer {
    @Override
    public void imGui() {
        ImGui.begin("Yippe");
        ImGui.text("it works");
        ImGui.end();
    }
}
