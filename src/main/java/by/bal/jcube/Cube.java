package by.bal.jcube;

public class Cube extends Parallelepiped {
    public Cube(Vector3 center, double scale) {
        super(Vector3.getOnes(), center, scale);
    }

    public Cube(Vector3 center) {
        super(Vector3.getOnes(), center, 1.0);
    }
}
