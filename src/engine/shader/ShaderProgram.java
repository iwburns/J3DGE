package engine.shader;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    public static final String DEFAULT_POSITION_ATTRIBUTE_NAME = "position";
    public static final String DEFAULT_COLOR_ATTRIBUTE_NAME = "color";

    public String positionAttributeName;
    public String colorAttributeName;
    private int programId;

    private HashMap<String, Integer> attributes = new HashMap<>();
    private HashMap<String, Integer> uniforms = new HashMap<>();
    private ArrayList<Shader> shaders = new ArrayList<>();

    public ShaderProgram() {
        this(DEFAULT_POSITION_ATTRIBUTE_NAME, DEFAULT_COLOR_ATTRIBUTE_NAME);
    }

    public ShaderProgram(String positionAttributeName) {
        this(positionAttributeName, DEFAULT_COLOR_ATTRIBUTE_NAME);
    }

    public ShaderProgram(String positionAttributeName, String colorAttributeName) {
        programId = glCreateProgram();
        this.positionAttributeName = positionAttributeName;
        this.colorAttributeName = colorAttributeName;
    }

    private void addAttribute(String name) {
        int location = glGetAttribLocation(programId, name);
        attributes.put(name, location);
    }

    private void addUniform(String name) {
        int location = glGetUniformLocation(programId, name);
        uniforms.put(name, location);
    }

    public void attachShader(String filename, int type) {
        Shader shader = new Shader(filename, type);
        attachShader(shader);
    }

    public void attachShader(Shader shader) {
        shaders.add(shader);
        glAttachShader(programId, shader.getShaderId());
    }

    public void destroy() {
        glUseProgram(0);

        shaders.forEach(shader -> {
            glDetachShader(programId, shader.getShaderId());
            glDeleteShader(shader.getShaderId());
        });

        glDeleteProgram(programId);
    }

    private void findAttributes() {
        IntBuffer lengthBuff = BufferUtils.createIntBuffer(1);
        IntBuffer sizeBuff = BufferUtils.createIntBuffer(1);
        IntBuffer typeBuff = BufferUtils.createIntBuffer(1);
        ByteBuffer nameBuff = BufferUtils.createByteBuffer(50); //TODO: find a way to know how big this needs to be.

        int length;
        int size;
        int type;
        byte[] nameBytes;
        String name;

        int numAttributes = glGetProgrami(programId, GL_ACTIVE_ATTRIBUTES);

        for (int i = 0; i < numAttributes; i++) {
            glGetActiveAttrib(programId, i, lengthBuff, sizeBuff, typeBuff, nameBuff);
            length = lengthBuff.get(0);
            size = sizeBuff.get(0);
            type = typeBuff.get(0);

            nameBytes = new byte[length];
            nameBuff.get(nameBytes, 0, length);
            name = new String(nameBytes, StandardCharsets.UTF_8); //TODO: find out if this should be the default charset

            addAttribute(name);
        }
    }

    private void findUniforms() {
        IntBuffer lengthBuff = BufferUtils.createIntBuffer(1);
        IntBuffer sizeBuff = BufferUtils.createIntBuffer(1);
        IntBuffer typeBuff = BufferUtils.createIntBuffer(1);
        ByteBuffer nameBuff = BufferUtils.createByteBuffer(50); //TODO: find a way to know how big this needs to be.

        int length;
        int size;
        int type;
        byte[] nameBytes;
        String name;

        int numUniforms = glGetProgrami(programId, GL_ACTIVE_UNIFORMS);

        for (int i = 0; i < numUniforms; i++) {
            glGetActiveUniform(programId, i, lengthBuff, sizeBuff, typeBuff, nameBuff);
            length = lengthBuff.get(0);
            size = sizeBuff.get(0);
            type = typeBuff.get(0);

            nameBytes = new byte[length];
            nameBuff.get(nameBytes, 0, length);
            name = new String(nameBytes, StandardCharsets.UTF_8); //TODO: find out if this should be the default charset

            addUniform(name);
        }
    }

    public int getAttributeLocation(String name) {
        return attributes.get(name);
    }

    public Collection<Integer> getAttributeLocations() {
        return attributes.values();
    }

    public int getProgramId() {
        return programId;
    }

    public int getUniformLocation(String name) {
        return uniforms.get(name);
    }

    public Collection<Integer> getUniformLocations() {
        return uniforms.values();
    }

    public void link() {
        glLinkProgram(programId);

        if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE) {
            System.err.println("ShaderProgram failed to link...");
            System.err.println(glGetProgramInfoLog(programId, GL_INFO_LOG_LENGTH));
        }

        findAttributes();
        findUniforms();
    }

    public void validate() {
        glValidateProgram(programId);

        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println("ShaderProgram is invalid...");
            System.err.println(glGetProgramInfoLog(programId, GL_INFO_LOG_LENGTH));
        }
    }
}
