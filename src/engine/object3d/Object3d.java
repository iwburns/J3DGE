package engine.object3d;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Quaternionf;

public class Object3d {

    //TODO: matrix caching booleans

    protected Vector3f position;
    protected Quaternionf rotation;
    protected Vector3f scale;

    protected Matrix4f localToWorld;
    protected Matrix4f worldToLocal;

    //TODO: parent
    //TODO: children

    public Object3d() {
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = new Vector3f();

        localToWorld = new Matrix4f();
        worldToLocal = new Matrix4f();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Matrix4f getLocalToWorld() {
        return localToWorld;
    }

    public Matrix4f getWorldToLocal() {
        return worldToLocal;
    }
}
