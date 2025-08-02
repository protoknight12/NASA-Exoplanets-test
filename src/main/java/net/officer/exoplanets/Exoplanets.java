package net.officer.exoplanets;

import net.officer.exoplanets.program.BaseLayer;
import net.officer.exoplanets.program.ExoplanetsWindow;

public class Exoplanets {
    public static void main(String[] args) {
        ExoplanetsWindow window = new ExoplanetsWindow(new BaseLayer());
        window.init();
        window.run();
        window.destroy();
    }
}
