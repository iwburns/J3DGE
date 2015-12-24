package engine.material;

import engine.shader.ShaderProgram;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class Material {

    private ShaderProgram program;

    public Material() {
        program = new ShaderProgram();

        program.attachShader("res/shaders/vertex.glsl", GL_VERTEX_SHADER);
        program.attachShader("res/shaders/fragment.glsl", GL_FRAGMENT_SHADER);

        program.link();
        program.validate();
    }

    public ShaderProgram getProgram() {
        return program;
    }

    public void destroyShaderProgram() {
        program.destroy();
    }
}
