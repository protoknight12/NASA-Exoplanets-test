package net.officer.exoplanets.measurement;

public class Parsec extends Number {
    private final double value;

    public Parsec(double value) {
        this.value = value;
    }

    public double value() {
        return value;
    }

    public String toString() {
        return value + "pc";
    }

    public double toAstronomicalUnitDouble() {
        return value * 206_265;
    }

    public AstronomicalUnit toAstronomicalUnit() {
        return new AstronomicalUnit(value * 206_265);
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
