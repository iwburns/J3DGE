import engine.Engine;
import engine.Game;

public class Main {

    public static void main(String[] args) {
        Game g = new Game();
        Engine gameEngine = new Engine(g);

        gameEngine.run();
    }

}