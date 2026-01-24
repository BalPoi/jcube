package by.bal.jcube;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjParser {
    public static Meshable loadMesh(String resourcePath) throws IOException {
        List<Vector3> vertices = new ArrayList<>();
        List<Vector2> edges = new ArrayList<>();

        // Для хранения граней и извлечения рёбер
        List<List<Integer>> faces = new ArrayList<>();

        InputStream inputStream = ObjParser.class.getClassLoader().getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        );
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("#") || line.isEmpty()) {
                continue; // Пропускаем комментарии и пустые строки
            }

            String[] parts = line.split("\\s+");
            if (parts.length < 2) continue;

            // Вершины
            if (parts[0].equals("v")) {
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                double z = Double.parseDouble(parts[3]);
                vertices.add(new Vector3(x, y, z));
            }
            // Грани (полигоны)
            else if (parts[0].equals("f")) {
                List<Integer> faceVertices = new ArrayList<>();

                // parts[1], parts[2], ... содержат индексы вершин
                for (int i = 1; i < parts.length; i++) {
                    // Формат может быть "1", "1/2", "1/2/3" - берём только первый номер
                    String[] vertexData = parts[i].split("/");
                    int vertexIndex = Integer.parseInt(vertexData[0]);

                    // OBJ использует 1-индексацию, а нам нужна 0-индексация
                    faceVertices.add(vertexIndex - 1);
                }
                faces.add(faceVertices);
            }
        }
        reader.close();

        // Преобразуем грани в рёбра (уникальные пары вершин)
        edges = extractEdgesFromFaces(faces);

        return new Mesh(vertices.toArray(new Vector3[0]),
                            edges.toArray(new Vector2[0]));
    }

    private static List<Vector2> extractEdgesFromFaces(List<List<Integer>> faces) {
        Set<String> edgeSet = new HashSet<>(); // Для уникальности рёбер

        for (List<Integer> face : faces) {
            int size = face.size();
            for (int i = 0; i < size; i++) {
                int a = face.get(i);
                int b = face.get((i + 1) % size); // Замыкаем полигон

                // Сортируем индексы, чтобы ребро (1,2) и (2,1) считалось одним
                int min = Math.min(a, b);
                int max = Math.max(a, b);
                String edgeKey = min + "," + max;

                edgeSet.add(edgeKey);
            }
        }

        // Конвертируем Set обратно в список Vector2
        List<Vector2> edges = new ArrayList<>();
        for (String edgeKey : edgeSet) {
            String[] indices = edgeKey.split(",");
            int a = Integer.parseInt(indices[0]);
            int b = Integer.parseInt(indices[1]);
            edges.add(new Vector2(a, b));
        }

        return edges;
    }
}
