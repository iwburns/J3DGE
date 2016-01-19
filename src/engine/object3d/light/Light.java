package engine.object3d.light;

import engine.object3d.Object3d;
import org.joml.Vector3f;

public class Light extends Object3d {

    protected Vector3f color;

    public Light(float r, float g, float b) {
        super();
        color = new Vector3f(r, g, b);
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(float r, float g, float b) {
        color.x = r;
        color.y = g;
        color.z = b;
    }
}
