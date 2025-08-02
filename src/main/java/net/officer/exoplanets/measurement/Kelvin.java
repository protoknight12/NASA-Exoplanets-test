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
        return value + "pc";
    }

    public Color toColor() {
        double red = 0;
        double blue = 0;
        double green = 0;

        if(value <= 66) {
            red = 255;
        } else {
            red = value - 60;
            red = 329.698727446 * Math.pow(red, -0.1332047592);
            if(red < 0) red = 0;
            if(red > 255) red = 255;
        }

        if(value <= 66) {
            green = value;
        } else {
            green = 99.4708025861 - 60;
            red = 329.698727446 * Math.pow(red, -0.1332047592);
            if(red < 0) red = 0;
            if(red > 255) red = 255;
        }

        return new Color((int) red, (int) blue, (int) green);
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
