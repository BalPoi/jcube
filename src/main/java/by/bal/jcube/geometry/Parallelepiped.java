package by.bal.jcube.geometry;

import by.bal.jcube.geometry.vector.Vec;
import by.bal.jcube.geometry.vector.Vector2;
import by.bal.jcube.geometry.vector.Vector3;

public class Parallelepiped implements Meshable {
    public Vector3[] vertices;
    public Vector2[] edges;

    public Vector3 size;

    public Vector3 center;

    public Parallelepiped(Vector3 size, Vector3 center, double scale) {
        this.center = center;

        size.mulScalar(scale);
        this.size = size;

        double xSizeHalf = this.size.x() / 2;
        double ySizeHalf = this.size.y() / 2;
        double zSizeHalf = this.size.z() / 2;
        double xCenterOffset = this.center.x();
        double yCenterOffset = this.center.y();
        double zCenterOffset = this.center.z();
        vertices = new Vector3[]{
                new Vector3(xSizeHalf + xCenterOffset, ySizeHalf + yCenterOffset, zSizeHalf + zCenterOffset),
                new Vector3(xSizeHalf + xCenterOffset, -ySizeHalf + yCenterOffset, zSizeHalf + zCenterOffset),
                new Vector3(-xSizeHalf + xCenterOffset, -ySizeHalf + yCenterOffset, zSizeHalf + zCenterOffset),
                new Vector3(-xSizeHalf + xCenterOffset, ySizeHalf + yCenterOffset, zSizeHalf + zCenterOffset),

                new Vector3(xSizeHalf + xCenterOffset, ySizeHalf + yCenterOffset, -zSizeHalf + zCenterOffset),
                new Vector3(xSizeHalf + xCenterOffset, -ySizeHalf + yCenterOffset, -zSizeHalf + zCenterOffset),
                new Vector3(-xSizeHalf + xCenterOffset, -ySizeHalf + yCenterOffset, -zSizeHalf + zCenterOffset),
                new Vector3(-xSizeHalf + xCenterOffset, ySizeHalf + yCenterOffset, -zSizeHalf + zCenterOffset)
        };

        edges = new Vector2[]{
                Vec.of(0, 1),
                Vec.of(1,2),
                Vec.of(2,3),
                Vec.of(3,0),

                Vec.of(4,5),
                Vec.of(5,6),
                Vec.of(6,7),
                Vec.of(7,4),

                Vec.of(0,4),
                Vec.of(1,5),
                Vec.of(2,6),
                Vec.of(3,7)
        };
    }

    public Parallelepiped(Vector3 size, Vector3 center) {
        this(size, center, 1.0);
    }

    @Override
    public Vector3[] getVertices() {
        return vertices;
    }

    @Override
    public Vector2[] getEdges() {
        return edges;
    }

    @Override
    public Vector3 getCenter() {
        return center;
    }
}
