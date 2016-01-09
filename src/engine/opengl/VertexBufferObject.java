package engine.opengl;

import java.nio.*;

import static org.lwjgl.opengl.GL15.*;

public class VertexBufferObject<BufferType> {
    
    public static int BUFFER_TARGET_ARRAY_BUFFER = GL_ARRAY_BUFFER;
    public static int BUFFER_TARGET_ELEMENT_ARRAY_BUFFER = GL_ELEMENT_ARRAY_BUFFER;

    public static int USAGE_HINT_STATIC_DRAW = GL_STATIC_DRAW;
    public static int USAGE_HINT_DYNAMIC_DRAW = GL_DYNAMIC_DRAW;

    private final int id;
    private final int bufferTarget;
    private int usageHint;

    private BufferType bufferData;

    public VertexBufferObject(int bufferTarget) {
        id = glGenBuffers();
        this.bufferTarget = bufferTarget;
        usageHint = USAGE_HINT_STATIC_DRAW;
    }

    public void setBufferData(BufferType bufferData) {
        this.bufferData = bufferData;
    }

    public BufferType getBufferData() {
        return bufferData;
    }

    public void sendBufferData() {
        //todo: find a better way
        if (bufferData instanceof ByteBuffer) {
            glBufferData(bufferTarget, (ByteBuffer) bufferData, usageHint);
        } else if (bufferData instanceof DoubleBuffer) {
            glBufferData(bufferTarget, (DoubleBuffer) bufferData, usageHint);
        } else if (bufferData instanceof FloatBuffer) {
            glBufferData(bufferTarget, (FloatBuffer) bufferData, usageHint);
        } else if (bufferData instanceof IntBuffer) {
            glBufferData(bufferTarget, (IntBuffer) bufferData, usageHint);
        } else if (bufferData instanceof ShortBuffer) {
            glBufferData(bufferTarget, (ShortBuffer) bufferData, usageHint);
        }
    }

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

}
