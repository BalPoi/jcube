package by.bal.jcube;

public class Vector2 implements Cloneable {
    public double x;
    public double y;

    public Vector2() {
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2 of(double x, double y) {
        return new Vector2(x, y);
    }

    @Override
    public Vector2 clone() {
        return new Vector2(x, y);
    }
}
