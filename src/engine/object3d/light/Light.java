package engine.object3d.light;

import engine.object3d.Object3d;
import engine.util.Color;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Light extends Object3d {

    protected Color color;
    protected float attenuation;
    protected float ambient;

    public Light(Color color) {
        this(color, 1, 0);
    }

    public Light(Color color, float attenuation, float ambient) {
        super();
        this.color = color;
        this.attenuation = attenuation;
        this.ambient = ambient;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(float r, float g, float b) {
        color.setR(r);
        color.setG(g);
        color.setB(b);
    }

    public float getAttenuation() {
        return attenuation;
    }

    public void setAttenuation(float attenuation) {
        this.attenuation = attenuation;
    }

    public float getAmbient() {
        return ambient;
    }

    public void setAmbient(float ambient) {
        this.ambient = ambient;
    }
}
