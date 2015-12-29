package engine.object3d.camera;

import engine.object3d.Object3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera extends Object3d {

    protected Matrix4f projection;

    public Camera() {
        super();
        projection = new Matrix4f();
    }

    public Matrix4f getProjection() {
        return projection;
    }

    public Matrix4f getView() {
        return getWorldToLocal();
    }

    //TODO: possibly move these into object3d itself
    public void translateRelativeToLocal(Vector4f v) {
        Vector4f worldTranslation = getLocalToWorld().transform(v);
        translate(new Vector3f(worldTranslation.x, worldTranslation.y, worldTranslation.z));
    }

    public void moveForward(float amt) {
        Vector4f localTargetVector = new Vector4f(0, 0, -amt, 1);
        translateRelativeToLocal(localTargetVector);
    }

    public void moveRight(float amt) {
        Vector4f localTargetVector = new Vector4f(amt, 0, 0, 1);
        translateRelativeToLocal(localTargetVector);
    }

    public void moveUp(float amt) {
        Vector4f localTargetVector = new Vector4f(0, amt, 0, 1);
        translateRelativeToLocal(localTargetVector);
    }


}
