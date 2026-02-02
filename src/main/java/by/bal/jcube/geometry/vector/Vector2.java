package by.bal.jcube.geometry.vector;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Vector2 implements Cloneable {
    private double x;
    private double y;

    public double x() {
        return x;
    }

    public void x(double x) {
        this.x = x;
    }

    public double y() {
        return y;
    }

    public void y(double y) {
        this.y = y;
    }

    @Override
    public Vector2 clone() {
        return new Vector2(x, y);
    }
}
