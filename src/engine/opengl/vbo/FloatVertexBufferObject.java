package engine.opengl.vbo;

import java.nio.*;

import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

public class FloatVertexBufferObject extends VertexBufferObject {

    private FloatBuffer bufferData;

    public FloatVertexBufferObject(int bufferTarget) {
        this(bufferTarget, USAGE_HINT_STATIC_DRAW);
    }

    public FloatVertexBufferObject(int bufferTarget, int usageHint) {
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
        bufferData = (FloatBuffer) b;
    }
}
