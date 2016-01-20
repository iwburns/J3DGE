package engine.object3d.light;

import engine.object3d.Object3d;
import org.joml.Vector3f;

public class Light extends Object3d {

    protected Vector3f color;
    protected float attenuation;
    protected float ambient;

    public Light(float r, float g, float b, float attenuation, float ambient) {
        super();
        color = new Vector3f(r, g, b);
        this.attenuation = attenuation;
        this.ambient = ambient;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(float r, float g, float b) {
        color.x = r;
        color.y = g;
        color.z = b;
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
