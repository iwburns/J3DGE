package engine.opengl.vao;

import engine.opengl.vbo.VertexBufferObject;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArrayObject {

    private final int id;
    private final boolean indexed;
    private VertexBufferObject verticesVbo;
    private VertexBufferObject colorsVbo;
    private VertexBufferObject indicesVbo;

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
        verticesVbo.sendAllDataAutoBind();
        colorsVbo.sendAllDataAutoBind();
        if (indexed) {
            indicesVbo.sendAllDataAutoBind();
        }
    }

    public VertexBufferObject getVerticesVbo() {
        return verticesVbo;
    }

    public void setVerticesVbo(VertexBufferObject verticesVbo) {
        this.verticesVbo = verticesVbo;
    }

    public VertexBufferObject getColorsVbo() {
        return colorsVbo;
    }

    public void setColorsVbo(VertexBufferObject colorsVbo) {
        this.colorsVbo = colorsVbo;
    }

    public VertexBufferObject getIndicesVbo() {
        return indicesVbo;
    }

    public void setIndicesVbo(VertexBufferObject vbo) {
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
