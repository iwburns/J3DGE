package engine;

import engine.geometry.Geometry;
import engine.material.Material;
import engine.object3d.Mesh;
import engine.object3d.camera.Camera;
import engine.object3d.camera.PerspectiveCamera;
import engine.render.Scene;
import engine.util.Draw3dUtils;
import org.joml.Vector3f;

public class Game {

    public final int TARGET_FPS = 60;
    public final int TARGET_UPS = 30;

    public int width = 800;
    public int height = 600;

    private Scene scene;
    private PerspectiveCamera camera;

    Mesh box;

    public Game() {
        scene = new Scene();
        camera = new PerspectiveCamera(75,  (float)(width)/(height), 0.00001f, 100);
    }

    public Game(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void init() {
        Geometry boxGeometry = Draw3dUtils.cubeGeometry(1, 1, 1, 1, 1, 1);
        Material boxMaterial = new Material();
        box = new Mesh(boxGeometry, boxMaterial);
        box.translate(new Vector3f(0, 0, -5));

        scene.add(box);
    }

    public void update() {
        box.rotate(new Vector3f(0, 0, 1), 1);
    }

    public Scene getScene() {
        return scene;
    }

    public Camera getCamera() {
        return camera;
    }

}
