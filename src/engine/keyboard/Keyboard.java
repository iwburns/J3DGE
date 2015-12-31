package engine.keyboard;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {

    private static GLFWKeyCallback DEFAULT_KEY_CALLBACK = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, GLFW_TRUE); // We will detect this in our rendering loop
        }
    };

    private GLFWKeyCallback keyCallback;

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

}
