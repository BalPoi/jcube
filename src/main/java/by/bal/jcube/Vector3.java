package by.bal.jcube;

public class Vector3 extends Vector2 implements Cloneable {
    public double z;

    public Vector3() {
    }

    public Vector3(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }

    public static Vector3 of(double x, double y, double z) {
        return new Vector3(x, y, z);
    }

    public static Vector3 getOnes() {
        return new Vector3(1.0, 1.0, 1.0);
    }

    public void mulScalar(double a) {
        x *= a;
        y *= a;
        z *= a;
    }

    public void divScalar(double a) {
        x /= a;
        y /= a;
        z /= a;
    }

    public void add(Vector3 vec) {
        x += vec.x;
        y += vec.y;
        z += vec.z;
    }

    public void sub(Vector3 vec) {
        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
    }

    public void mull(Vector3 vec) {
        x *= vec.x;
        y *= vec.y;
        z *= vec.z;
    }

    public void div(Vector3 vec) {
        x /= vec.x;
        y /= vec.y;
        z /= vec.z;
    }

    public void rotateX(double angle) {
        double radians = Math.toRadians(angle);
        double sinA = Math.sin(radians);
        double cosA = Math.cos(radians);

        double oldY = y;

        y = y * cosA + z * -sinA;
        z = oldY * sinA + z * cosA;
    }

    public void rotateY(double angle) {
        double radians = Math.toRadians(angle);
        double sinA = Math.sin(radians);
        double cosA = Math.cos(radians);

        double oldX = x;

        x = x * cosA + z * sinA;
        z = oldX * -sinA + z * cosA;
    }

    public void rotateZ(double angle) {
        double radians = Math.toRadians(angle);
        double sinA = Math.sin(radians);
        double cosA = Math.cos(radians);

        double oldX = x;

        x = x * cosA + y * -sinA;
        y = oldX * sinA + y * cosA;
    }

    @Override
    public Vector3 clone() {
        return new Vector3(x, y, z);
    }
}
