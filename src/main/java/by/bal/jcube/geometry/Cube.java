package by.bal.jcube.geometry;

import by.bal.jcube.geometry.vector.Vec;
import by.bal.jcube.geometry.vector.Vector3;

public class Cube extends Parallelepiped {
    public Cube(Vector3 center, double scale) {
        super(Vec.of(1, 1, 1), center, scale);
    }

    public Cube(Vector3 center) {
        super(Vec.of(1, 1, 1), center, 1.0);
    }
}
