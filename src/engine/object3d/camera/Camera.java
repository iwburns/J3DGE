package engine.object3d.camera;

import engine.object3d.Object3d;
import org.joml.Matrix4f;

public abstract class Camera extends Object3d {

    protected Matrix4f projection;
    protected boolean projectionNeedsUpdate;

    protected float aspectRatio;
    protected float near;
    protected float far;

    protected Camera() {
        super();
        projection = new Matrix4f();
        projectionNeedsUpdate = false;
    }

    public abstract Matrix4f getProjection();

    public Matrix4f getView() {
        return getWorldToLocal();
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        projectionNeedsUpdate = true;
    }

    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        this.near = near;
        projectionNeedsUpdate = true;
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        this.far = far;
        projectionNeedsUpdate = true;
    }
}
