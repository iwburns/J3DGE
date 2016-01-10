package engine.opengl.vbo;

import java.nio.*;

import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class FloatVertexBufferObject extends VertexBufferObject {

    private FloatBuffer bufferData;

    public FloatVertexBufferObject(FloatBuffer bufferData, int bufferTarget) {
        this(bufferData, bufferTarget, USAGE_HINT_STATIC_DRAW);
    }

    public FloatVertexBufferObject(FloatBuffer bufferData, int bufferTarget, int usageHint) {
        id = glGenBuffers();
        this.bufferData = bufferData;
        this.bufferTarget = bufferTarget;
        this.usageHint = usageHint;
    }

    @Override
    public void sendBufferData() {
        glBufferData(bufferTarget, bufferData, usageHint);
    }

    @Override
    public Buffer getBufferData() {
        return bufferData;
    }

    @Override
    public void setBufferData(Buffer b) {
        bufferData = (FloatBuffer) b;
    }

    @Override
    public int getSize() {
        return bufferData.capacity();
    }
}
