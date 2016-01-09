package engine.mouse;

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

    private double xDelta;
    private double yDelta;

    private double xPos;
    private double yPos;

    public Mouse() {

        buttonStatuses = new HashMap<>();

        xPos = 0;
        yPos = 0;

        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                xDelta = xpos - xPos;
                yDelta = ypos - yPos;

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

    public double getxDelta() {
        return xDelta;
    }

    public double getyDelta() {
        return yDelta;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }
}
