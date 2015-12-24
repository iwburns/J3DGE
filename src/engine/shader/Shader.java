package engine.shader;

public class Shader {

    private int shaderId;
    private int type;

    public Shader(String filename, int type) {
        shaderId = ShaderUtils.loadShader(filename, type);
        this.type = type;
    }

    public int getShaderId() {
        return shaderId;
    }

    public int getType() {
        return type;
    }

}
