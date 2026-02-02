package by.bal.jcube.camera;

import by.bal.jcube.geometry.vector.Vector2;
import by.bal.jcube.geometry.vector.Vector3;
import lombok.Getter;
import lombok.Setter;

public class Camera {
    @Getter
    @Setter
    private Vector3 position;

    @Getter
    @Setter
    private Vector3 target;

    @Getter
    private double fov;
    private double screenDistance;


    public Camera() {
        this.position = new Vector3();
        this.target = new Vector3(0, 0, -1);
        setFov(Math.PI / 2);
    }

    public Camera(Vector3 position, double fov) {
        this.position = position;
        this.target = new Vector3(0, 0, -1);
        setFov(fov);
    }

    private double calcScreenDistance(double fov) {
        return 1.0 / Math.tan(fov / 2.0);
    }

    public void setFov(double fov) {
        this.fov = fov;
        this.screenDistance = calcScreenDistance(fov);
    }

    /**
     * Выполняется перспективная проекция точки из мирового пространства в нормализованное
     * координатное пространство (NDC - Normalized Device Coordinates) через плоскость проекции ("экран камеры").
     * <pre>
     *      +1▲y'
     *   ┌────┼────┐
     *   │    │    │
     * -1│    │    │+1
     *  ─┼────┼────┼►
     *   │    │    │x'
     *   │    │    │
     *   └────┼────┘
     *      -1│
     * , где x' и y' > 1 и < -1 выходят за пределы видимого экрана
     * </pre>
     *
     * @param worldPoint
     * @return
     */
    public Vector2 toNdc(Vector3 worldPoint) {
        Vector3 relPoint = worldPoint.cloneRelativeTo(position);
        return new Vector2(
                relPoint.x() / relPoint.z() * screenDistance,
                relPoint.y() / relPoint.z() * screenDistance
        );
    }
}
