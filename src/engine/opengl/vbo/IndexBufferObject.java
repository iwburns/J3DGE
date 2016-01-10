package engine.opengl.vbo;

import java.nio.Buffer;

public abstract class IndexBufferObject extends BufferObject {

    protected IndexBufferObject() {
        this(BUFFER_TARGET_ELEMENT_ARRAY_BUFFER, USAGE_HINT_STATIC_DRAW);
    }

    protected IndexBufferObject(int bufferTarget, int usageHint) {
        super(bufferTarget, usageHint);
    }

    public abstract Buffer getBufferData();

    public abstract void setBufferData(Buffer b);

    public abstract int getSize();

    public abstract void sendBufferData();

    public void sendBufferDataAutoBind() {
        bind();
        sendBufferData();
        unbind();
    }

    @Override
    public void sendData() {
        sendBufferData();
    }
}
