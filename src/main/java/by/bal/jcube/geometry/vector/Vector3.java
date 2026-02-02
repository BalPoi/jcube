package by.bal.jcube.geometry.vector;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Vector3 implements Cloneable {
    private double x;
    private double y;
    private double z;

    public double x() {
        return x;
    }

    public void x(double x) {
        this.x = x;
    }

    public double y() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + "," + z + ')';
    }

    public void y(double y) {
        this.y = y;
    }

    public double z() {
        return z;
    }

    public void z(double z) {
        this.z = z;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void normalize() {
        divScalar(length());
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
        x += vec.x();
        y += vec.y();
        z += vec.z();
    }

    public void sub(Vector3 vec) {
        x -= vec.x();
        y -= vec.y();
        z -= vec.z();
    }

    public Vector3 cloneRelativeTo(Vector3 origin) {
        Vector3 clone = this.clone();
        clone.sub(origin);
        return clone;
    }

    public void mull(Vector3 vec) {
        x *= vec.x();
        y *= vec.y();
        z *= vec.z();
    }

    public void div(Vector3 vec) {
        x /= vec.x();
        y /= vec.y();
        z /= vec.z();
    }

    public void rotateX(double radians) {
        double sinA = Math.sin(radians);
        double cosA = Math.cos(radians);

        double oldY = y;

        y = y * cosA + z * -sinA;
        z = oldY * sinA + z * cosA;
    }

    public void rotateY(double radians) {
        double sinA = Math.sin(radians);
        double cosA = Math.cos(radians);

        double oldX = x;

        x = x * cosA + z * sinA;
        z = oldX * -sinA + z * cosA;
    }

    public void rotateZ(double radians) {
        double sinA = Math.sin(radians);
        double cosA = Math.cos(radians);

        double oldX = x;

        x = x * cosA + y * -sinA;
        y = oldX * sinA + y * cosA;
    }

    public void rotateXat(double radian, Vector3 center) {
        this.sub(center);
        this.rotateX(radian);
        this.add(center);
    }

    public void rotateYat(double radian, Vector3 center) {
        this.sub(center);
        this.rotateY(radian);
        this.add(center);
    }

    public void rotateZat(double radian, Vector3 center) {
        this.sub(center);
        this.rotateZ(radian);
        this.add(center);
    }

    @Override
    public Vector3 clone() {
        return new Vector3(x, y, z);
    }
}
