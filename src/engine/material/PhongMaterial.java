package engine.material;

import engine.shader.ShaderProgram;
import engine.util.Color;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public class PhongMaterial extends Material {

    protected Color specularColor;
    protected float specularCoefficient;

    public PhongMaterial(Color specularColor, float specularCoefficient) {
        program = new ShaderProgram();

        program.attachShader("res/shaders/phongVertex.glsl", GL_VERTEX_SHADER);
        program.attachShader("res/shaders/phongFragment.glsl", GL_FRAGMENT_SHADER);

        program.link();
        program.validate();

        this.specularColor = specularColor;
        this.specularCoefficient = specularCoefficient;
    }

    public PhongMaterial() {
        this(new Color(1, 1, 1), 0.5f);
    }

    public Color getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Color specularColor) {
        this.specularColor = specularColor;
    }

    public float getSpecularCoefficient() {
        return specularCoefficient;
    }

    public void setSpecularCoefficient(float specularCoefficient) {
        this.specularCoefficient = specularCoefficient;
    }
}
