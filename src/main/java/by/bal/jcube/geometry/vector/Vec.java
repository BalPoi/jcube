package by.bal.jcube.geometry.vector;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Vec {

    public Vector2 of(double x, double y) {
        return new Vector2(x, y);
    }

    public Vector3 of(double x, double y, double z) {
        return new Vector3(x, y, z);
    }

    public static Vector3 getOnes() {
        return new Vector3(1.0, 1.0, 1.0);
    }

}
