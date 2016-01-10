package engine.opengl.vbo;

import static org.lwjgl.opengl.GL15.*;

public abstract class BufferObject {

    public static int BUFFER_TARGET_ARRAY_BUFFER = GL_ARRAY_BUFFER;
    public static int BUFFER_TARGET_ELEMENT_ARRAY_BUFFER = GL_ELEMENT_ARRAY_BUFFER;

    public static int USAGE_HINT_STATIC_DRAW = GL_STATIC_DRAW;
    public static int USAGE_HINT_DYNAMIC_DRAW = GL_DYNAMIC_DRAW;

    protected final int id;
    protected final int bufferTarget;
    protected final int usageHint;

    protected BufferObject(int bufferTarget, int usageHint) {
        id = glGenBuffers();
        this.bufferTarget = bufferTarget;
        this.usageHint = usageHint;
    }

    public abstract void sendData();

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

    public void sendDataAutoBind() {
        bind();
        sendData();
        unbind();
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

}
