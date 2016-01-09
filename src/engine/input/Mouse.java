package engine.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {

    public static final int MOUSE_BUTTON_STATUS_UP = 0;
    public static final int MOUSE_BUTTON_STATUS_DOWN = 1;

    private GLFWCursorPosCallback cursorPosCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;

    private HashMap<Integer, Integer> buttonStatuses;

    private double deltaX;
    private double deltaY;

    private double xPos;
    private double yPos;

    public Mouse() {

        buttonStatuses = new HashMap<>();

        xPos = 0;
        yPos = 0;

        deltaX = 0;
        deltaY = 0;

        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                deltaX = xpos - xPos;
                deltaY = ypos - yPos;

                xPos = xpos;
                yPos = ypos;
            }
        };

        mouseButtonCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == GLFW_PRESS) {
                    setButtonStatus(button, MOUSE_BUTTON_STATUS_DOWN);
                } else if (action == GLFW_RELEASE) {
                    setButtonStatus(button, MOUSE_BUTTON_STATUS_UP);
                }
            }
        };
    }

    public GLFWCursorPosCallback getCursorPosCallback() {
        return cursorPosCallback;
    }

    public GLFWMouseButtonCallback getMouseButtonCallback() {
        return mouseButtonCallback;
    }

    public void setButtonStatus(int button, int status) {
        buttonStatuses.put(button, status);
    }

    public boolean isButtonDown(int button) {
        if (buttonStatuses.get(button) == null) {
            return false;
        }
        return buttonStatuses.get(button) == MOUSE_BUTTON_STATUS_DOWN;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void resetDeltas() {
        deltaX = 0;
        deltaY = 0;
    }

    public void destroy() {
        cursorPosCallback.release();
        mouseButtonCallback.release();
        buttonStatuses = null;
    }
}
