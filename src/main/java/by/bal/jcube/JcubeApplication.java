package by.bal.jcube;

import by.bal.jcube.camera.Camera;
import by.bal.jcube.geometry.Meshable;
import by.bal.jcube.geometry.Parallelepiped;
import by.bal.jcube.geometry.vector.Vec;
import by.bal.jcube.geometry.vector.Vector2;
import by.bal.jcube.geometry.vector.Vector3;
import by.bal.jcube.obj.ObjParser;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class JcubeApplication extends Application {
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;

    public static final int FPS = 60;
    public static final double FRAME_DURATION = 1_000_000_000.0 / FPS;
    public static final double NANOS_IN_SEC = 1_000_000_000.0;

    public static final Vector2 COMPAS_CENTER = Vec.of(40, 60);
    public static final int COMPAS_SIZE = 25;

    public static final Vector3 GLOBAL_UP = Vec.of(0, 1, 0);
    public static final Vector3 GLOBAL_DOWN = Vec.of(0, -1, 0);
    public static final Vector3 GLOBAL_RIGHT = Vec.of(1, 0, 0);
    public static final Vector3 GLOBAL_LEFT = Vec.of(-1, 0, 0);
    public static final Vector3 GLOBAL_FORWARD = Vec.of(0, 0, 1);
    public static final Vector3 GLOBAL_BACKWARD = Vec.of(0, 0, -1);


    private final Text fpsLabel = new Text(10, 20, "FPS: ");
    private final Text camPositionLabel = new Text(10, 100, "XYZ");
    private long prevFrameNanos;
    private long fpsLabelNano = 0;
    private Canvas canvas;
    private GraphicsContext gc;

    private List<Meshable> meshes = new ArrayList<>();

    private Camera compasCamera = new Camera(Vec.of(0, 0, -3), Math.toRadians(30));
    private Camera mainCamera = new Camera(Vec.of(0, 0, -1), Math.toRadians(60));


    @Override
    public void start(Stage stage) throws InterruptedException, IOException {
        // Прямоугольная область для низкоуровневого рисования
        canvas = new Canvas(WIDTH, HEIGHT);
        // Набор методов для рисования на Canvas
        gc = canvas.getGraphicsContext2D();

        // Границы канваса
        gc.setLineWidth(1.0);
        gc.strokeRect(0, 0, WIDTH, HEIGHT);


        // pane - Контейнер, управляющий расположением дочерних элементов
        Pane root = new Pane(canvas, fpsLabel, camPositionLabel);

        // scene - Полный набор элементов интерфейса для этого окна
        // Особенность: В одном окне всегда одна сцена, но можно менять сцены (как навигация)
        Scene scene = new Scene(root);

        // stage - Отдельное окно программы
        // Контролирует: Размер окна, заголовок, кнопки (свернуть/закрыть), иконку
        stage.setTitle("JCube");
        stage.setScene(scene);
        stage.show();

        setupKeyboardControls(scene);

        initMeshes();
        startAnimation();
    }

    private void initMeshes() throws IOException {
        // Cube cube = new Cube(Vec.of(0, 0, 3));
        // meshes.add(cube);

        // Parallelepiped parall = new Parallelepiped(Vec.of(1, 2, 2), Vec.of(1, 1, 4));
        // meshes.add(parall);


        // Random rnd = new Random();
        // for (int i = 0; i < 10; i++) {
        //     meshes.add(new Parallelepiped(
        //             Vec.of(rnd.nextInt(1, 3), rnd.nextInt(1, 3), rnd.nextInt(3, 6)),
        //             Vec.of(rnd.nextInt(-6,6), rnd.nextInt(-6,6), rnd.nextInt(3, 20)))
        //     );
        // }

        // Meshable mesh = ObjParser.loadMesh("Fence.obj");
        // Meshable mesh = ObjParser.loadMesh("GargoneFace.obj");
        Meshable mesh = ObjParser.loadMesh("darina2.obj");
        // double scale = 0.1;
        Vector3 center = mesh.getCenter();
        Vector3[] vertices = mesh.getVertices();
        // center.mulScalar(scale);
        // for (Vector3 vertex : vertices) {
        //     vertex.mulScalar(scale);
        // }


        Vector3 offset = Vec.of(-center.x(), -center.y(), -center.z() + 100);

        center.add(offset);

        for (Vector3 vertex : vertices) {
            vertex.add(offset);

            // vertex.sub(center);
            // vertex.rotateX(Math.PI / 2);
            // vertex.rotateY(Math.PI);
            // vertex.add(center);

        }

        meshes.add(mesh);
    }

    private void setupKeyboardControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            double moveSpeed = 0.1; // Скорость движения

            if (Objects.requireNonNull(event.getCode()) == KeyCode.SPACE) {
                mainCamera.getPosition().add(Vec.of(0, moveSpeed, 0)); // Движение вверх
            }
            if (event.getCode() == KeyCode.CONTROL) {
                mainCamera.getPosition().add(Vec.of(0, -moveSpeed, 0)); // Движение вниз
            }
            if (event.getCode() == KeyCode.A) {
                mainCamera.getPosition().add(Vec.of(-moveSpeed, 0, 0)); // Движение влево
            }
            if (event.getCode() == KeyCode.D) {
                mainCamera.getPosition().add(Vec.of(moveSpeed, 0, 0)); // Движение вправо
            }
            if (event.getCode() == KeyCode.W) {
                mainCamera.getPosition().add(Vec.of(0, 0, moveSpeed)); // Движение вперёд
            }
            if (event.getCode() == KeyCode.S) {
                mainCamera.getPosition().add(Vec.of(0, 0, -moveSpeed)); // Движение назад
            }
        });
    }

    private void renderMeshes() {
        for (Meshable mesh : meshes) {
            Vector3[] vertices = mesh.getVertices();

            boolean[] vertexVisible = new boolean[vertices.length];
            double[] screenX = new double[vertices.length];
            double[] screenY = new double[vertices.length];
            double[] pointSize = new double[vertices.length];

            Vector3 relCenter = mesh.getCenter().cloneRelativeTo(mainCamera.getPosition());
            Vector2 ndcCenter = mainCamera.toNdc(relCenter);

            double centerX = (ndcCenter.x() + 1) / 2 * WIDTH;
            double centerY = (1 - ndcCenter.y()) / 2 * HEIGHT;

            // 0. Опционально. Рендеринг центра меша
            gc.setFill(Color.BLUE);
            double centerDiameter = 10;
            double centerHalfDiameter = centerDiameter / 2;
            gc.fillOval(centerX - centerHalfDiameter, centerY - centerHalfDiameter, centerDiameter, centerDiameter);


            // 1. Рассчитываем вершины на экране
            for (int i = 0; i < vertices.length; i++) {
                // Клонируем вершину и ставим текущее положение камеры как начало координат
                Vector3 vertex = vertices[i].cloneRelativeTo(mainCamera.getPosition());
                Vector2 ndcVertex = mainCamera.toNdc(vertex);

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

                screenX[i] = (ndcVertex.x() + 1) / 2 * WIDTH;
                screenY[i] = (1 - ndcVertex.y()) / 2 * HEIGHT;
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

    private void startAnimation() {
        AnimationTimer timer = new AnimationTimer() {
            @SneakyThrows
            @Override
            public void handle(long now) {
                double deltaTime = (now - prevFrameNanos) / NANOS_IN_SEC;

                drawFrame(now, deltaTime);

                prevFrameNanos = now;
            }
        };
        timer.start();
    }

    private void drawFrame(long now, double deltaTime) {
        clearCanvas();

        // UI
        drawCompas();
        drawCamPosition();
        updateFpsLabel(now, deltaTime);

        // Meshes
        renderMeshes();

        for (Meshable mesh : meshes) {
            Vector3[] vertices = mesh.getVertices();
            for (Vector3 vertex : vertices) {
                vertex.rotateYat(Math.PI / 16 * deltaTime, mesh.getCenter());
            }
        }
    }

    private void drawCamPosition() {
        camPositionLabel.setText(mainCamera.getPosition().toString());
    }

    private void updateFpsLabel(long now, double deltaTime) {
        if (now - fpsLabelNano >= NANOS_IN_SEC / 4) {
            fpsLabel.setText("FPS: " + Math.round(1 / deltaTime));
            fpsLabelNano = now;
        }
    }

    private void clearCanvas() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
    }

    private void drawCompas() {
        Vector2 ndcX = compasCamera.toNdc(GLOBAL_RIGHT);
        Vector2 ndcY = compasCamera.toNdc(GLOBAL_UP);
        Vector2 ndcZ = compasCamera.toNdc(GLOBAL_FORWARD);

        double comX = COMPAS_CENTER.x();
        double comY = COMPAS_CENTER.y();

        // X axis
        gc.setStroke(Color.RED);
        gc.strokeLine(comX, comY, comX + ndcX.x() * COMPAS_SIZE, comY - ndcX.y() * COMPAS_SIZE);
        // Y axis
        gc.setStroke(Color.GREEN);
        gc.strokeLine(comX, comY, comX + ndcY.x() * COMPAS_SIZE, comY - ndcY.y() * COMPAS_SIZE);
        // Z axis
        gc.setStroke(Color.BLUE);
        gc.strokeLine(comX, comY, comX + ndcZ.x() * COMPAS_SIZE, comY - ndcZ.y() * COMPAS_SIZE);
    }
}
