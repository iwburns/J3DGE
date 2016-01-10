package engine.opengl.vao;

import engine.opengl.vbo.FloatVertexBufferObject;
import engine.opengl.vbo.ShortIndexBufferObject;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArrayObject {

    private final int id;
    private final boolean indexed;
    private FloatVertexBufferObject verticesVbo;
    private FloatVertexBufferObject colorsVbo;
    private ShortIndexBufferObject indicesVbo;

    public VertexArrayObject(boolean indexed) {
        id = glGenVertexArrays();
        this.indexed = indexed;
    }

    public void bind() {
        glBindVertexArray(id);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void delete() {
        glDeleteVertexArrays(id);
    }

    public int getId() {
        return id;
    }

    public void sendVboDataAutoBind() {
        bind();
        sendVboData();
        unbind();
    }

    public void sendVboData() {
        verticesVbo.sendDataAutoBind();
        colorsVbo.sendDataAutoBind();
        if (indexed) {
            indicesVbo.sendDataAutoBind();
        }
    }

    public FloatVertexBufferObject getVerticesVbo() {
        return verticesVbo;
    }

    public void setVerticesVbo(FloatVertexBufferObject verticesVbo) {
        this.verticesVbo = verticesVbo;
    }

    public FloatVertexBufferObject getColorsVbo() {
        return colorsVbo;
    }

    public void setColorsVbo(FloatVertexBufferObject colorsVbo) {
        this.colorsVbo = colorsVbo;
    }

    public ShortIndexBufferObject getIndicesVbo() {
        return indicesVbo;
    }

    public void setIndicesVbo(ShortIndexBufferObject vbo) {
        if (!indexed) {
            System.out.println("ERROR: This VertexArrayObject is not indexed and cannot have an indicesVbo added to it.");
            return;
        }

        indicesVbo = vbo;
    }

    public boolean isIndexed() {
        return indexed;
    }
}
