package engine.opengl;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class VertexBufferObject {

    //todo: possibly make this hold the actual buffer data too

    private final int id;

    public VertexBufferObject() {
        id = glGenBuffers();
    }

    public void bind(int target) {
        glBindBuffer(target, id);
    }

    public void delete() {
        glDeleteBuffers(id);
    }

    public int getId() {
        return id;
    }

}
