package engine.opengl.vbo;

import java.nio.Buffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class ShortVertexBufferObject extends VertexBufferObject {

    private ShortBuffer bufferData;

    public ShortVertexBufferObject(ShortBuffer bufferData, int bufferTarget) {
        this(bufferData, bufferTarget, USAGE_HINT_STATIC_DRAW);
    }

    public ShortVertexBufferObject(ShortBuffer bufferData, int bufferTarget, int usageHint) {
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
        bufferData = (ShortBuffer) b;
    }
}
