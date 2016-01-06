import engine.Game;
import engine.geometry.Geometry;
import engine.keyboard.Keyboard;
import engine.material.Material;
import engine.object3d.Mesh;
import engine.object3d.Object3d;
import engine.object3d.camera.Camera;
import engine.object3d.camera.PerspectiveCamera;
import engine.render.Scene;
import engine.util.Draw3dUtils;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Demo extends Game {

    private Scene scene;
    private PerspectiveCamera camera;
    private Keyboard keyboard;

    private Vector3f cameraMotion;

    Object3d object1;
    Mesh mesh1;
    Mesh mesh2;
    Mesh mesh3;

    public Demo() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Demo(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void init() {

        scene = new Scene();
        camera = new PerspectiveCamera(75,  (float)(width)/(height), 0.01f, 10000);

        cameraMotion = new Vector3f();
        initKeyboard();

        camera.moveForward(-10);
        camera.moveRight(2);
        camera.moveUp(1);

        Material material = new Material();
        Geometry mesh1Geo = Draw3dUtils.cubeGeometry(1, 1, 1, 1, 0, 0);
        Geometry mesh2Geo = Draw3dUtils.cubeGeometry(0.5f, 2, 0.5f, 0, 0, 1);
        Geometry mesh3Geo = Draw3dUtils.sphereGeometry(0.1f, 10, 10, 0, 1, 0);
        Geometry gridGeo = Draw3dUtils.gridHelper(10, 1);
        Geometry axisGeo = Draw3dUtils.axisHelper(1);

        Mesh gridMesh = new Mesh(gridGeo, material);
        Mesh axisMesh = new Mesh(axisGeo, material);
        scene.add(gridMesh);
        scene.add(axisMesh);

        mesh1 = new Mesh(mesh1Geo, material);
        mesh2 = new Mesh(mesh2Geo, material);
        mesh3 = new Mesh(mesh3Geo, material);

        mesh1.translate(new Vector3f(0, 1, 0));
        mesh2.translate(new Vector3f(2, 0, 0));
        mesh3.translate(new Vector3f(1, 0, 0));

        object1 = new Object3d();

        scene.add(object1);
        object1.addChild(mesh1);
        mesh1.addChild(mesh2);
        mesh2.addChild(mesh3);
    }

    @Override
    public void update() {
        mesh2.moveForward(0.1f);
        object1.rotate(new Vector3f(1, 0, 0), 1f);
        mesh1.rotate(new Vector3f(0, 0, 1), 1f);
        mesh2.rotate(new Vector3f(0, 1, 0), 1f);

        if (keyboard.isKeyDown(GLFW_KEY_W)) {
            camera.translateRelativeToRotation(new Vector3f(0, 0, -0.1f));
        }
        //camera.translateRelativeToRotation(cameraMotion);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    @Override
    public Keyboard getKeyboard() {
        return keyboard;
    }

    private void initKeyboard() {
        keyboard = new Keyboard(new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GLFW_TRUE);

                cameraMotion = new Vector3f();

                //TODO: figure out why this is so jumpy

                if (key == GLFW_KEY_W) {
                    if (action == GLFW_PRESS) {
                        keyboard.setKeyStatus(key, Keyboard.KEY_STATUS_DOWN);
                    } else if (action == GLFW_RELEASE) {
                        keyboard.setKeyStatus(key, Keyboard.KEY_STATUS_UP);
                    }
                }
                if (key == GLFW_KEY_S && action == GLFW_PRESS) {
                    cameraMotion.add(new Vector3f(0, 0,  0.1f));
                }
                if (key == GLFW_KEY_A && action == GLFW_PRESS) {
                    cameraMotion.add(new Vector3f(-0.1f, 0, 0));
                }
                if (key == GLFW_KEY_D && action == GLFW_PRESS) {
                    cameraMotion.add(new Vector3f( 0.1f, 0, 0));
                }
                if (key == GLFW_KEY_SPACE && action == GLFW_PRESS) {
                    cameraMotion.add(new Vector3f(0,  0.1f, 0));
                }
                if (key == GLFW_KEY_LEFT_SHIFT && action == GLFW_PRESS) {
                    cameraMotion.add(new Vector3f(0, -0.1f, 0));
                }
            }
        });
    }
}
