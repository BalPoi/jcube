package by.bal.jcube;

import by.bal.jcube.geometry.Meshable;
import by.bal.jcube.geometry.vector.Vec;
import by.bal.jcube.geometry.vector.Vector2;
import by.bal.jcube.geometry.vector.Vector3;
import by.bal.jcube.obj.ObjParser;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;
    public static final int FPS = 60;
    private final long[] prevFramesNanos = new long[1 << 10];
    private long prevFrameNanos;
    private long frameCount = 0;

    private Canvas canvas;
    private GraphicsContext gc;

    private List<Meshable> meshes = new ArrayList<>();
    private Vector3 cameraPosition = new Vector3();


    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("JCube");
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        initMeshes();

        startAnimation();

        setupKeyboardControls(scene);
    }

    private void setupKeyboardControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            double moveSpeed = 0.1; // Скорость движения

            switch (event.getCode()) {
                case UP:
                    cameraPosition.y(cameraPosition.y() - moveSpeed); // Движение вперёд (Z уменьшается)
                    break;
                case DOWN:
                    cameraPosition.y(cameraPosition.y() + moveSpeed); // Движение назад
                    break;
                case LEFT:
                    cameraPosition.x(cameraPosition.x() - moveSpeed); // Движение влево
                    break;
                case RIGHT:
                    cameraPosition.x(cameraPosition.x() + moveSpeed); // Движение вправо
                    break;
                case W:
                    cameraPosition.z(cameraPosition.z() + moveSpeed); // Вверх (дополнительно)
                    break;
                case S:
                    cameraPosition.z(cameraPosition.z() - moveSpeed); // Вниз
                    break;
            }
        });
    }

    private void initMeshes() throws IOException {
        // Cube cube = new Cube(Vector3.of(0, 0, 2));
        // meshes.add(cube);
        // Parallelepiped parall = new Parallelepiped(Vector3.of(1, 2, 2), Vector3.of(1, 1, 4));
        // meshes.add(parall);
        //
        // Random rnd = new Random();
        // for (int i = 0; i < 100; i++) {
        //     meshes.add(new Parallelepiped(
        //             Vector3.of(rnd.nextInt(1, 3), rnd.nextInt(1, 3), rnd.nextInt(1, 3)),
        //             Vector3.of(rnd.nextInt(10), rnd.nextInt(10), rnd.nextInt(2, 20)))
        //     );
        // }

        // Meshable fenceMesh = ObjParser.loadMesh("Fence.obj");
        Meshable fenceMesh = ObjParser.loadMesh("GargoneFace.obj");
        double scale = 0.1;
        Vector3 center = fenceMesh.getCenter();
        Vector3[] vertices = fenceMesh.getVertices();
        center.mulScalar(scale);
        for (Vector3 vertex : vertices) {
            vertex.mulScalar(scale);
        }


        Vector3 offset = Vec.of(-center.x(), -center.y(), -center.z() + 1);

        center.add(offset);

        for (Vector3 vertex : vertices) {
            vertex.add(offset);

            vertex.sub(center);
            vertex.rotateX(180);
            vertex.add(center);

        }

        meshes.add(fenceMesh);
    }

    private void startAnimation() {
        prevFrameNanos = System.nanoTime();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // if (now - prevFrameNanos >= FRAME_TIME) {
                clearCanvas();
                printFps(now);
                // modifyMeshes();
                renderMeshes();
                prevFrameNanos = now;
                // }
            }
        };
        animationTimer.start();
    }

    private void printFps(long now) {
        long currFps = 1_000_000_000 / (now - prevFrameNanos);
        prevFramesNanos[(int) (frameCount++ & prevFramesNanos.length - 1)] = currFps;

        double avgFps = 0;
        for (long frameNano : prevFramesNanos) {
            avgFps += frameNano;
        }
        avgFps /= prevFramesNanos.length;

        gc.fillText("Avg FPS: " + (int) avgFps, 10, 10);
    }

    private void clearCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void renderMeshes() {
        for (Meshable mesh : meshes) {
            Vector3[] vertices = mesh.getVertices();

            boolean[] vertexVisible = new boolean[vertices.length];
            double[] screenX = new double[vertices.length];
            double[] screenY = new double[vertices.length];
            double[] pointSize = new double[vertices.length];

            double centerX;
            double centerY;
            Vector3 center = mesh.getCenter().clone();
            center.sub(cameraPosition);
            centerX = ((center.x() / center.z()) + 1) * WIDTH / 2;
            centerY = ((center.y() / center.z()) + 1) * HEIGHT / 2;

            // 0. Опционально. Рендеринг центра меша
            gc.setFill(Color.BLUE);
            double centerDiameter = 10;
            double centerHalfDiameter = centerDiameter / 2;
            gc.fillOval(centerX - centerHalfDiameter, centerY - centerHalfDiameter, centerDiameter, centerDiameter);


            // 1. Рассчитываем вершины на экране
            for (int i = 0; i < vertices.length; i++) {
                // Клонируем вершину и ставим текущее положение камеры как начало координат
                Vector3 vertex = vertices[i].clone();
                vertex.sub(cameraPosition);

                if (vertex.z() < 0) {
                    vertexVisible[i] = false;
                    continue;
                }
                double xn = vertex.x() / vertex.z();
                if (xn > 1) {
                    vertexVisible[i] = false;
                    continue;
                }
                double yn = vertex.y() / vertex.z();
                if (yn > 1) {
                    vertexVisible[i] = false;
                    continue;
                }
                vertexVisible[i] = true;

                screenX[i] = (xn + 1) * WIDTH / 2;
                screenY[i] = (yn + 1) * HEIGHT / 2;
                pointSize[i] = 10 / Math.max(vertex.z(), 1);
            }

            // 2. Рендерим рёбра
            Vector2[] edges = mesh.getEdges();
            for (int i = 0; i < edges.length; i++) {
                gc.setLineWidth(0.5);
                Vector2 edge = edges[i];
                int aIdx = (int) edge.x();
                int bIdx = (int) edge.y();

                boolean aVisible = vertexVisible[aIdx];
                boolean bVisible = vertexVisible[bIdx];
                if (!aVisible || !bVisible) continue;

                gc.strokeLine(screenX[aIdx], screenY[aIdx], screenX[bIdx], screenY[bIdx]);
            }

            // 3. Рендерим вершины
            for (int i = 0; i < vertices.length; i++) {
                gc.setFill(Color.BLACK);

                if (!vertexVisible[i]) continue;

                double diameter = pointSize[i];
                double halfDiameter = diameter / 2;
                gc.fillOval(screenX[i] - halfDiameter, screenY[i] - halfDiameter, diameter, diameter);
            }
        }
    }

    private void modifyMeshes() {
        // Vector3 commonCenter = new Vector3();
        // for (Mesh mesh : meshes) {
        //     commonCenter.add(mesh.getCenter());
        // }
        // commonCenter.divScalar(meshes.size());

        for (Meshable mesh : meshes) {
            Vector3[] vertices = mesh.getVertices();
            Vector3 center = mesh.getCenter();
            for (Vector3 vertex : vertices) {
                vertex.sub(center);
                // vertex.rotateX(0.1);
                vertex.rotateY(0.25);
                // vertex.rotateZ(0.1);
                vertex.add(center);
            }
        }
    }
}
