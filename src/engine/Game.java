package engine;

import engine.keyboard.Keyboard;
import engine.object3d.camera.Camera;
import engine.render.Scene;

public abstract class Game {

    protected static final int DEFAULT_TARGET_FPS = 60;
    protected static final int DEFAULT_TARGET_UPS = 30;

    protected static final int DEFAULT_WIDTH = 800;
    protected static final int DEFAULT_HEIGHT = 600;

    public abstract void init();

    public abstract void update();

    public abstract Scene getScene();

    public abstract Camera getCamera();

    public abstract Keyboard getKeyboard();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getTargetFps();

    public abstract int getTargetUps();

}
