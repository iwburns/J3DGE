package engine.opengl.vbo;

import java.nio.Buffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glBufferData;

public class FloatVertexBufferObject extends VertexBufferObject {

    private FloatBuffer bufferData;

    public FloatVertexBufferObject(FloatBuffer bufferData) {
        super();
        this.bufferData = bufferData;
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

    @Override
    public void sendBufferData() {
        glBufferData(bufferTarget, bufferData, usageHint);
    }
}
