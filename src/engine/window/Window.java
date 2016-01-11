package engine.window;

import engine.object3d.camera.Camera;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class Window {

    private long windowHandle;
    private int width;
    private int height;
    private String title;
    private long monitor;
    private long share;

    private IntBuffer widthBuffer;
    private IntBuffer heightBuffer;

    public Window(int width, int height, String title, long monitor, long share) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.monitor = monitor;
        this.share = share;

        widthBuffer = BufferUtils.createIntBuffer(1);
        heightBuffer = BufferUtils.createIntBuffer(1);

        windowHandle = glfwCreateWindow(width, height, title, monitor, share);
    }

    public void destroy() {
        glfwDestroyWindow(windowHandle);
    }

    private void getFrameBufferSize(IntBuffer width, IntBuffer height) {
        glfwGetFramebufferSize(windowHandle, width, height);
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public void setPosition(int xPos, int yPos) {
        glfwSetWindowPos(windowHandle, xPos, yPos);
    }

    public void setShouldClose() {
        glfwSetWindowShouldClose(windowHandle, GLFW_TRUE);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(windowHandle) == GLFW_TRUE;
    }

    private void swapBuffers() {
        glfwSwapBuffers(windowHandle);
    }

    public void update(Camera camera) {
        swapBuffers();
        updateWindowSize(camera);
    }

    private void updateWindowSize(Camera camera) {
        getFrameBufferSize(widthBuffer, heightBuffer);

        width = widthBuffer.get();
        height = heightBuffer.get();

        widthBuffer.flip();
        heightBuffer.flip();

        glViewport(0, 0, width, height);
        camera.setAspectRatio((float)width / (float)height);
    }

}
