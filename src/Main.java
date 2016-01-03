import engine.Engine;
import engine.Game;

public class Main {

    public static void main(String[] args) {
        Demo d = new Demo();
        Engine gameEngine = new Engine(d);

        gameEngine.run();
    }

}