package engine.opengl.vao;

import engine.opengl.vbo.VertexBufferObject;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArrayObject {

    private final int id;
    private final boolean indexed;
    private ArrayList<VertexBufferObject> vertexBufferObjects;
    private VertexBufferObject indicesVbo; //for now, this one is special

    public VertexArrayObject(boolean indexed) {
        id = glGenVertexArrays();
        this.indexed = indexed;
        vertexBufferObjects = new ArrayList<>();
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

    public void addVertexBufferObject(VertexBufferObject vbo) {
        vertexBufferObjects.add(vbo);
    }

    public void removeVertexBufferObject(VertexBufferObject vbo) {
        vertexBufferObjects.remove(vbo);
    }

    public ArrayList<VertexBufferObject> getVertexBufferObjects() {
        return vertexBufferObjects;
    }

    public void sendVboDataAutoBind() {
        bind();
        sendVboData();
        unbind();
    }

    public void sendVboData() {
        vertexBufferObjects.forEach(VertexBufferObject::sendAllDataAutoBind);
        if (indexed) {
            indicesVbo.sendAllDataAutoBind();
        }
    }

    public void setIndicesVbo(VertexBufferObject vbo) {
        if (!indexed) {
            System.out.println("This VertexArrayObject is not indexed and cannot have an indicesVbo added to it.");
            return;
        }

        indicesVbo = vbo;
    }

    public VertexBufferObject getIndicesVbo() {
        return indicesVbo;
    }

    public boolean isIndexed() {
        return indexed;
    }
}
