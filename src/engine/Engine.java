package engine;

import engine.geometry.Geometry;
import engine.material.Material;
import engine.object3d.Mesh;
import engine.object3d.Object3d;
import engine.object3d.camera.PerspectiveCamera;
import engine.shader.ShaderProgram;
import engine.util.Draw3dUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Engine {

    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;

    // The window handle
    private long window;

    private Timer timer;

    public Game game;

    Mesh box;
    FloatBuffer projectionBuffer;
    FloatBuffer viewBuffer;
    FloatBuffer modelBuffer;
    PerspectiveCamera camera;

    public Engine(Game g) {
        errorCallback = GLFWErrorCallback.createPrint(System.err);

        //TODO: move to a key controls class or similar
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                    glfwSetWindowShouldClose(window, GLFW_TRUE); // We will detect this in our rendering loop
            }
        };

        game = g;
        timer = new Timer();
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        try {
            init();
            loop();

            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            // Terminate GLFW and release the GLFWErrorCallback
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void init() {
        glfwSetErrorCallback(errorCallback);

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GLFW_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(game.width, game.height, "n-body-java", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback);

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                window,
                (vidmode.width() - game.width) / 2,
                (vidmode.height() - game.height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    private void update() {
        box.rotate(new Vector3f(0, 0, 1), 1f);
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        ShaderProgram program = box.getMaterial().getProgram();

        glUseProgram(program.getProgramId());

        camera.getProjection().get(projectionBuffer);
        camera.getView().get(viewBuffer);
        box.getModel().get(modelBuffer);

        glUniformMatrix4fv(program.getUniformLocation("projection"), false, projectionBuffer);
        glUniformMatrix4fv(program.getUniformLocation("view"), false, viewBuffer);
        glUniformMatrix4fv(program.getUniformLocation("model"), false, modelBuffer);

        box.bindVao();
        box.enableVertexAttributes();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, box.getIndicesVboID());
        glDrawElements(box.getGeometry().getDrawMode(), box.getIndicesCount(), GL_UNSIGNED_SHORT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        box.disableVertexAttributes();
        box.unbindVao();

        glUseProgram(0);

        glfwSwapBuffers(window);
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        //all initialization and gl code needs to be after the above call.

        camera = new PerspectiveCamera(75,  (float)(game.width)/(game.height), 0.00001f, 100);
        camera.moveForward(-5);

        Geometry boxGeometry = Draw3dUtils.cubeGeometry(1, 1, 1, 1, 1, 1);
        Material boxMaterial = new Material();
        box = new Mesh(boxGeometry, boxMaterial);
        box.translate(new Vector3f(0, 0, 0));

        projectionBuffer = BufferUtils.createFloatBuffer(16);
        viewBuffer = BufferUtils.createFloatBuffer(16);
        modelBuffer = BufferUtils.createFloatBuffer(16);

        // Set the clear color
        glClearColor(0.5f, 0.5f, 0.5f, 0.0f);

        /* Declare buffers for using inside the loop */
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);

        /* Loop until window gets closed */
        while (glfwWindowShouldClose(window) != GLFW_TRUE) {
            float ratio;

            /* Get width and height to calcualte the ratio */
            glfwGetFramebufferSize(window, width, height);
            ratio = width.get() / (float) height.get();

            /* Rewind buffers for next get */
            width.rewind();
            height.rewind();

            /* Set viewport and clear screen */
            glViewport(0, 0, width.get(), height.get());

            update();
            render();

            glfwPollEvents();

            /* Flip buffers for next loop */
            width.flip();
            height.flip();
        }
    }
}
