package engine.object3d.camera;

import org.joml.Matrix4f;

public class PerspectiveCamera extends Camera {

    protected float fovDegrees;

    public PerspectiveCamera(float fovDegrees, float aspectRatio, float near, float far) {
        super();
        this.fovDegrees = fovDegrees;
        this.aspectRatio = aspectRatio;
        this.near = near;
        this.far = far;
        projection.setPerspective((float)Math.toRadians(fovDegrees), aspectRatio, near, far);
    }

    @Override
    public Matrix4f getProjection() {
        if (projectionNeedsUpdate) {
            projectionNeedsUpdate = false;
            projection.setPerspective((float)Math.toRadians(fovDegrees), aspectRatio, near, far);
        }
        return projection;
    }
}
