package engine.opengl;

import static org.lwjgl.opengl.GL15.*;

public class VertexBufferObject {

    //todo: possibly make this hold the actual buffer data too
    
    public static int BUFFER_TARGET_ARRAY_BUFFER = GL_ARRAY_BUFFER;
    public static int BUFFER_TARGET_ELEMENT_ARRAY_BUFFER = GL_ELEMENT_ARRAY_BUFFER;

    public static int USAGE_HINT_STATIC_DRAW = GL_STATIC_DRAW;
    public static int USAGE_HINT_DYNAMIC_DRAW = GL_DYNAMIC_DRAW;

    private final int id;
    private final int bufferTarget;
    private int usageHint;

    public VertexBufferObject(int bufferTarget) {
        id = glGenBuffers();
        this.bufferTarget = bufferTarget;
        usageHint = USAGE_HINT_STATIC_DRAW;
    }

    public void bind() {
        glBindBuffer(bufferTarget, id);
    }

    public void unbind() {
        glBindBuffer(bufferTarget, 0);
    }

    public void deleteBuffer() {
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

}
