package engine.opengl.vbo;

import java.nio.Buffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL15.glBufferData;

public class ShortVertexBufferObject extends VertexBufferObject {

    private ShortBuffer bufferData;

    public ShortVertexBufferObject(ShortBuffer bufferData) {
        super();
        this.bufferData = bufferData;
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
        bufferData = (ShortBuffer) b;
    }

    @Override
    public int getSize() {
        return bufferData.capacity();
    }
}
