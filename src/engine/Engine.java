package engine;

import engine.keyboard.Keyboard;
import engine.render.Renderer;
import engine.window.Window;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Engine {

    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;

    // The window handle
    private Window window;
    private Keyboard keyboard;

    private Timer timer = new Timer();
    private Renderer renderer = new Renderer();

    public Game game;

    public Engine(Game g) {
        errorCallback = GLFWErrorCallback.createPrint(System.err);
        game = g;

        keyboard = new Keyboard(game.getKeyCallback());
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        try {
            init();
            loop();
            cleanUpWindow();
        } finally {
            cleanUpGLFW();
        }
    }

    private void init() {
        initGLFW();
        initOpenGL();
        game.init();
    }

    private void initGLFW() {
        glfwSetErrorCallback(errorCallback);

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GLFW_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = new Window(game.width, game.height, "n-body-java", NULL, NULL);
        if ( window.getWindowHandle() == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window.getWindowHandle(), keyboard.getGLFWKeyCallback());

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        window.setPosition(
                (vidmode.width() - game.width) / 2,
                (vidmode.height() - game.height) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window.getWindowHandle());
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window.getWindowHandle());
    }

    private void initOpenGL() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        //all initialization and gl code needs to be after the above call.

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_PROGRAM_POINT_SIZE);
    }

    private void update() {
        game.update();
    }

    private void render() {
        renderer.render(game.getScene(), game.getCamera());
        window.update();
    }

    private void loop() {
        while (!window.shouldClose()) {
            glfwPollEvents();
            update();
            render();
        }
    }

    private void cleanUpWindow() {
        // Release window and window callbacks
        window.destroy();
        keyboard.destroy();
        //TODO: add mouse here (and elsewhere)
    }

    private void cleanUpGLFW() {
        // Terminate GLFW and release the GLFWErrorCallback
        glfwTerminate();
        errorCallback.release();
    }
}
