package engine.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Keyboard {

    //TODO: make all of this static, you can't create more than one keyboard

    public static final int KEY_STATUS_UP = 0;
    public static final int KEY_STATUS_DOWN = 1;

    private GLFWKeyCallback keyCallback;
    private HashMap<Integer, Integer> keyStatuses;

    public Keyboard() {
        keyStatuses = new HashMap<>();
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW_PRESS) {
                    setKeyStatus(key, KEY_STATUS_DOWN);
                } else if (action == GLFW_RELEASE) {
                    setKeyStatus(key, KEY_STATUS_UP);
                }
            }
        };
    }

    public GLFWKeyCallback getGLFWKeyCallback() {
        return keyCallback;
    }

    public void setKeyStatus(int key, int status) {
        keyStatuses.put(key, status);
    }

    public boolean isKeyDown(int key) {
        if (keyStatuses.get(key) == null) {
            return false;
        }
        return keyStatuses.get(key) == KEY_STATUS_DOWN;
    }

    public void destroy() {
        keyCallback.release();
        keyStatuses = null;
    }

}
