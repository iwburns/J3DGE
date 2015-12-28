package engine;

public class Game {

    public final int TARGET_FPS = 60;
    public final int TARGET_UPS = 30;

    public int width = 800;
    public int height = 600;

    public Game() {

    }

    public Game(int width, int height) {
        this.width = width;
        this.height = height;
    }

}
