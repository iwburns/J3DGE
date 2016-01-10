package engine.opengl.vbo;

import java.nio.Buffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public abstract class VertexBufferObject {

    //todo: add other dataType options here.
    public static int DATA_TYPE_FLOAT = GL_FLOAT;

    public static int BUFFER_TARGET_ARRAY_BUFFER = GL_ARRAY_BUFFER;
    public static int BUFFER_TARGET_ELEMENT_ARRAY_BUFFER = GL_ELEMENT_ARRAY_BUFFER;

    public static int USAGE_HINT_STATIC_DRAW = GL_STATIC_DRAW;
    public static int USAGE_HINT_DYNAMIC_DRAW = GL_DYNAMIC_DRAW;

    protected int id;
    protected int bufferTarget;
    protected int usageHint;

    public void bind() {
        glBindBuffer(bufferTarget, id);
    }

    public void unbind() {
        glBindBuffer(bufferTarget, 0);
    }

    public void delete() {
        unbind();
        glDeleteBuffers(id);
    }

    public int getId() {
        return id;
    }

    public int getBufferTarget() {
        return bufferTarget;
    }

    public int getUsageHint() {
        return usageHint;
    }

    public void setUsageHint(int usageHint) {
        this.usageHint = usageHint;
    }

    public abstract void sendBufferData();

    public abstract Buffer getBufferData();

    public abstract void setBufferData(Buffer b);

    public void addAttributePointer(int attributeIndex, int size, int dataType, boolean normalized, int stride, long offset) {
        //TODO: make a class to manage these.
        glVertexAttribPointer(attributeIndex, size, dataType, normalized, stride, offset);
    }
}
