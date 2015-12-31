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

}
