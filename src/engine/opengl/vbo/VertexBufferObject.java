package engine.opengl.vbo;

import java.nio.Buffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public abstract class VertexBufferObject extends BufferObject {

    protected ArrayList<VertexAttributePointer> vertexAttributePointers = new ArrayList<>();

    protected VertexBufferObject() {
        this(BUFFER_TARGET_ARRAY_BUFFER, USAGE_HINT_STATIC_DRAW);
    }

    protected VertexBufferObject(int bufferTarget, int usageHint) {
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
        sendVertexAttributes();
    }

    public void addVertexAttributePointer(VertexAttributePointer vertexAttributePointer) {
        vertexAttributePointers.add(vertexAttributePointer);
    }

    public void removeVertexAttributePointer(VertexAttributePointer vertexAttributePointer) {
        vertexAttributePointers.remove(vertexAttributePointer);
    }

    public void sendVertexAttributesAutoBind() {
        bind();
        sendVertexAttributes();
        unbind();
    }

    public void sendVertexAttributes() {
        vertexAttributePointers.forEach(pointer -> {
            glVertexAttribPointer(
                    pointer.getLocation(),
                    pointer.getSize(),
                    pointer.getType(),
                    pointer.isNormalized(),
                    pointer.getStride(),
                    pointer.getOffset()
            );
        });
    }

}
