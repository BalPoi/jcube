package by.bal.jcube.camera;

import by.bal.jcube.geometry.vector.Vector2;
import by.bal.jcube.geometry.vector.Vector3;
import lombok.Getter;
import lombok.Setter;

public class Camera {
    private static final double DEFAULT_FOV = Math.PI / 2;

    @Getter
    @Setter
    private Vector3 position;

    @Getter
    private double fov;
    private double screenDistance;


    public Camera() {
        this(new Vector3(), DEFAULT_FOV);
    }

    public Camera(Vector3 position) {
        this(position, DEFAULT_FOV);
    }

    public Camera(Vector3 position, double fov) {
        this.position = position;
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
