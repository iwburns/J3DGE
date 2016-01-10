package engine.opengl.vbo;

import java.nio.Buffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class ShortVertexBufferObject extends VertexBufferObject {

    ShortBuffer bufferData;

    public ShortVertexBufferObject(int bufferTarget) {
        this(bufferTarget, USAGE_HINT_STATIC_DRAW);
    }

    public ShortVertexBufferObject(int bufferTarget, int usageHint) {
        id = glGenBuffers();
        this.bufferTarget = bufferTarget;
        this.usageHint = usageHint;
    }

    @Override
    public void sendBufferData(boolean autoBind) {
        if (autoBind) {
            bind();
        }
        glBufferData(bufferTarget, bufferData, usageHint);
        if (autoBind) {
            unbind();
        }
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
