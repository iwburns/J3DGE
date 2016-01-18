package engine.material;

import engine.shader.ShaderProgram;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class PhongMaterial extends Material {

    public PhongMaterial() {
        program = new ShaderProgram();

        program.attachShader("res/shaders/phongVertex.glsl", GL_VERTEX_SHADER);
        program.attachShader("res/shaders/phongFragment.glsl", GL_FRAGMENT_SHADER);

        program.link();
        program.validate();
    }

}
