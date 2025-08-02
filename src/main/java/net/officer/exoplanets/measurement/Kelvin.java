package net.officer.exoplanets.measurement;

import java.awt.*;

public class Kelvin extends Number {
    private final float value;

    public Kelvin(float value) {
        this.value = value;
    }

    public float value() {
        return value;
    }

    public String toString() {
        return value + "K";
    }

    public Color toColor() {
        double red;
        double blue;
        double green;
        double temp = value / 100;

        if(temp <= 66) {
            red = 255;
        } else {
            red = temp - 60;
            red = 329.698727446 * Math.pow(red, -0.1332047592);
            if(red < 0) red = 0;
            if(red > 255) red = 255;
        }

        if (temp <= 66) {
            green = temp;
            green = 99.4708025861 * Math.log(green) - 161.1195681661;
            if (green < 0) {
                green = 0;
            }
            if (green > 255) {
                green = 255;
            }
        }
        else {
            green = temp - 60;
            green = 288.1221695283 * Math.pow(green, -0.0755148492);
            if (green < 0) {
                green = 0;
            }
            if (green > 255) {
                green = 255;
            }
        }

        if (temp >= 66) {
            blue = 255;
        }
        else {
            if (temp <= 19) {
                blue = 0;
            }
            else {
                blue = temp - 10;
                blue = 138.5177312231 * Math.log(blue) - 305.0447927307;
                if (blue < 0) {
                    blue = 0;
                }
                if (blue > 255) {
                    blue = 255;
                }
            }
        }

        return new Color((int) red, (int) green, (int) blue);
    }

    public float[] toColorFloat4() {
        double red;
        double blue;
        double green;
        double temp = value / 100;

        if(temp <= 66) {
            red = 255;
        } else {
            red = temp - 60;
            red = 329.698727446 * Math.pow(red, -0.1332047592);
            if(red < 0) red = 0;
            if(red > 255) red = 255;
        }

        if (temp <= 66) {
            green = temp;
            green = 99.4708025861 * Math.log(green) - 161.1195681661;
            if (green < 0) {
                green = 0;
            }
            if (green > 255) {
                green = 255;
            }
        }
        else {
            green = temp - 60;
            green = 288.1221695283 * Math.pow(green, -0.0755148492);
            if (green < 0) {
                green = 0;
            }
            if (green > 255) {
                green = 255;
            }
        }

        if (temp >= 66) {
            blue = 255;
        }
        else {
            if (temp <= 19) {
                blue = 0;
            }
            else {
                blue = temp - 10;
                blue = 138.5177312231 * Math.log(blue) - 305.0447927307;
                if (blue < 0) {
                    blue = 0;
                }
                if (blue > 255) {
                    blue = 255;
                }
            }
        }

        return new float[]{(float) (red / 255f), (float) (green / 255f), (float) (blue / 255f), 1f};
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return (long) value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }
}
