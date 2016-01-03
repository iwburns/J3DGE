package engine;

import engine.object3d.camera.Camera;
import engine.render.Scene;
import org.lwjgl.glfw.GLFWKeyCallback;

public abstract class Game {

    public final int TARGET_FPS = 60;
    public final int TARGET_UPS = 30;

    protected static final int DEFAULT_WIDTH = 800;
    protected static final int DEFAULT_HEIGHT = 600;

    public int width;
    public int height;

    public abstract void init();

    public abstract void update();

    public abstract Scene getScene();

    public abstract Camera getCamera();

    public abstract GLFWKeyCallback getKeyCallback();

}
