package engine.object3d.camera;

public class PerspectiveCamera extends Camera {

    public PerspectiveCamera(float fovDegrees, float aspectRatio, float near, float far) {
        super();

        projection.setPerspective((float)Math.toRadians(fovDegrees), aspectRatio, near, far);
    }

}
