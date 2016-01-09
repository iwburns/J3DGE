package engine.object3d.camera;

import engine.object3d.Object3d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera extends Object3d {

    protected Matrix4f projection;

    protected boolean maintainWorldUpVector;

    public Camera() {
        super();
        projection = new Matrix4f();
        maintainWorldUpVector = true;
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public Matrix4f getView() {
        return getWorldToLocal();
    }

    public void setMaintainWorldUpVector(boolean b) {
        this.maintainWorldUpVector = b;
    }


    //TODO: possibly move these up into Object3d

    public void rotateX(float degrees) {
        Vector3f localRotationAxis = new Vector3f(1, 0, 0);
        rotate(localRotationAxis, degrees);
    }

    public void rotateY(float degrees) {
        Vector3f localRotationAxis = new Vector3f(0, 1, 0);

        if (maintainWorldUpVector) {
            Quaternionf inverseRotation = new Quaternionf();
            rotation.invert(inverseRotation);

            localRotationAxis.rotate(inverseRotation);
        }

        rotate(localRotationAxis, degrees);
    }

    public void rotateZ(float degrees) {
        Vector3f localRotationAxis = new Vector3f(0, 0, 1);
        rotate(localRotationAxis, degrees);
    }

}
