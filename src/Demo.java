import engine.Game;
import engine.geometry.Geometry;
import engine.input.Keyboard;
import engine.material.Material;
import engine.input.Mouse;
import engine.material.PhongMaterial;
import engine.object3d.Mesh;
import engine.object3d.Object3d;
import engine.object3d.camera.Camera;
import engine.object3d.camera.PerspectiveCamera;
import engine.object3d.light.Light;
import engine.render.Scene;
import engine.util.Color;
import engine.util.Draw3dUtils;
import org.joml.Vector3f;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Demo extends Game {

    private Scene scene;
    private PerspectiveCamera camera;
    private Keyboard keyboard;
    private Mouse mouse;

    private Vector3f cameraMotion;

    Object3d object1;
    Mesh mesh1;
    Mesh mesh2;

    private int width;
    private int height;

    private boolean shouldClose = false;

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
        keyboard = new Keyboard();
        mouse = new Mouse();

        camera.moveForward(-10);
        camera.moveUp(1);

        Material basicMaterial = new Material();
        Material phongMaterial = new PhongMaterial(Color.WHITE, 100f);
        Geometry mesh1Geo = Draw3dUtils.cubeGeometry(1, 1, 1, Color.RED);
        Geometry mesh2Geo = Draw3dUtils.cubeGeometry(0.5f, 2, 0.5f, Color.BLUE);
        Geometry lightBallGeo1 = Draw3dUtils.sphereGeometry(0.1f, 10, 10, Color.RED);
        Geometry lightBallGeo2 = Draw3dUtils.sphereGeometry(0.1f, 10, 10, Color.GREEN);
        Geometry lightBallGeo3 = Draw3dUtils.sphereGeometry(0.1f, 10, 10, Color.BLUE);
        Geometry gridGeo = Draw3dUtils.gridHelper(10, 1);
        Geometry axisGeo = Draw3dUtils.axisHelper(1);

        Geometry wallGeo = Draw3dUtils.cubeGeometry(10, 10, 0.5f, new Color(0.5f, 0.5f, 0.5f));

        Mesh gridMesh = new Mesh(gridGeo, basicMaterial);
        Mesh axisMesh = new Mesh(axisGeo, basicMaterial);
        scene.add(gridMesh);
        scene.add(axisMesh);

        mesh1 = new Mesh(mesh1Geo, phongMaterial);
        mesh2 = new Mesh(mesh2Geo, phongMaterial);
        Mesh lightBall1 = new Mesh(lightBallGeo1, basicMaterial);
        Mesh lightBall2 = new Mesh(lightBallGeo2, basicMaterial);
        Mesh lightBall3 = new Mesh(lightBallGeo3, basicMaterial);

        Mesh wallMesh = new Mesh(wallGeo, phongMaterial);
        Mesh wallMesh2 = new Mesh(wallGeo, phongMaterial);

        wallMesh.rotateY(90, true);
        wallMesh.translate(new Vector3f(5, 0, 0));
        wallMesh2.translate(new Vector3f(0, 0, -5));

        scene.add(wallMesh);
        scene.add(wallMesh2);

        object1 = new Object3d();
        scene.add(object1);

        object1.addChild(mesh1);
        mesh1.translate(new Vector3f(0, 1, 0));
        mesh1.addChild(mesh2);
        mesh2.translate(new Vector3f(2, 0, 0));

        float ambient = 0.001f;
        float attenuation = 0.1f;

        Light light1 = new Light(Color.RED, attenuation, ambient);
        scene.addLight(light1);
        light1.addChild(lightBall1);
        light1.moveForward(-3);
        light1.moveRight(3);
        light1.moveUp(0);

        Light light2 = new Light(Color.GREEN, attenuation, ambient);
        scene.addLight(light2);
        light2.addChild(lightBall2);
        light2.moveForward(3);
        light2.moveRight(3);
        light2.moveUp(0);

        Light light3 = new Light(Color.BLUE, attenuation, ambient);
        scene.addLight(light3);
        light3.addChild(lightBall3);
        light3.moveForward(3);
        light3.moveRight(-3);
        light3.moveUp(0);
    }

    @Override
    public void update(float delta) {
//        mesh2.moveForward(5f * delta);
        object1.rotate(new Vector3f(1, 0, 0), 50 * delta);
        mesh1.rotate(new Vector3f(0, 0, 1), 50 * delta);
        mesh2.rotate(new Vector3f(0, 1, 0), 50 * delta);

        updateCamera(delta);
    }

    private void updateCamera(float delta) {
        float moveAmt = 5f * delta;
        float rotationMultiplier = 35f * delta;
        float xRotAmt;
        float yRotAmt;

        cameraMotion = new Vector3f();

        if (keyboard.isKeyDown(GLFW_KEY_ESCAPE)) {
            shouldClose = true;
        }
        if (keyboard.isKeyDown(GLFW_KEY_W)) {
            cameraMotion.add(new Vector3f(0, 0, -moveAmt));
        }
        if (keyboard.isKeyDown(GLFW_KEY_S)) {
            cameraMotion.add(new Vector3f(0, 0, moveAmt));
        }
        if (keyboard.isKeyDown(GLFW_KEY_A)) {
            cameraMotion.add(new Vector3f(-moveAmt, 0, 0));
        }
        if (keyboard.isKeyDown(GLFW_KEY_D)) {
            cameraMotion.add(new Vector3f(moveAmt, 0, 0));
        }
        if (keyboard.isKeyDown(GLFW_KEY_SPACE)) {
            cameraMotion.add(new Vector3f(0, moveAmt, 0));
        }
        if (keyboard.isKeyDown(GLFW_KEY_LEFT_SHIFT)) {
            cameraMotion.add(new Vector3f(0, -moveAmt, 0));
        }

        camera.translateRelativeToRotation(cameraMotion);

        if (mouse.isButtonDown(GLFW_MOUSE_BUTTON_2)) {

            xRotAmt= (float) mouse.getDeltaX() * rotationMultiplier;
            yRotAmt= (float) mouse.getDeltaY() * rotationMultiplier;

            camera.rotateY(-xRotAmt, true);
            camera.rotateX(-yRotAmt);
        }
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

    @Override
    public Mouse getMouse() {
        return mouse;
    }

    @Override
    public int getWidth() {
        return DEFAULT_WIDTH;
    }

    @Override
    public int getHeight() {
        return DEFAULT_HEIGHT;
    }

    @Override
    public int getTargetFps() {
        return DEFAULT_TARGET_FPS;
    }

    @Override
    public int getTargetUps() {
        return DEFAULT_TARGET_UPS;
    }

    @Override
    public boolean shouldClose() {
        return shouldClose;
    }
}
