package by.bal.jcube.geometry;

import by.bal.jcube.geometry.vector.Vector2;
import by.bal.jcube.geometry.vector.Vector3;

public interface Meshable {
    Vector3[] getVertices();
    Vector2[] getEdges();
    Vector3 getCenter();
}
