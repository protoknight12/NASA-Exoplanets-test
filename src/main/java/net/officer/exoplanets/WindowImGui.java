package net.officer.exoplanets;

import static imgui.ImGui.*;
import imgui.ImVec4;
import imgui.app.Application;
import imgui.app.Configuration;

public class WindowImGui extends Application {
    @Override
    protected void configure(Configuration config) {
        config.setTitle("Got a window working");
    }

    @Override
    public void process() {
        setWindowFontScale(2f);
        textColored(new ImVec4(.5f, .2f, 1f, 1f), "Hello from ImGui!");
        setWindowFontScale(1f);
    }

    public static void main(String[] args) {
        launch(new WindowImGui());
    }
}
