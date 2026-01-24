package by.bal.jcube;

public class Mesh implements Meshable {
    public Vector3[] vertices;
    public Vector2[] edges;

    public Vector3 center;

    public Mesh(Vector3[] vertices, Vector2[] edges) {
        this.vertices = vertices;
        this.edges = edges;
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
        if (center != null) return center;
        if (vertices == null || vertices.length == 0) {
            return new Vector3(0, 0, 0);
        }

        // Инициализируем начальными значениями из первой вершины
        double minX = vertices[0].x;
        double maxX = vertices[0].x;
        double minY = vertices[0].y;
        double maxY = vertices[0].y;
        double minZ = vertices[0].z;
        double maxZ = vertices[0].z;

        // Находим минимумы и максимумы по всем вершинам
        for (int i = 1; i < vertices.length; i++) {
            Vector3 vertex = vertices[i];

            // X
            if (vertex.x < minX) minX = vertex.x;
            if (vertex.x > maxX) maxX = vertex.x;

            // Y
            if (vertex.y < minY) minY = vertex.y;
            if (vertex.y > maxY) maxY = vertex.y;

            // Z
            if (vertex.z < minZ) minZ = vertex.z;
            if (vertex.z > maxZ) maxZ = vertex.z;
        }

        // Центр AABB (ограничивающего параллелепипеда)
        center = new Vector3(
                (minX + maxX) / 2,
                (minY + maxY) / 2,
                (minZ + maxZ) / 2
        );

        return center;
    }
}
