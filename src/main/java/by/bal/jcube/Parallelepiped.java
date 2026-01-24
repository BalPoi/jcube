package by.bal.jcube;

public class Parallelepiped implements Meshable {
    public Vector3[] vertices;
    public Vector2[] edges;

    public Vector3 size;

    public Vector3 center;

    public Parallelepiped(Vector3 size, Vector3 center, double scale) {
        this.center = center;

        size.mulScalar(scale);
        this.size = size;

        double xSizeHalf = this.size.x / 2;
        double ySizeHalf = this.size.y / 2;
        double zSizeHalf = this.size.z / 2;
        double xCenterOffset = this.center.x;
        double yCenterOffset = this.center.y;
        double zCenterOffset = this.center.z;
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
                Vector2.of(0,1),
                Vector2.of(1,2),
                Vector2.of(2,3),
                Vector2.of(3,0),

                Vector2.of(4,5),
                Vector2.of(5,6),
                Vector2.of(6,7),
                Vector2.of(7,4),

                Vector2.of(0,4),
                Vector2.of(1,5),
                Vector2.of(2,6),
                Vector2.of(3,7)
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
