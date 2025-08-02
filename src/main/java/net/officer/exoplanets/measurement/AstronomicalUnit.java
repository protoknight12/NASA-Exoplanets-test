package net.officer.exoplanets.measurement;

public class AstronomicalUnit extends Number {
    private final double value;

    public AstronomicalUnit(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }

    public String toString() {
        return value + "AU";
    }

    public double toParsecDouble() {
        return 0;
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
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return value;
    }
}
