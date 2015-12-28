package engine.object3d;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Quaternionf;

public class Object3d {

    //TODO: matrix caching booleans

    protected Vector3f position;
    protected Quaternionf rotation;  //TODO: find a way to support point-rotation in addition to standard rotation
    protected Vector3f scale;

    protected Matrix4f localToWorld;
    protected Matrix4f worldToLocal;

    //TODO: parent
    //TODO: children

    public Object3d() {
        position = new Vector3f();
        rotation = new Quaternionf();
        scale = new Vector3f(1, 1, 1);

        localToWorld = new Matrix4f();
        worldToLocal = new Matrix4f();
    }

    public void translate(Vector3f v) {
        position.add(v);
    }

    public void rotate(Vector3f axis, float angle) {
        Quaternionf rot = new Quaternionf(new AxisAngle4f(angle, axis));
        rot.normalize();
        rot.mul(rotation, rotation);
    }

    public void scale(Vector3f v) {
        scale.x *= v.x;
        scale.y *= v.y;
        scale.z *= v.z;
    }

    public void scale(float f) {
        scale.mul(f);
    }

    public Matrix4f getLocalToWorld() {
        /*
            Because we want to do the following in this order:
                1. scale the object
                2. rotate it to the correct orientation
                3. translate it to it's correct position
            And because we are using column major matrices (and they are post-multiplied together), we must apply these
            transformations to an identity matrix in reverse order to get the desired effect.
         */

        localToWorld = new Matrix4f();

        localToWorld.translate(position);
        localToWorld.rotate(rotation);
        localToWorld.scale(scale);

        return localToWorld;
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

    public Matrix4f getWorldToLocal() {
        return worldToLocal;
    }
}