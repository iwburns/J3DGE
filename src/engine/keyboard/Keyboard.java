package engine.keyboard;

import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {

    public static final int KEY_STATUS_UP = 0;
    public static final int KEY_STATUS_DOWN = 1;

    private static GLFWKeyCallback DEFAULT_KEY_CALLBACK = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, GLFW_TRUE); // We will detect this in our rendering loop
        }
    };

    private GLFWKeyCallback keyCallback;
    private HashMap<Integer, Integer> keyStatus = new HashMap<>();

    public Keyboard(GLFWKeyCallback keyCallback) {
        if (keyCallback == null) {
            this.keyCallback = DEFAULT_KEY_CALLBACK;
        } else {
            this.keyCallback = keyCallback;
        }
    }

    public GLFWKeyCallback getGLFWKeyCallback() {
        return keyCallback;
    }

    public void destroy() {
        keyCallback.release();
    }

    public void setKeyStatus(int key, int status) {
        keyStatus.put(key, status);
    }

    public boolean isKeyDown(int key) {
        if (keyStatus.get(key) == null) {
            return false;
        }
        return keyStatus.get(key) == KEY_STATUS_DOWN;
    }

}
